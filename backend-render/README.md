# 🚀 Tallas API - Backend Spring Boot

API REST para gestión de tallas desplegada en Render.

## 🌐 URL Producción
https://tu-app.onrender.com

## 📋 Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/tallas` | Listar todas las tallas |
| GET | `/tallas/{id}` | Obtener detalle |
| POST | `/tallas` | Crear nueva talla |
| PUT | `/tallas/{id}` | Actualizar talla |
| DELETE | `/tallas/{id}` | Eliminar talla |

## 🔧 Tecnologías

- Spring Boot 3.2.0
- Java 17
- PostgreSQL (producción)
- H2 (desarrollo)

## 🏃 Desarrollo Local

```bash
mvn spring-boot:run
```

URL local: http://localhost:8083
