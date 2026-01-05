package com.linktic.productos.dto;

import com.linktic.productos.modelo.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoResponseDTO {
    private DataWrapper data;

    public ProductoResponseDTO(Producto producto) {
        this.data = new DataWrapper(producto);
    }

    @Data
    public static class DataWrapper {
        private String type = "products";
        private String id;
        private ProductAttributes attributes;

        public DataWrapper(Producto producto) {
            this.id = producto.getId().toString();
            this.attributes = new ProductAttributes(producto.getNombre(), producto.getPrecio());
        }
    }

    @Data
    @AllArgsConstructor
    public static class ProductAttributes {
        private String nombre;
        private BigDecimal precio;
    }
}