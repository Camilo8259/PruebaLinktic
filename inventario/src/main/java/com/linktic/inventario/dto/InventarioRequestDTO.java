package com.linktic.inventario.dto;

import lombok.Data;

@Data
public class InventarioRequestDTO {
    private DataRequest data;

    @Data
    public static class DataRequest {
        private AttributesRequest attributes;
    }

    @Data
    public static class AttributesRequest {
        private Integer cantidad;
    }
}