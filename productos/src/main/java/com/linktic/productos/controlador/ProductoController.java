package com.linktic.productos.controlador;

import com.linktic.productos.dto.ProductoRequestDTO;
import com.linktic.productos.dto.ProductoResponseDTO;
import com.linktic.productos.modelo.Producto;
import com.linktic.productos.servicio.ProductoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @Autowired
    private ProductoServicio servicio;

    @GetMapping(produces = "application/vnd.api+json")
    public ResponseEntity<Map<String, Object>> getAllProducts(Pageable pageable) {
        Page<Producto> productPage = servicio.findAll(pageable);

        List<ProductoResponseDTO.DataWrapper> data = productPage.getContent().stream()
                .map(producto -> new ProductoResponseDTO(producto).getData())
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);

        Map<String, Object> meta = new HashMap<>();
        meta.put("totalItems", productPage.getTotalElements());
        meta.put("totalPages", productPage.getTotalPages());
        response.put("meta", meta);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}", produces = "application/vnd.api+json")
    public ResponseEntity<ProductoResponseDTO> getProductById(@PathVariable Long id) {
        Producto producto = servicio.findById(id);
        return ResponseEntity.ok(new ProductoResponseDTO(producto));
    }

    @PostMapping(consumes = "application/vnd.api+json", produces = "application/vnd.api+json")
    public ResponseEntity<ProductoResponseDTO> createProduct(@RequestBody ProductoRequestDTO request) {
        Producto newProducto = servicio.save(request);
        return new ResponseEntity<>(new ProductoResponseDTO(newProducto), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", consumes = "application/vnd.api+json", produces = "application/vnd.api+json")
    public ResponseEntity<ProductoResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductoRequestDTO request) {
        return servicio.update(id, request)
                .map(product -> ResponseEntity.ok(new ProductoResponseDTO(product)))
                .get();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        servicio.delete(id);
        return ResponseEntity.noContent().build();
    }
}