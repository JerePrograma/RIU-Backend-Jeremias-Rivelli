# Hotel Search API

## Descripción

Este proyecto resuelve un challenge orientado a registrar búsquedas de disponibilidad de hotel y consultar cuántas veces se repitió una búsqueda equivalente.

La solución está construida con Spring Boot, Java 21, Kafka y Oracle, y sigue una arquitectura hexagonal sin modularización física. La idea principal es separar con claridad el ingreso por HTTP, la publicación y consumo de eventos, la lógica de aplicación y la persistencia.

La operación de registro de búsqueda es asíncrona: el endpoint `POST /search` acepta la solicitud, genera un identificador local y publica un evento en Kafka. Luego un consumidor persiste la búsqueda en base de datos y actualiza un contador agregado por fingerprint.

## Objetivo funcional

La API expone dos endpoints:

- `POST /search`: registra una búsqueda y devuelve un `searchId`
- `GET /count?searchId=...`: devuelve la búsqueda original asociada a ese identificador y la cantidad de veces que se registró una búsqueda equivalente

## Decisiones de diseño

Hay algunas decisiones que conviene dejar explícitas porque forman parte de la solución y no son accidentales.

### Persistencia asíncrona

El endpoint `POST /search` no persiste directamente en base de datos. En su lugar:

1. valida el request
2. construye el objeto de dominio inmutable
3. genera un `searchId` localmente
4. calcula un fingerprint de la búsqueda
5. publica el evento en Kafka
6. devuelve `202 Accepted`

La persistencia real ocurre después, en el consumidor Kafka. Por eso la respuesta del alta no es `200 OK`, sino `202 Accepted`.

### Identidad lógica de la búsqueda

Dos búsquedas se consideran equivalentes si coinciden en:

- `hotelId`
- `checkIn`
- `checkOut`
- `ages`

Importante: el orden de `ages` **sí** importa.  
Por ejemplo, estas dos búsquedas no son equivalentes:

- `[30, 29, 1, 3]`
- `[3, 1, 29, 30]`

Esto no es un detalle de implementación, sino una regla explícita del challenge.

### `searchId` sin acceso a base de datos

El identificador de búsqueda se genera en memoria, sin consultar la base de datos. Esto evita acoplar la operación HTTP a la persistencia y respeta la restricción del enunciado.

### Contador agregado por fingerprint

Para evitar hacer `count(*)` repetidos sobre la tabla transaccional de búsquedas, la solución mantiene una tabla agregada por fingerprint.

Esto mejora el comportamiento del endpoint `GET /count` cuando se consulta muchas veces una misma búsqueda.

### Idempotencia del consumidor

El consumidor Kafka persiste la búsqueda de forma idempotente usando `searchId` como identificador único. Si el mensaje se reprocesa por retry o redelivery, no se vuelve a insertar la búsqueda ni se incrementa el contador más de una vez.

## Tecnologías usadas

- Java 21
- Spring Boot 3
- Spring Web
- Spring Validation
- Spring JDBC
- Spring Kafka
- Oracle Database Free
- Docker / Docker Compose
- springdoc-openapi
- JUnit 5
- JaCoCo

## Arquitectura

La aplicación sigue una arquitectura hexagonal con separación por capas:

- `adapter.in.rest`: controllers y DTOs de entrada
- `application.service`: casos de uso
- `application.port.in`: puertos de entrada
- `application.port.out`: puertos de salida
- `adapter.out.kafka`: productor y consumidor Kafka
- `adapter.out.persistence`: repositorios JDBC
- `domain.model`: modelo de dominio
- `domain.service`: servicios de dominio
- `config`: configuración general
- `config.kafka`: configuración específica de Kafka

La separación no busca “cumplir una forma”, sino dejar clara la dirección de dependencias y evitar mezclar HTTP, Kafka, SQL y lógica de negocio en una misma clase.

## Requisitos para ejecutar

Para levantar el proyecto no hace falta tener Java, Maven, Oracle ni Kafka instalados localmente.

Alcanza con tener:

- Docker
- Docker Compose

## Cómo levantar la aplicación

Desde la raíz del proyecto:

```bash
docker compose up --build
```

Si querés forzar una reconstrucción limpia:

```bash
docker compose down -v
docker compose build --no-cache
docker compose up
```

## Servicios levantados

El `docker-compose` levanta:

- `oracle`: base de datos Oracle Free
- `kafka`: broker Kafka en modo KRaft
- `app`: aplicación Spring Boot

## URLs útiles

Una vez levantado el proyecto:

- Aplicación: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Endpoints

### 1. Registrar una búsqueda

**POST** `/search`

