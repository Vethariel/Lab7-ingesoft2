# Prestamos-00

Préstamo de equipos del laboratorio: el agregado `Prestamo`, la `CalculadoraMulta`, el caso de uso `SolicitarPrestamo` y el adaptador HTTP de disponibilidad, con sus pruebas unitarias, de dobles, de integración y su cobertura. Laboratorio 6 de Ingeniería de Software II (2016702), Universidad Nacional de Colombia, 2026-II.

## Stack

| Pieza | Herramienta | Versión |
| --- | --- | --- |
| Lenguaje y construcción | Java (`release` 17) y Maven | JDK 17 o superior, Maven 3.9 |
| (1) Framework de pruebas | JUnit 5 (Jupiter) | 5.14.2 |
| (2) Dobles de prueba | Mockito | 5.20.0 |
| (3) Cliente HTTP y JSON | `java.net.http.HttpClient` (del JDK) y Gson | Gson 2.13.2 |
| (4) Servidor HTTP de prueba | MockWebServer (OkHttp) | 5.3.0 |
| (5) Cobertura | JaCoCo | 0.8.14 |

## Estructura

| Ruta | Qué es |
| --- | --- |
| `src/main/java/co/edu/unal/prestamos/domain/` | Dominio: reglas de negocio, casos de uso y contratos. Sin red, base de datos ni framework. |
| `src/main/java/co/edu/unal/prestamos/data/` | Adaptadores que cumplen los contratos del dominio con servicios externos. |
| `src/test/java/co/edu/unal/prestamos/domain/` | Pruebas unitarias y con dobles del dominio. |
| `src/test/java/co/edu/unal/prestamos/data/` | Pruebas de integración contra un servidor HTTP local. |

## Comandos

Desde esta carpeta (`Prestamos-00/`). La primera ejecución descarga las dependencias.

```bash
# Todas las pruebas
mvn test

# Una sola clase de prueba
mvn test -Dtest=PrestamoTest

# Solo las pruebas de integración de data
mvn test -Dtest='co.edu.unal.prestamos.data.**'

# Pruebas y reporte de cobertura
mvn verify
```

El reporte de cobertura queda en `target/site/jacoco/index.html`. Para abrirlo:

```bash
xdg-open target/site/jacoco/index.html
```
