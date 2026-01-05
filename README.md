# PruebaLinktic
Proyecto que contieene los servicios de Productos e Inventario
# Sistema de Gestión de Microservicios: Productos e Inventarios

Este proyecto consiste en una arquitectura de microservicios diseñada para gestionar un catálogo de productos y su stock correspondiente, implementando comunicación inter-servicios, seguridad mediante API Key y persistencia de datos.

## 1. Descripción de la Arquitectura

El sistema sigue un patrón de **Microservicios Desacoplados** con las siguientes características:

* [cite_start]**Microservicio de Productos:** Actúa como el catálogo maestro de la aplicación[cite: 7].
* [cite_start]**Microservicio de Inventario:** Gestiona las existencias y depende del catálogo de productos para validar la integridad de los datos.
* [cite_start]**Comunicación:** Se realiza de forma sincrónica mediante `RestTemplate` para validaciones en tiempo real entre servicios.
* [cite_start]**Seguridad:** Implementación de un `OncePerRequestFilter` personalizado que valida una `X-API-KEY` en las cabeceras de las peticiones.



---

## 2. Decisiones Técnicas y Justificaciones

| Decisión | Justificación |
| :--- | :--- |
| **Spring Boot 3.4.1 / 4.0.1** | Uso de las versiones más recientes para aprovechar mejoras en rendimiento. [cite_start]Se implementó `@MockitoBean` para compatibilidad con el nuevo sistema de pruebas[cite: 7]. |
| **Base de Datos H2 (Tests)** | [cite_start]Se configuró un perfil de `test` con base de datos en memoria para garantizar que las pruebas sean independientes del entorno local[cite: 11, 13]. |
| **Manejo Global de Excepciones** | [cite_start]Implementación de `@RestControllerAdvice` para estandarizar las respuestas de error y evitar fugas de trazas internas. |
| **Estrategia de Pruebas** | Cobertura de **Happy Paths** y **Edge Cases**. [cite_start]Se priorizaron pruebas unitarias con Mockito y pruebas de integración con `MockMvc`[cite: 23, 24]. |
| **SLF4J para Logging** | [cite_start]Uso de logs profesionales de nivel `INFO` y `ERROR` para facilitar la auditoría en lugar de impresiones por consola estándar[cite: 22, 23]. |

---

## 3. Endpoints

Todos los endpoints requieren la cabecera: `[{"key":"Accept","value":"application/vnd.api+json","description":"","type":"default","enabled":true},
{"key":"Content-Type","value":"application/vnd.api+json","description":"","type":"default","enabled":true},
{"key":"X-API-KEY","value":"LinkticSecreto2026","description":"","type":"default","enabled":true}]`.

### Microservicio de Productos (Puerto 8081)
* **GET** `http://localhost:8001/api/v1/productos`
    * *Descripción:* Obtiene todos los productos.
* **GET** `http://localhost:8001/api/v1/productos/1`
    * *Descripción:* Obtiene un producto por su ID, ejemplo Id:1.
* **POST** `http://localhost:8001/api/v1/productos`
    * *Cuerpo (JSON:API):* `{
  "data": {
    "type": "productos",
    "attributes": {
      "nombre": "Imac",
      "precio": 7000000
    }
  }
}`
    * *Descripción:* Crear un producto.

### Microservicio de Inventario (Puerto 8082)
* **GET** `/api/v1/inventarios/{productoId}`
    * *Descripción:* Consulta el stock de un producto específico.
* **PATCH** `/api/v1/inventarios/{productoId}`
    * *Descripción:* Actualiza la cantidad en stock.
    * *Cuerpo:* `{"data": {"type": "inventarios", "attributes": {"cantidad": 50}}}`

---

## 4. Instrucciones de Instalación y Ejecución

### Requisitos Previos
* **Java 21** o superior.
* **Maven 3.8+** (o usar el wrapper `./mvnw` incluido).
* **MySQL** (para entorno de producción/desarrollo).

### Paso 1: Configuración
Clonar el repositorio y configurar las credenciales de base de datos en los archivos `application.properties` de cada carpeta.

### Paso 2: Compilación
Ejecuta el siguiente comando en la raíz de cada microservicio:
```bash
./mvnw clean install -DskipTests
