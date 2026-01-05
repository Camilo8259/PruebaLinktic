package com.linktic.inventario.dto.external;

import lombok.Data;

@Data
public class ProductExternalDTO {
    private DataWrapper data;

    @Data
    public static class DataWrapper {
        private String id;
        private Attributes attributes;
    }

    @Data
    public static class Attributes {
        private String nombre;
        private Double precio;
    }
}