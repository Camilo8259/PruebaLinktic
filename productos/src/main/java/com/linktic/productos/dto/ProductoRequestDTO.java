package com.linktic.productos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoRequestDTO {
    private DataRequest data;

    @Data
    public static class DataRequest {
        private String type;
        private AttributesRequest attributes;
    }

    @Data
    public static class AttributesRequest {
        private String nombre;
        private BigDecimal precio;
    }
}