# Tienda de Ropa - Arquitectura de Microservicios

## Descripción

Este proyecto corresponde al desarrollo de una aplicación de una tienda de ropa basada en una arquitectura de microservicios utilizando Spring Boot.

Cada microservicio posee su propia responsabilidad, su propia base de datos y se comunica mediante servicios REST.

---

## herramientas utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Cloud Eureka
- MySQL
- Maven
- Swagger / OpenAPI
- Lombok
- Git
- GitHub
- Postman

---

## apariencia del Microservicios

| Microservicio      | Puerto    |
|--------------------|--------   |
| Eureka Server      | 8761      |
| UsuariosMS         | 8081      |
| ProductosMS        | 8082      |
| InventarioMS       | 8083      |
| PromocionesMS      | 8084      |
| ServicioDePagoMS   | 8085      |
| PedidosMS          | 8086      | 
| ServicioDeEnviosMS | 8087      |
| CarritoMS          | 8088      |
| ReportesMS         | 8089      |  
| AtencionClienteMS  | 8090      | 
---

## Funcionalidades

- Gestión de usuarios
- Gestión de productos
- Gestión de inventario
- Gestión del carrito de compras
- Registro de pedidos
- Gestión de pagos
- Gestión de envíos
- Gestión de promociones
- Atención al cliente
- Generación de reportes
- Documentación de APIs mediante Swagger

---

## Comunicación entre microservicios

El microservicio **PedidosMS** consume información desde:

- UsuariosMS
- ProductosMS
- ServicioDePagoMS
- ServicioDeEnviosMS

mediante `RestTemplate`.

---

##  Base de datos

Cada microservicio utiliza su propia base de datos MySQL, siguiendo el patrón **Database per Service**.

---

## 📑 Documentación API

Cada microservicio posee documentación Swagger.

Ejemplo:

```
http://localhost:8081/swagger-ui/index.html
```

---

## Ejecución del microservicio

1. Iniciar MySQL.
2. Ejecutar Eureka Server.
3. Ejecutar los microservicios.
4. Acceder a Swagger desde el navegador.
5. Probar los endpoints utilizando Postman.

---

## Autor

**Edwars Fuentes**