#### Request

```json
{
  "hotelId": "1234aBc",
  "checkIn": "29/12/2023",
  "checkOut": "31/12/2023",
  "ages": [30, 29, 1, 3]
}
```

#### Response

```json
{
  "searchId": "4ab7c9f1-3c3b-4d2c-9f12-8d8d345f0f54"
}
```

#### Estado esperado

- `202 Accepted`

#### Ejemplo con curl

```bash
curl -X POST "http://localhost:8080/search" \
  -H "Content-Type: application/json" \
  -d '{
    "hotelId": "1234aBc",
    "checkIn": "29/12/2023",
    "checkOut": "31/12/2023",
    "ages": [30, 29, 1, 3]
  }'
```

---

### 2. Consultar el conteo de una búsqueda

**GET** `/count?searchId=...`

#### Ejemplo

```bash
curl "http://localhost:8080/count?searchId=4ab7c9f1-3c3b-4d2c-9f12-8d8d345f0f54"
```

#### Response

```json
{
  "searchId": "4ab7c9f1-3c3b-4d2c-9f12-8d8d345f0f54",
  "search": {
    "hotelId": "1234aBc",
    "checkIn": "29/12/2023",
    "checkOut": "31/12/2023",
    "ages": [30, 29, 1, 3]
  },
  "count": 100
}
```

#### Estados esperados

- `200 OK`
- `400 Bad Request` si el parámetro es inválido
- `404 Not Found` si el `searchId` no existe

## Validaciones contempladas

La solución valida tanto a nivel request como a nivel dominio.

Entre las validaciones relevantes:

- `hotelId` obligatorio
- `checkIn` obligatorio
- `checkOut` obligatorio
- `checkIn` debe ser anterior a `checkOut`
- `ages` no puede ser nulo
- `ages` no puede estar vacío
- `ages` no puede contener valores nulos
- `ages` no puede contener valores negativos

## Consideraciones sobre consistencia

Como el registro de búsqueda es asíncrono, puede existir una pequeña ventana entre:

- el momento en que `POST /search` devuelve el `searchId`
- y el momento en que esa búsqueda ya quedó persistida y contada

En otras palabras: si se consulta `GET /count` inmediatamente después del `POST`, puede ocurrir que la persistencia todavía no se haya materializado.

Eso es esperable y está alineado con el diseño del challenge.

## Base de datos

La persistencia usa dos estructuras principales:

### `searches`
Tabla transaccional donde se almacena cada búsqueda individual.

### `search_counts`
Tabla agregada donde se mantiene el contador por fingerprint.

La tabla agregada existe para evitar recalcular conteos sobre la tabla completa cada vez que se consulta el endpoint `GET /count`.

## Kafka

Kafka se usa para desacoplar la operación HTTP de la persistencia.

### Productor
Publica un `SearchMessage` con:

- `searchId`
- `fingerprint`
- `hotelId`
- `checkIn`
- `checkOut`
- `ages`

### Consumidor
Consume el mensaje, persiste la búsqueda y actualiza el contador agregado.

El offset se confirma manualmente solo después de una persistencia exitosa.

## Generación de cobertura

El proyecto incluye JaCoCo con umbral mínimo de cobertura del 80%.

### Ejecutar tests y verificación

Si tenés Maven instalado localmente:

```bash
mvn clean verify
```

Si no querés depender de Maven local, también podés generar la cobertura usando Docker:

```bash
docker run --rm -v "${PWD}:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-21 mvn clean verify
```

### Reporte generado

Una vez ejecutado `verify`, el reporte queda en:

```text
target/site/jacoco/index.html
```

## Cómo detener el entorno

```bash
docker compose down
```

Si además querés eliminar volúmenes:

```bash
docker compose down -v
```

## Posibles mejoras

El objetivo del proyecto fue resolver el challenge con foco en claridad, validación, desacople y eficiencia del conteo. Aun así, hay mejoras posibles si esto evolucionara a un escenario más cercano a producción:

- agregar observabilidad con métricas y tracing
- incorporar manejo más fino de errores transitorios de Kafka
- agregar healthchecks más detallados
- versionar esquema con Flyway o Liquibase
- endurecer configuración de seguridad y secretos
- ampliar la estrategia de testing con pruebas de integración más completas

## Resumen final

La solución busca equilibrar tres cosas:

1. cumplir con el enunciado
2. mantener una arquitectura clara
3. evitar decisiones que funcionen en demo pero escalen mal bajo repetición intensiva de consultas

La parte más importante de ese equilibrio está en dos decisiones:

- persistencia asíncrona con Kafka
- contador agregado por fingerprint
