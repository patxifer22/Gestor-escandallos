# 🍳 Gestor de Escandallos y Control de Costes (JavaFX 21)

Aplicación de escritorio moderna desarrollada en **Java 21 LTS** con **JavaFX 21** y **Maven**, diseñada para el control integral de costes de materia prima, gestión de mermas/desperdicios, cálculo de escandallos por ración y optimización de márgenes financieros en restauración y hostelería.

---

## 📖 ¿Qué es un Escandallo y cuál es el objetivo?

En el sector gastronómico, un **escandallo** es la ficha técnica que desglosa detalladamente todos los ingredientes que componen un plato o elaboración, cuantificando su coste exacto considerando el desperdicio (**merma**) generado durante su preparación o limpieza. 

El objetivo de esta herramienta es:
1. Conocer el **coste real neto** de cada ingrediente tras el aprovechamiento útil.
2. Calcular el **coste de materia prima por ración** (*Food Cost* unitario).
3. Sugerir un **Precio de Venta al Público (PVP)** recomendado con IVA según el **margen de beneficio objetivo** del restaurante.
4. Evaluar el **margen real (%)** y beneficio neto por ración frente al PVP real fijado en la carta del restaurante.

---

## 🏛️ Arquitectura del Sistema

El proyecto sigue una **arquitectura por capas desacoplada** (*Modelo - Repositorio - Servicio - Vista*), garantizando código limpio, mantenible y fácilmente testeable:

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
│                   CAPA DE PERSISTENCIA (JSON)                    │
│      IngredientRepository        │       RecipeRepository        │
│                JsonStorageUtil   │       DataSeeder              │
└─────────────────────────────────┬────────────────────────────────┘
                                  │
┌─────────────────────────────────▼────────────────────────────────┐
│                       CAPA DE DOMINIO                            │
│        Ingredient  │  Recipe  │  RecipeItem  │  Unit  │ Category │
└──────────────────────────────────────────────────────────────────┘
```

---

## 📁 Estructura Detallada de Carpetas y Archivos

```
Gestor-escandallos/
├── pom.xml                                 # Configuración Maven (JavaFX 21, Jackson, JUnit 5)
├── README.md                               # Documentación integral del proyecto
├── .gitignore                              # Archivos y carpetas excluidos de Git
├── data/                                   # Directorio de persistencia local (archivos JSON)
│   ├── ingredients.json                    # Base de datos local de ingredientes
│   └── recipes.json                        # Base de datos local de escandallos
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
    │   │       ├── repository/             # Acceso y persistencia de datos
    │   │       │   ├── IngredientRepository.java  # CRUD de ingredientes con búsqueda
    │   │       │   └── RecipeRepository.java      # CRUD de escandallos con filtros
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
    │   │       └── util/                   # Utilidades de persistencia y formato
    │   │           ├── CurrencyFormatter.java    # Formateo a moneda (€) y porcentajes (%)
    │   │           ├── DataSeeder.java           # Carga de datos de demostración gastronómicos
    │   │           └── JsonStorageUtil.java      # Serialización y deserialización JSON con Jackson
    │   └── resources/
    │       └── css/
    │           └── styles.css              # Hoja de estilos moderna (Dark Theme) para JavaFX
    └── test/
        └── java/
            └── com/escandallos/service/
                └── CostCalculatorServiceTest.java # Pruebas unitarias automatizadas JUnit 5
