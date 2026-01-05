package com.linktic.inventario;

import com.linktic.inventario.service.InventarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class InventarioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioService inventarioService;

    private final String API_KEY = "LinkticSecreto2026";

    //Actualizar inventario de producto inexistente
    @Test
    void actualizarInventarioProductoInexistente() throws Exception {
        Long idInexistente = 7L;

        when(inventarioService.validateAndGetProductName(idInexistente))
                .thenThrow(new com.linktic.inventario.exception.ResourceNotFoundException("El producto con ID 7 no existe"));

        String jsonRequest = "{\"data\":{\"type\":\"inventarios\",\"attributes\":{\"cantidad\":50}}}";

        mockMvc.perform(patch("/api/v1/inventarios/" + idInexistente)
                        .header("X-API-KEY", API_KEY)
                        .contentType("application/vnd.api+json")
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El producto con ID 7 no existe"));
    }
}