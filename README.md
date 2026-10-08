# 🍳 Gestor de Escandallos y Control de Costes (JavaFX 21 + SQLite)

Aplicación de escritorio moderna desarrollada en **Java 21 LTS** con **JavaFX 21**, **Maven** y base de datos relacional **SQLite** mediante **JDBC nativo**, diseñada para el control integral de costes de materia prima, gestión de mermas/desperdicios, cálculo de escandallos por ración y optimización de márgenes financieros en restauración y hostelería.

---

## 📖 ¿Qué es un Escandallo y cuál es el objetivo?

En el sector gastronómico, un **escandallo** es la ficha técnica que desglosa detalladamente todos los ingredientes que componen un plato o elaboración, cuantificando su coste exacto considerando el desperdicio (**merma**) generado durante su preparación o limpieza. 

El objetivo de esta herramienta es:
1. Conocer el **coste real neto** de cada ingrediente tras el aprovechamiento útil.
2. Calcular el **coste de materia prima por ración** (*Food Cost* unitario).
3. Sugerir un **Precio de Venta al Público (PVP)** recomendado con IVA según el **margen de beneficio objetivo** del restaurante.
4. Evaluar el **margen real (%)** y beneficio neto por ración frente al PVP real fijado en la carta del restaurante.
5. Gestionar la información en una **base de datos relacional robusta (SQLite)** con integridad referencial y transacciones ACID.

---

## 🏛️ Arquitectura del Sistema

El proyecto sigue una **arquitectura por capas desacoplada** (*Modelo - Repositorio/DAO - Servicio - Vista*):

```
┌──────────────────────────────────────────────────────────────────┐
│                      CAPA DE VISTA (JavaFX 21)                   │
│   MainView  │  DashboardView  │  IngredientView  │  RecipeView   │
│                 MetricCard    │  styles.css                      │
└─────────────────────────────────┬────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────┐
│                     CAPA DE SERVICIO (Negocio)                   │
│                    CostCalculatorService                         │
│       (Cálculo de Food Cost global, márgenes, métricas)          │
└─────────────────────────────────┬────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────┐
│             CAPA DE PERSISTENCIA (SQLite JDBC / DAOs)            │
│      IngredientRepository        │       RecipeRepository        │
│                DatabaseManager   │       DataSeeder              │
└─────────────────────────────────┬────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────┐
│                       CAPA DE DOMINIO                            │
│        Ingredient  │  Recipe  │  RecipeItem  │  Unit  │ Category │
└──────────────────────────────────────────────────────────────────┘
```

---

## 🗄️ Esquema de la Base de Datos Relacional (SQLite)

La persistencia se gestiona en el archivo local embebido `data/escandallos.db`. Las tablas y relaciones están estructuradas de la siguiente manera:

```
┌───────────────────────┐             ┌─────────────────────────┐
│      ingredients      │             │         recipes         │
├───────────────────────┤             ├─────────────────────────┤
│ id (PK)         TEXT  │             │ id (PK)           TEXT  │
│ name            TEXT  │             │ name              TEXT  │
│ category        TEXT  │             │ category          TEXT  │
│ purchase_price  REAL  │             │ portions          INT   │
│ unit            TEXT  │             │ target_margin_pct REAL  │
│ waste_pct       REAL  │             │ vat_percentage    REAL  │
│ supplier        TEXT  │             │ real_selling_pvp  REAL  │
│ allergens       TEXT  │             │ notes             TEXT  │
└───────────▲───────────┘             └────────────▲────────────┘
            │                                      │
            │ 1:N                                  │ 1:N
            │                                      │
            └───────────┐             ┌────────────┘
                        │             │
                  ┌─────┴─────────────┴─────┐
                  │      recipe_items       │
                  ├─────────────────────────┤
                  │ id (PK AUTO)    INTEGER │
                  │ recipe_id (FK)  TEXT    │──> ON DELETE CASCADE
                  │ ingredient_id   TEXT    │──> ON DELETE RESTRICT
                  │ quantity        REAL    │
                  │ unit            TEXT    │
                  └─────────────────────────┘
```

