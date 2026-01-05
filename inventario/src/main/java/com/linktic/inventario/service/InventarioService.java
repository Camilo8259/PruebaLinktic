package com.linktic.inventario.service;

import com.linktic.inventario.dto.external.ProductExternalDTO;
import com.linktic.inventario.exception.ResourceNotFoundException;
import com.linktic.inventario.model.Inventario;
import com.linktic.inventario.repository.InventarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class InventarioService {

    private static final Logger logger = LoggerFactory.getLogger(InventarioService.class);

    private final InventarioRepository repository;
    private final RestTemplate restTemplate;

    @Value("${products.service.url}")
    private String productsServiceUrl;

    @Value("${app.api-key}")
    private String apiKey;

    public InventarioService(InventarioRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    public Inventario getInventarioByProductoId(Long productoId) {
        return repository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("No hay registros de inventario para el producto con ID: " + productoId));
    }

    public String validateAndGetProductName(Long productoId) {
        int maxIntentos = 3;
        for (int i = 1; i <= maxIntentos; i++) {
            try {
                logger.info("Validando existencia del producto ID: {} (Intento {})", productoId, i);

                HttpHeaders headers = new HttpHeaders();
                headers.set("X-API-KEY", apiKey);
                headers.set("Accept", "application/vnd.api+json");
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<ProductExternalDTO> response = restTemplate.exchange(
                        productsServiceUrl + "/api/v1/productos/" + productoId,
                        HttpMethod.GET,
                        entity,
                        ProductExternalDTO.class
                );

                if (response.getBody() != null && response.getBody().getData() != null) {
                    return response.getBody().getData().getAttributes().getNombre();
                }
            } catch (HttpClientErrorException.NotFound e) {
                logger.error("El producto con ID {} no existe en el sistema central.", productoId);
                throw new ResourceNotFoundException("El producto con ID " + productoId + " no existe");
            } catch (ResourceAccessException e) {
                logger.warn("Falla de red en intento {}. Reintentando...", i);
                if (i == maxIntentos) throw e;
            } catch (Exception e) {
                logger.error("Error técnico al validar producto: {}", e.getMessage());
                throw new RuntimeException("Error en la validación remota del producto");
            }
        }
        throw new ResourceNotFoundException("No se pudo validar el producto con ID " + productoId);
    }

    public Inventario updateStock(Long productoId, Integer nuevaCantidad) {
        Inventario inventario = repository.findByProductoId(productoId)
                .orElse(new Inventario(null, productoId, 0));

        inventario.setCantidad(nuevaCantidad);
        Inventario saved = repository.save(inventario);

        logger.info("[EVENTO] Inventario actualizado - Producto ID: {}, Nueva Cantidad: {}",
                productoId, nuevaCantidad);
        return saved;
    }
}