package com.linktic.inventario.service;

import com.linktic.inventario.model.Inventario;
import com.linktic.inventario.repository.InventarioRepository;
import com.linktic.inventario.exception.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(InventarioServiceTest.class);

    @Mock
    private InventarioRepository repository;

    @InjectMocks
    private InventarioService service;

    @Test
    @DisplayName("Actualizar stock de un producto existente")
    void testUpdateStockSuccess() {
        Long productoId = 10L;
        Integer cantidadNueva = 500;
        Inventario inventarioMock = new Inventario(1L, productoId, 100);

        when(repository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioMock));
        when(repository.save(any(Inventario.class))).thenReturn(inventarioMock);

        Inventario resultado = service.updateStock(productoId, cantidadNueva);

        assertNotNull(resultado);
        assertEquals(cantidadNueva, resultado.getCantidad());
        verify(repository, times(1)).save(any(Inventario.class));

        logger.info("[TEST EXITOSO] Happy Path: Se actualizó el stock correctamente a {} para el producto ID {}.",
                cantidadNueva, productoId);
    }

    @Test
    @DisplayName("Lanzar excepción si el inventario solicitado no existe")
    void testGetInventarioNotFound() {
        Long idInexistente = 999L;

        when(repository.findByProductoId(idInexistente)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            service.getInventarioByProductoId(idInexistente);
        });

        assertTrue(exception.getMessage().contains(String.valueOf(idInexistente)));

        logger.info("[TEST EXITOSO] Validación de inventario inexistente para el ID {} confirmada.",
                idInexistente);
    }

    @Test
    @DisplayName("Crear registro de inventario si el producto existe pero no tiene stock previo")
    void testUpdateStockNewRecord() {
        Long productoId = 5L;
        Integer cantidad = 20;

        when(repository.findByProductoId(productoId)).thenReturn(Optional.empty());
        when(repository.save(any(Inventario.class))).thenAnswer(i -> i.getArguments()[0]);

        Inventario resultado = service.updateStock(productoId, cantidad);

        assertEquals(cantidad, resultado.getCantidad());
        assertEquals(productoId, resultado.getProductoId());

        logger.info("[TEST EXITOSO] Se creó un nuevo registro de inventario para el producto ID {}.",
                productoId);
    }
}