### Definición DDL de las Tablas e Índices
* **`ingredients`**: Catálogo de materias primas con precios brutos y porcentajes de merma.
* **`recipes`**: Datos maestros del plato, rendimiento en raciones, margen objetivo e impuestos.
* **`recipe_items`**: Tabla de relación que asocia qué ingredientes y en qué cantidades exactas componen cada escandallo.
* **Integridad referencial y transacciones**: Clave foránea con `ON DELETE CASCADE` en las recetas (si se elimina un escandallo se eliminan sus líneas asociadas en una transacción atómica).

---

## 📁 Estructura Detallada de Carpetas y Archivos

```
Gestor-escandallos/
├── pom.xml                                 # Configuración Maven (JavaFX 21, SQLite JDBC, JUnit 5)
├── README.md                               # Documentación integral del proyecto
├── .gitignore                              # Archivos y carpetas excluidos de Git
├── data/                                   # Directorio de persistencia local
│   └── escandallos.db                      # Base de datos SQLite embebida
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/escandallos/
    │   │       ├── App.java                # Punto de entrada de la aplicación JavaFX
    │   │       ├── AppLauncher.java        # Lanzador para empaquetado JAR ejecutable
    │   │       ├── model/                  # Entidades y enumeraciones del negocio
    │   │       │   ├── Category.java       # Categorías de platos (Entrante, Principal, Postre...)
    │   │       │   ├── Ingredient.java     # Entidad Ingrediente con cálculo de merma y costes
    │   │       │   ├── Recipe.java         # Entidad Escandallo / Receta con lógica financiera
    │   │       │   ├── RecipeItem.java     # Línea de ingrediente con conversión de unidades
    │   │       │   └── Unit.java           # Unidades de medida (KG, G, L, ML, UD)
    │   │       ├── repository/             # Acceso y persistencia de datos (JDBC)
    │   │       │   ├── DatabaseManager.java       # Conexión, PRAGMA e inicialización DDL de SQLite
    │   │       │   ├── IngredientRepository.java  # DAO de ingredientes con PreparedStatement
    │   │       │   └── RecipeRepository.java      # DAO de escandallos con transacciones relacionales
    │   │       ├── service/                # Lógica de cálculo y análisis financiero
    │   │       │   └── CostCalculatorService.java # Métricas globales y análisis de Food Cost
    │   │       ├── ui/                     # Vistas y componentes visuales en JavaFX
    │   │       │   ├── MainView.java       # Ventana principal (BorderPane, TabPane y StatusBar)
    │   │       │   ├── components/
    │   │       │   │   └── MetricCard.java # Tarjeta visual de KPIs (indicadores clave)
    │   │       │   └── views/
    │   │       │       ├── DashboardView.java    # Panel ejecutivo de rentabilidad
    │   │       │       ├── IngredientView.java   # Gestión de ingredientes con modal reactivo
    │   │       │       └── RecipeView.java       # Creador y ficha técnica interactiva
    │   │       └── util/                   # Utilidades del sistema
    │   │           ├── CurrencyFormatter.java    # Formateo a moneda (€) y porcentajes (%)
    │   │           └── DataSeeder.java           # Sembrador inicial en SQLite si la BD está vacía
    │   └── resources/
    │       └── css/
    │           └── styles.css              # Hoja de estilos moderna (Dark Theme) para JavaFX
    └── test/
        └── java/
            └── com/escandallos/
                ├── repository/
                │   └── DatabaseRepositoryTest.java   # Pruebas de integración con SQLite JDBC
                └── service/
                    └── CostCalculatorServiceTest.java # Pruebas unitarias de cálculo financiero
```

---

## 🧩 Descripción Detallada de Componentes