```

---

## 🧩 Descripción Detallada de Componentes

### 1. Capa de Dominio (`com.escandallos.model`)
* **`Unit`**: Enumeración fuertemente tipada con las unidades soportadas (`KG`, `G`, `L`, `ML`, `UD`), sus nombres descriptivos y símbolos.
* **`Category`**: Enumeración para clasificar las elaboraciones en carta (`ENTRANTE`, `PRINCIPAL`, `POSTRE`, `BEBIDA`, `SALSA`, `GUARNICION`, `OTRO`).
* **`Ingredient`**: Representa el ingrediente básico comprado a proveedores. Registra nombre, categoría, precio bruto de compra por unidad, % de merma/desperdicio, proveedor y alérgenos. Su método `getNetCostPerUnit()` calcula el coste por unidad aprovechable.
* **`RecipeItem`**: Modela una línea de ingrediente dentro de un escandallo. Cuenta con un conversor automático de unidades (`getQuantityInIngredientUnit()`) que permite especificar ingredientes en gramos (`g`) o mililitros (`ml`) aun cuando el ingrediente fue adquirido en kilogramos (`kg`) o litros (`L`).
* **`Recipe`**: Es el escandallo completo. Incluye la lista de `RecipeItem`, raciones producidas (*yield*), margen objetivo deseado (%), porcentaje de IVA (ej. 10%) y PVP real en carta. Contiene métodos para calcular el coste total de elaboración, coste por ración, PVP sugerido sin/con IVA, beneficio unitario y margen real.

### 2. Capa de Persistencia (`com.escandallos.repository` y `util`)
* **`JsonStorageUtil`**: Utilidad que gestiona la lectura y escritura en disco en formato JSON mediante Jackson (`ObjectMapper` con soporte para JavaTime), creando los directorios automáticamente y formateando el JSON con indentación legible.
* **`IngredientRepository`**: Mantiene en memoria y persiste en `data/ingredients.json` los ingredientes. Permite buscar por texto filtrando en tiempo real.
* **`RecipeRepository`**: Mantiene y persiste en `data/recipes.json` los escandallos. Permite filtrado por nombre y por categoría de plato.
* **`DataSeeder`**: Si la aplicación se abre por primera vez y no existen datos, siembra automáticamente ingredientes de alta gastronomía (solomillo de ternera, gamba roja de Dénia, arroz bomba, AOVE, etc.) y dos recetas completas para disponer de una demo inmediata.
* **`CurrencyFormatter`**: Estandariza la presentación de números a moneda europea (`12,50 €`) y porcentajes (`75,00 %`).

### 3. Capa de Negocio (`com.escandallos.service`)
* **`CostCalculatorService`**: Expone métodos de agregación y análisis de negocio para la dirección del restaurante:
  * Conteo total de ingredientes y recetas activas.
  * **Food Cost Promedio ponderado (%)** de toda la carta del establecimiento.
  * Detección del plato con **mayor margen de rentabilidad** y con **menor margen**.

### 4. Capa de Interfaz de Usuario (`com.escandallos.ui`) en JavaFX 21
* **`App`**: Clase principal que extiende `javafx.application.Application`, asegura la siembra de datos si es necesario, carga `styles.css` y lanza la ventana principal.
* **`AppLauncher`**: Punto de entrada auxiliar para ejecutar el `.jar` empaquetado sin restricciones de línea de comandos de módulos de JavaFX.
* **`MainView`**: Contenedor principal de la aplicación (`BorderPane`) con cabecera de marca, botón de sincronización de datos, pestañas `TabPane` y barra de estado inferior.
* **`MetricCard`**: Tarjeta visual reutilizable para los KPIs con barra de color identificativa, título, valor grande y subtítulo.
* **`DashboardView`**: Panel ejecutivo con las 4 tarjetas KPI principales y una tabla general de escandallos con indicadores visuales de rentabilidad (`✅ Excelente ≥70%`, `⚠️ Aceptable`, `❌ Margen Bajo`).
* **`IngredientView`**: Tabla con la base de datos de ingredientes, barra de búsqueda en tiempo real y diálogo modal (`Dialog<Ingredient>`) con **cálculo reactivo en vivo** del coste neto según la merma antes de guardar.
* **`RecipeView`**: Creador y visor de fichas técnicas y escandallos. Cuenta con un diálogo interactivo donde se añaden ingredientes, cantidades y unidades, recalculando al instante el coste de materia prima, coste por ración, PVP sugerido y margen neto real.
* **`styles.css`**: Hoja de estilos moderna con temática oscura (paleta basada en colores Catppuccin / Dark Slate), esquinas redondeadas, tablas limpias y transiciones en botones.

### 5. Pruebas Unitarias (`src/test/...`)
* **`CostCalculatorServiceTest`**: Conjunto de pruebas unitarias con JUnit 5 que comprueban rigurosamente:
  1. El cálculo exacto del coste neto con diferentes porcentajes de merma (0% y 20%).
  2. La correcta suma de costes, conversión de gramos a kilogramos, cálculo por ración y cálculo del PVP sugerido con IVA del 10%.

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

1. **Compilar el proyecto y ejecutar las pruebas unitarias**:
   ```powershell
   mvn clean test
   ```

2. **Ejecutar la aplicación de escritorio (JavaFX)**:
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
