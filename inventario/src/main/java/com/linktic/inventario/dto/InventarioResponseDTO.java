package com.linktic.inventario.dto;

import com.linktic.inventario.model.Inventario;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class InventarioResponseDTO {
    private DataWrapper data;

    public InventarioResponseDTO(Inventario inventario, String nombreProducto) {
        this.data = new DataWrapper(inventario, nombreProducto);
    }

    @Data
    public static class DataWrapper {
        private String type = "inventarios";
        private String id;
        private InventoryAttributes attributes;

        public DataWrapper(Inventario inventario, String nombreProducto) {
            this.id = inventario.getId().toString();
            this.attributes = new InventoryAttributes(
                    inventario.getProductoId(),
                    inventario.getCantidad(),
                    nombreProducto
            );
        }
    }

    @Data
    @AllArgsConstructor
    public static class InventoryAttributes {
        private Long producto_id;
        private Integer cantidad;
        private String producto_nombre;
    }
}