### 1. Capa de Dominio (`com.escandallos.model`)
* **`Unit`**: Enumeración fuertemente tipada con las unidades soportadas (`KG`, `G`, `L`, `ML`, `UD`), sus nombres descriptivos y símbolos.
* **`Category`**: Enumeración para clasificar las elaboraciones en carta (`ENTRANTE`, `PRINCIPAL`, `POSTRE`, `BEBIDA`, `SALSA`, `GUARNICION`, `OTRO`).
* **`Ingredient`**: Representa el ingrediente básico comprado a proveedores. Registra nombre, categoría, precio bruto de compra por unidad, % de merma/desperdicio, proveedor y alérgenos. Su método `getNetCostPerUnit()` calcula el coste por unidad aprovechable.
* **`RecipeItem`**: Modela una línea de ingrediente dentro de un escandallo. Cuenta con un conversor automático de unidades (`getQuantityInIngredientUnit()`) que permite especificar ingredientes en gramos (`g`) o mililitros (`ml`) aun cuando el ingrediente fue adquirido en kilogramos (`kg`) o litros (`L`).
* **`Recipe`**: Es el escandallo completo. Incluye la lista de `RecipeItem`, raciones producidas (*yield*), margen objetivo deseado (%), porcentaje de IVA (ej. 10%) y PVP real en carta. Contiene métodos para calcular el coste total de elaboración, coste por ración, PVP sugerido sin/con IVA, beneficio unitario y margen real.

### 2. Capa de Persistencia Relacional (`com.escandallos.repository` y `util`)
* **`DatabaseManager`**: Gestiona la conexión centralizada con SQLite (`jdbc:sqlite:data/escandallos.db`), activa las claves foráneas (`PRAGMA foreign_keys = ON;`) y ejecuta el DDL para crear las tablas e índices si no existen.
* **`IngredientRepository`**: Implementa las operaciones CRUD para ingredientes utilizando `PreparedStatement`, consultas optimizadas e inserciones en lote (*batch*).
* **`RecipeRepository`**: Gestiona los escandallos y sus líneas de detalle (`recipe_items`) mediante **transacciones atómicas** (`setAutoCommit(false)` y `commit()`), uniendo mediante `JOIN` relacional la información completa del ingrediente al recuperar las recetas.
* **`DataSeeder`**: Si la base de datos SQLite se encuentra vacía o incompleta al arrancar (o mediante el botón *🌱 Cargar Catálogo Demo* en la barra superior), inserta o sincroniza automáticamente un catálogo gastronómico completo con más de **50 ingredientes** profesionales y **19 escandallos** detallados clasificados en todas las categorías de carta (*Entrantes, Platos Principales, Guarniciones, Salsas y Bases, Postres y Bebidas*).
* **`CurrencyFormatter`**: Estandariza la presentación de números a formato europeo (`12,50 €` y `75,00 %`).

### 3. Capa de Negocio (`com.escandallos.service`)
* **`CostCalculatorService`**: Expone métodos de análisis y agregación para la gestión económica del restaurante:
  * Conteo total de materias primas y escandallos registrados en SQLite.
  * **Food Cost Promedio ponderado (%)** de toda la carta del establecimiento.
  * Identificación del plato con **mayor margen de rentabilidad** y con **menor margen**.

### 4. Capa de Interfaz de Usuario (`com.escandallos.ui`) en JavaFX 21
* **`App`**: Clase principal que extiende `javafx.application.Application`, inicializa la base de datos SQLite, siembra datos iniciales si procede, carga `styles.css` y despliega la ventana principal.
* **`AppLauncher`**: Punto de entrada auxiliar para ejecutar el `.jar` empaquetado sin restricciones de módulos.
* **`MainView`**: Contenedor principal de la aplicación (`BorderPane`) con cabecera de marca, botón de sincronización de datos, pestañas `TabPane` y barra de estado inferior con indicador de conexión a SQLite.
* **`MetricCard`**: Tarjeta visual reutilizable para los KPIs con barra de color identificativa, título, valor grande y subtítulo.
* **`DashboardView`**: Panel ejecutivo con las 4 tarjetas KPI principales y una tabla general de escandallos con indicadores visuales de rentabilidad (`✅ Excelente ≥70%`, `⚠️ Aceptable`, `❌ Margen Bajo`).
* **`IngredientView`**: Tabla conectada a SQLite con buscador en tiempo real y diálogo modal (`Dialog<Ingredient>`) con **cálculo reactivo en vivo** del coste neto según la merma antes de guardar.
* **`RecipeView`**: Creador y visor de fichas técnicas y escandallos. Cuenta con un diálogo interactivo donde se añaden ingredientes, cantidades y unidades, recalculando al instante el coste de materia prima, coste por ración, PVP sugerido y margen neto real.
* **`styles.css`**: Hoja de estilos moderna con temática oscura (paleta basada en colores Catppuccin / Dark Slate), esquinas redondeadas y tablas estilizadas.

