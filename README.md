# 🍳 Gestor de Escandallos y Control de Costes (JavaFX 21)

Aplicación de escritorio moderna en **Java 21** con **JavaFX 21** y **Maven** diseñada para la gestión de ingredientes, mermas de cocina, cálculo de costes por ración y control de márgenes financieros en restauración.

---

## 🌟 Características Principales

1. **Base de Datos de Ingredientes y Mermas**:
   - Registro de ingredientes con precio de compra (€/kg, €/L, €/ud).
   - Aplicación del **% de merma/desperdicio** con cálculo automático del **coste real neto aprovechable**.
   - Control de alérgenos y proveedores.

2. **Creador de Escandallos y Fichas Técnicas**:
   - Cálculo automático de coste total de ingrediente según cantidad y unidades (conversión automática `g` -> `kg`, `ml` -> `L`).
   - Cálculo del **coste de materia prima por ración**.
   - Sugerencia de **PVP sin IVA** y **PVP con IVA** en función del **margen objetivo (%)**.
   - Cálculo del **margen real (%)** y **beneficio neto por ración** según el PVP real fijado en carta.

3. **Dashboard Ejecutivo & Rentabilidad**:
   - Indicadores KPI (Total ingredientes, total escandallos, Food Cost promedio, plato más rentable).
   - Indicadores visuales de rentabilidad por color (verde: $\ge 70\%$, naranja: $\ge 50\%$, rojo: $< 50\%$).

4. **Interfaz de Usuario Moderna en JavaFX**:
   - Estilizado profesional con hojas de estilo CSS (`styles.css`).
   - Componentes interactivos `TableView`, `TabPane`, `Dialog`, `MetricCard`.
   - Persistencia de datos automática en archivos locales **JSON** (`data/ingredients.json`, `data/recipes.json`).

---

## 🏗️ Estructura del Proyecto

```
Gestor-escandallos/
├── pom.xml                                 # Configuración Maven (JavaFX 21, Jackson, JUnit 5)
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/escandallos/
│   │   │       ├── App.java                # Punto de entrada JavaFX Application
│   │   │       ├── AppLauncher.java        # Launcher para ejecutables JAR sin módulo
│   │   │       ├── model/                  # Modelos (Ingredient, Recipe, RecipeItem, Unit, Category)
│   │   │       ├── repository/             # Repositorios JSON (IngredientRepository, RecipeRepository)
│   │   │       ├── service/                # Lógica de cálculo (CostCalculatorService)
│   │   │       ├── ui/                     # Vistas JavaFX (MainView, DashboardView, IngredientView, RecipeView)
│   │   │       └── util/                   # Persistencia, formateadores (€ y %) y datos demo
│   │   └── resources/
│   │       └── css/
│   │           └── styles.css              # Hoja de estilos CSS de JavaFX
│   └── test/
│       └── java/
│           └── com/escandallos/service/   # Pruebas unitarias de cálculo de escandallos
```

---

## 🚀 Cómo Ejecutar el Proyecto JavaFX

### Requisitos
- **JDK 21** o superior.
- **Maven 3.8+**.

### Comandos de Compilación y Ejecución

1. **Compilar y probar el proyecto**:
   ```bash
   mvn clean test
   ```

2. **Ejecutar la Aplicación JavaFX con el plugin de OpenJFX**:
   ```bash
   mvn javafx:run
   ```

3. **Generar Ejecutable JAR con dependencias**:
   ```bash
   mvn clean package
   ```
   El archivo ejecutable estará ubicado en: `target/gestor-escandallos-1.0.0-jar-with-dependencies.jar`.

---

## 📐 Fórmulas Financieras Empleadas

- **Coste Real Neto del Ingrediente**:
  $$\text{Precio Neto} = \frac{\text{Precio Bruto Compra}}{1 - \frac{\text{\% Merma}}{100}}$$

- **Coste por Ración**:
  $$\text{Coste Ración} = \frac{\sum \text{Coste Ingredientes}}{\text{Número de Raciones}}$$

- **PVP Sugerido (sin IVA)**:
  $$\text{PVP sin IVA} = \frac{\text{Coste Ración}}{1 - \frac{\text{\% Margen Objetivo}}{100}}$$

- **PVP Sugerido (con IVA)**:
  $$\text{PVP con IVA} = \text{PVP sin IVA} \times \left(1 + \frac{\text{\% IVA}}{100}\right)$$

- **Margen Real Obtenido**:
  $$\text{Margen Real (\%)} = \left(\frac{\text{PVP sin IVA} - \text{Coste Ración}}{\text{PVP sin IVA}}\right) \times 100$$
