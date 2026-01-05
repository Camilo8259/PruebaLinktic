package com.linktic.productos;

import com.linktic.productos.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ProductoIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MockMvc mockMvc;

    private final String API_KEY = "LinkticSecreto2026";

    //Consulta de producto inexistente
    @Test
    void consultaProductoInexistente() throws Exception {
        mockMvc.perform(get("/api/v1/productos/999")
                        .header("X-API-KEY", API_KEY)
                        .accept("application/vnd.api+json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("El producto con ID 999 no existe"))
                .andExpect(jsonPath("$.status").value(404));
    }

    //Conexion sin envio de API Key
    @Test
    void noEnvioDeApiKey() throws Exception {
        mockMvc.perform(get("/api/v1/productos/1")
                        .accept("application/vnd.api+json"))
                .andExpect(status().isForbidden());
        logger.info("No se permiten conexiones sin la cabecera X-API-KEY.");
    }
}