### 5. Pruebas Unitarias y de Integración (`src/test/...`)
* **`DatabaseRepositoryTest`**: Prueba las operaciones relacionales contra la base de datos SQLite (inserción de ingredientes, recuperación, persistencia de escandallos con ítems dependientes y borrado en cascada).
* **`CostCalculatorServiceTest`**: Comprueba rigurosamente las fórmulas matemáticas de merma, coste por ración, PVP con IVA y margen neto.

---

## 📐 Fórmulas Financieras Empleadas

El sistema aplica las fórmulas estándar de control de costes y fijación de precios en hostelería:

| Concepto | Fórmula | Explicación |
| :--- | :--- | :--- |
| **Coste Real Neto por Ingrediente** | $$\text{Precio Neto} = \frac{\text{Precio Bruto Compra}}{1 - \frac{\text{\% Merma}}{100}}$$ | Si 1 kg de solomillo cuesta 20 € y tiene un 20% de merma (grasa/huesos), los 0.8 kg útiles cuestan $20 / 0.8 = \mathbf{25{,}00\text{ €/kg}}$. |
| **Coste Total Materia Prima** | $$\text{Coste Total} = \sum_{i=1}^{n} (\text{Cantidad}_i \times \text{Precio Neto}_i)$$ | Suma del coste neto aprovechable de cada uno de los ingredientes usados en la receta. |
| **Coste por Ración (*Food Cost*)** | $$\text{Coste Ración} = \frac{\text{Coste Total}}{\text{Número de Raciones}}$$ | Coste unitario de materia prima necesario para elaborar un plato individual. |
| **PVP Sugerido (sin IVA)** | $$\text{PVP sin IVA} = \frac{\text{Coste Ración}}{1 - \frac{\text{\% Margen Objetivo}}{100}}$$ | Precio que se debe cobrar para alcanzar el margen deseado (ej. margen del 75%: se divide entre $0{,}25$). |
| **PVP Sugerido (con IVA)** | $$\text{PVP con IVA} = \text{PVP sin IVA} \times \left(1 + \frac{\text{\% IVA}}{100}\right)$$ | Precio final de venta al comensal con el impuesto sobre el valor añadido incluido (ej. 10%). |
| **Beneficio Neto por Ración** | $$\text{Beneficio Ración} = \text{PVP sin IVA Carta} - \text{Coste Ración}$$ | Ganancia neta obtenida tras descontar el coste de los alimentos del precio de venta sin IVA. |
| **Margen Real (%) Obtenido** | $$\text{Margen Real (\%)} = \left(\frac{\text{Beneficio Ración}}{\text{PVP sin IVA Carta}}\right) \times 100$$ | Porcentaje de rentabilidad real que arroja el plato según el precio fijado actualmente en carta. |

---

## 🚀 Requisitos y Comandos de Ejecución

### Requisitos Previos
* **Java Development Kit (JDK) 21** o superior instalado.
* **Apache Maven 3.8+** configurado.

### Comandos de Terminal

1. **Compilar el proyecto y ejecutar las pruebas unitarias y de base de datos**:
   ```powershell
   mvn clean test
   ```

2. **Ejecutar la aplicación de escritorio (JavaFX + SQLite)**:
   ```powershell
   mvn javafx:run
   ```

3. **Generar el ejecutable empaquetado (*Fat JAR* con dependencias)**:
   ```powershell
   mvn clean package
   ```
   El archivo generado se ubicará en:
   ```
   target/gestor-escandallos-1.0.0-jar-with-dependencies.jar
   ```

4. **Ejecutar directamente el archivo JAR**:
   ```powershell
   java -jar target/gestor-escandallos-1.0.0-jar-with-dependencies.jar
   ```
