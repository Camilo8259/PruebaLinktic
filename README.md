# PruebaLinktic
Proyecto que contieene los servicios de Productos e Inventario
# Microservicios: Productos e Inventarios

Este proyecto consiste en una arquitectura de microservicios diseñada para gestionar un catálogo de productos y su stock correspondiente, implementando comunicación inter-servicios, seguridad mediante API Key y persistencia de datos.

## 1. Instrucciones de Instalación y Ejecución

### Requisitos Previos
* **Java 21** o superior.
* **Maven 3.8+** (o usar el wrapper `./mvnw` incluido).
* **MySQL** (para entorno de producción/desarrollo).
* **Docker desktop** (para ejecutar el proyecto en docker compose).

### Paso 1: Configuración
Clonar el repositorio

### Paso 2: Lanzar el doker compose
Abrir una terminal en la carpeta del proyecto, ejemplo: `C:\Users\Familia\Documents\PruebaLinktic>`y ejecutar el siguiente comando:
```bash
docker-compose up -d
```

### Prueba Endpoints por medio de Postman
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

* **PATCH** `http://localhost:8001/api/v1/productos/4`
    * *Cuerpo (JSON:API):* `{
  "data": {
    "type": "productos",
    "id": "4",
    "attributes": {
      "nombre": "Imac",
      "precio": 7100000
    }
  }
}`
    * *Descripción:* Actualizar un producto.

* **DELETE** `http://localhost:8001/api/v1/productos/4`
    * *Descripción:* Eliminar un producto. Ejemplo Id producto:4

### Microservicio de Inventario (Puerto 8082)
* **GET** `http://localhost:8002/api/v1/inventarios/3`
    * *Descripción:* Consulta el stock de un producto específico. Ejemplo Id producto:3.
* **PATCH** `http://localhost:8002/api/v1/inventarios/3`
    * *Descripción:* Actualiza la cantidad en stock. Ejemplo Id producto:3.
    * *Cuerpo:* `{
  "data": {
    "type": "inventarios",
    "attributes": {
      "cantidad": 35
    }
  }

### Ejecutar pruebas unitarias y de integracion
Abrir una terminal en la carpeta de cada microservicio, ejemplo: `C:\Users\Familia\Documents\PruebaLinktic\inventario`y ejecutar el siguiente comando:
```bash
./mvnw test
```
---

## 2. Descripción de la Arquitectura

El sistema sigue un patrón de **Microservicios Desacoplados** con las siguientes características:

* **Microservicio de Productos:** Actúa como el catálogo maestro de la aplicación.
* **Microservicio de Inventario:** Gestiona las existencias y depende del catálogo de productos para validar la integridad de los datos.
* **Comunicación:** Se realiza de forma sincrónica mediante `RestTemplate` para validaciones en tiempo real entre servicios.
* **Seguridad:** Implementación de un `OncePerRequestFilter` personalizado que valida una `X-API-KEY` en las cabeceras de las peticiones.



---

## 3. Decisiones Técnicas y Justificaciones

| Decisión | Justificación |
| :--- | :--- |
| **Spring Boot 3.4.1 / 4.0.1** | Uso de las versiones más recientes para aprovechar mejoras en rendimiento. Se implementó `@MockitoBean` para compatibilidad con el nuevo sistema de pruebas. |
| **Base de Datos H2 (Tests)** | Se configuró un perfil de `test` con base de datos en memoria para garantizar que las pruebas sean independientes del entorno local. |
| **Manejo Global de Excepciones** | Implementación de `@RestControllerAdvice` para estandarizar las respuestas de error y evitar fugas de trazas internas. |
| **Estrategia de Pruebas** | Cobertura de caminos ideales y errores controlados. Se priorizaron pruebas unitarias con Mockito y pruebas de integración con `MockMvc`. |
| **SLF4J para Logging** | Uso de logs profesionales de nivel `INFO` y `ERROR` para facilitar la auditoría en lugar de impresiones por consola estándar. |

---

## 3. Diagramas



---


