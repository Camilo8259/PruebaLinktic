package com.linktic.inventario.controller;

import com.linktic.inventario.dto.InventarioRequestDTO;
import com.linktic.inventario.dto.InventarioResponseDTO;
import com.linktic.inventario.model.Inventario;
import com.linktic.inventario.service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventarios")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @GetMapping(value = "/{productoId}", produces = "application/vnd.api+json")
    public ResponseEntity<InventarioResponseDTO> getInventario(@PathVariable Long productoId) {
        String nombreProducto = service.validateAndGetProductName(productoId);
        Inventario inventario = service.getInventarioByProductoId(productoId);
        return ResponseEntity.ok(new InventarioResponseDTO(inventario, nombreProducto));
    }

    @PatchMapping(value = "/{productoId}", consumes = "application/vnd.api+json", produces = "application/vnd.api+json")
    public ResponseEntity<InventarioResponseDTO> updateInventario(@PathVariable Long productoId,
                                                                  @RequestBody InventarioRequestDTO request) {
        String nombreProducto = service.validateAndGetProductName(productoId);
        Integer cantidad = request.getData().getAttributes().getCantidad();
        Inventario actualizado = service.updateStock(productoId, cantidad);

        return ResponseEntity.ok(new InventarioResponseDTO(actualizado, nombreProducto));
    }
}