package com.linktic.productos.servicio;

import com.linktic.productos.dto.ProductoRequestDTO;
import com.linktic.productos.exception.ResourceNotFoundException;
import com.linktic.productos.modelo.Producto;
import com.linktic.productos.repositorio.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoServicio {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServicio.class);
    private final ProductoRepository repository;

    public ProductoServicio(ProductoRepository repository) {
        this.repository = repository;
    }

    public Page<Producto> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Producto findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El producto con ID " + id + " no existe"));
    }

    public Producto save(ProductoRequestDTO request) {
        Producto producto = new Producto();
        producto.setNombre(request.getData().getAttributes().getNombre());
        producto.setPrecio(request.getData().getAttributes().getPrecio());
        Producto saved = repository.save(producto);
        logger.info("[LOG] Producto creado exitosamente con ID: {}", saved.getId());
        return saved;
    }

    public Optional<Producto> update(Long id, ProductoRequestDTO request) {
        return Optional.of(repository.findById(id).map(existingProducto -> {
            existingProducto.setNombre(request.getData().getAttributes().getNombre());
            existingProducto.setPrecio(request.getData().getAttributes().getPrecio());
            return repository.save(existingProducto);
        }).orElseThrow(() -> new ResourceNotFoundException("No se pudo actualizar. ID " + id + " no existe")));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("No se pudo eliminar. ID " + id + " no existe");
        }
        repository.deleteById(id);
        logger.info("[LOG] Producto con ID {} eliminado", id);
    }
}