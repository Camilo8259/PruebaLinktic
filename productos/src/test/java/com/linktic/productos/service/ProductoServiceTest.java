package com.linktic.productos.service;

import com.linktic.productos.modelo.Producto;
import com.linktic.productos.repositorio.ProductoRepository;
import com.linktic.productos.exception.ResourceNotFoundException;
import com.linktic.productos.servicio.ProductoServicio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ProductoServiceTest.class);

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoServicio service;

    @Test
    @DisplayName("Obtener producto por ID correctamente")
    void testGetProductoByIdSuccess() {

        Producto productoMock = new Producto();
        productoMock.setId(1L);
        productoMock.setNombre("Monitor");
        productoMock.setPrecio(BigDecimal.valueOf(250.0));

        when(repository.findById(1L)).thenReturn(Optional.of(productoMock));

        Producto resultado = service.findById(1L);

        assertNotNull(resultado, "El producto no debería ser nulo");
        assertEquals("Monitor", resultado.getNombre(), "El nombre del producto debe coincidir");

        logger.info("[TEST EXITOSO] El servicio recuperó correctamente el producto '{}' con ID 1.", resultado.getNombre());
    }

    @Test
    @DisplayName("Lanzar excepción cuando el producto no existe")
    void testGetProductoNotFound() {
        Long idInexistente = 7L;
        when(repository.findById(idInexistente)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(idInexistente);
        });

        assertTrue(exception.getMessage().contains(String.valueOf(idInexistente)));

        logger.info("[TEST EXITOSO] Se capturó correctamente la excepción ResourceNotFound para el producto ID {}.", idInexistente);
    }
}