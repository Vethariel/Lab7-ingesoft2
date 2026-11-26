[![CI](https://github.com/Vethariel/Lab7-ingesoft2/actions/workflows/ci.yml/badge.svg)](https://github.com/Vethariel/Lab7-ingesoft2/actions/workflows/ci.yml)

# Laboratorio 7: CI/CD con GitHub Actions

Integración continua, puertas de calidad y entrega continua sobre `Prestamos-00`, el proyecto del Laboratorio 6. Ingeniería de Software II (2016702), Universidad Nacional de Colombia, 2026-II.

`Prestamos-00` es el préstamo de equipos del laboratorio: el agregado `Prestamo`, la `CalculadoraMulta`, el caso de uso `SolicitarPrestamo` y el adaptador HTTP de disponibilidad, con sus pruebas unitarias, de dobles, de integración y su cobertura. El historial de commits del Laboratorio 6, incluidos los de cada ciclo de TDD, sigue en este repositorio.

## Qué hay en el repositorio

| Ruta | Qué es |
| --- | --- |
| [pom.xml](pom.xml) y [src/](src/) | El proyecto Maven `Prestamos-00` y sus pruebas |
| [.github/workflows/ci.yml](.github/workflows/ci.yml) | El pipeline de integración continua: ejecuta `mvn -B verify` en cada push a `main` y en cada pull request |
| [docs/informe.pdf](docs/informe.pdf) | El informe, con las evidencias E1 a E20 |
| [latex/](latex/) | La fuente del informe y sus capturas |

## Stack

| Pieza | Herramienta | Versión |
| --- | --- | --- |
| Lenguaje y construcción | Java (`release` 17) y Maven | JDK 17 o superior, Maven 3.9 |
| (1) Framework de pruebas | JUnit 5 (Jupiter) | 5.14.2 |
| (2) Dobles de prueba | Mockito | 5.20.0 |
| (3) Cliente HTTP y JSON | `java.net.http.HttpClient` (del JDK) y Gson | Gson 2.13.2 |
| (4) Servidor HTTP de prueba | MockWebServer (OkHttp) | 5.3.0 |
| (5) Cobertura | JaCoCo | 0.8.14 |

## Estructura del proyecto

| Ruta | Qué es |
| --- | --- |
| `src/main/java/co/edu/unal/prestamos/domain/` | Dominio: reglas de negocio, casos de uso y contratos. Sin red, base de datos ni framework. |
| `src/main/java/co/edu/unal/prestamos/data/` | Adaptadores que cumplen los contratos del dominio con servicios externos. |
| `src/test/java/co/edu/unal/prestamos/domain/` | Pruebas unitarias y con dobles del dominio. |
| `src/test/java/co/edu/unal/prestamos/data/` | Pruebas de integración contra un servidor HTTP local. |

## Comandos

Desde la raíz del repositorio. La primera ejecución descarga las dependencias.

```bash
# Comando único: todas las pruebas, el reporte de cobertura y sus metas.
# Es el mismo que ejecuta el pipeline de integración continua.
mvn -B verify

# Solo las pruebas, sin cobertura
mvn test

# Una sola clase de prueba
mvn test -Dtest=PrestamoTest

# Solo las pruebas de integración de data
mvn test -Dtest='co.edu.unal.prestamos.data.**'
```

`mvn -B verify` también comprueba las metas de cobertura de líneas: al menos 90 % en `domain` y 80 % en `data`. Si alguna no se cumple, la construcción falla; si se cumplen, imprime `All coverage checks have been met.`

El reporte de cobertura queda en `target/site/jacoco/index.html`. Para abrirlo:

```bash
xdg-open target/site/jacoco/index.html
```

## Informe

La fuente está en [latex/main.tex](latex/main.tex) y se compila con LuaLaTeX. El PDF queda en [docs/informe.pdf](docs/informe.pdf):

```bash
cd latex
latexmk
```

Las capturas van en `latex/capturas/`. Mientras falta una, el PDF muestra un recuadro con su nombre. latexmk no detecta que llegó una captura nueva, así que en ese caso hay que forzar la compilación con `latexmk -g`.
