package com.escandallos.util;

import com.escandallos.model.Category;
import com.escandallos.model.Ingredient;
import com.escandallos.model.Recipe;
import com.escandallos.model.RecipeItem;
import com.escandallos.model.Unit;
import com.escandallos.repository.IngredientRepository;
import com.escandallos.repository.RecipeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Carga el catálogo ampliado de ingredientes y escandallos profesionales de gastronomía y hostelería.
 */
public class DataSeeder {

    public static void seedIfEmpty(IngredientRepository ingredientRepo, RecipeRepository recipeRepo) {
        if (ingredientRepo.findAll().size() < 20) {
            seedFullCatalog(ingredientRepo, recipeRepo);
        }
    }

    public static void seedFullCatalog(IngredientRepository ingredientRepo, RecipeRepository recipeRepo) {
        System.out.println("Sembrando catálogo profesional completo de materias primas y escandallos...");

        Map<String, Ingredient> ingMap = new HashMap<>();
        for (Ingredient existing : ingredientRepo.findAll()) {
            ingMap.put(existing.getName().trim().toLowerCase(), existing);
        }

        Map<String, Recipe> recMap = new HashMap<>();
        for (Recipe existingRec : recipeRepo.findAll()) {
            recMap.put(existingRec.getName().trim().toLowerCase(), existingRec);
        }

        // ==========================================
        // 1. CATÁLOGO MAESTRO DE INGREDIENTES
        // ==========================================

        // Carnes y Aves
        Ingredient solomillo = saveOrUpdateIngredient(ingMap, ingredientRepo, "Solomillo de Ternera", "Carnes", 24.50, Unit.KG, 15.0, "Cárnicas Garay", "Ninguno");
        Ingredient pollo = saveOrUpdateIngredient(ingMap, ingredientRepo, "Pechuga de Pollo de Corral", "Carnes", 7.20, Unit.KG, 10.0, "Avícola Real", "Ninguno");
        Ingredient cordero = saveOrUpdateIngredient(ingMap, ingredientRepo, "Paletilla de Cordero Lechal", "Carnes", 19.80, Unit.KG, 22.0, "Carnes Sierra", "Ninguno");
        Ingredient presa = saveOrUpdateIngredient(ingMap, ingredientRepo, "Presa Ibérica de Bellota", "Carnes", 28.00, Unit.KG, 12.0, "Ibéricos Guijuelo", "Ninguno");
        Ingredient jamon = saveOrUpdateIngredient(ingMap, ingredientRepo, "Jamón Ibérico de Cebo de Campo", "Carnes", 42.00, Unit.KG, 25.0, "Ibéricos Guijuelo", "Ninguno");
        Ingredient panceta = saveOrUpdateIngredient(ingMap, ingredientRepo, "Panceta de Cerdo Curada", "Carnes", 8.50, Unit.KG, 8.0, "Cárnicas Garay", "Ninguno");

        // Pescados y Mariscos
        Ingredient gamba = saveOrUpdateIngredient(ingMap, ingredientRepo, "Gamba Roja de Dénia", "Pescados y Mariscos", 45.00, Unit.KG, 35.0, "Pescados Paca", "Crustáceos");
        Ingredient lubina = saveOrUpdateIngredient(ingMap, ingredientRepo, "Lubina Salvaje Fresca", "Pescados y Mariscos", 18.50, Unit.KG, 38.0, "Lonja Central", "Pescado");
        Ingredient pulpo = saveOrUpdateIngredient(ingMap, ingredientRepo, "Pulpo Cocido Gallego", "Pescados y Mariscos", 26.00, Unit.KG, 5.0, "Mariscos Rías Baixas", "Moluscos");
        Ingredient salmon = saveOrUpdateIngredient(ingMap, ingredientRepo, "Salmón Noruego Fresco", "Pescados y Mariscos", 14.90, Unit.KG, 25.0, "Pescados Paca", "Pescado");
        Ingredient calamar = saveOrUpdateIngredient(ingMap, ingredientRepo, "Calamar de Potera Nacional", "Pescados y Mariscos", 16.00, Unit.KG, 30.0, "Lonja Central", "Moluscos");
        Ingredient mejillon = saveOrUpdateIngredient(ingMap, ingredientRepo, "Mejillón de Roca", "Pescados y Mariscos", 3.20, Unit.KG, 50.0, "Mariscos Rías Baixas", "Moluscos");

        // Verduras, Hortalizas y Hongos
        Ingredient patata = saveOrUpdateIngredient(ingMap, ingredientRepo, "Patata Monalisa", "Verduras", 1.20, Unit.KG, 18.0, "Huertas Vega", "Ninguno");
        Ingredient cebolla = saveOrUpdateIngredient(ingMap, ingredientRepo, "Cebolla Dulce de Fuentes", "Verduras", 1.40, Unit.KG, 12.0, "Huertas Vega", "Ninguno");
        Ingredient ajo = saveOrUpdateIngredient(ingMap, ingredientRepo, "Ajo Morado de Las Pedroñeras", "Verduras", 4.50, Unit.KG, 15.0, "Ajos del Moral", "Ninguno");
        Ingredient tomate = saveOrUpdateIngredient(ingMap, ingredientRepo, "Tomate Rama Maduro", "Verduras", 2.10, Unit.KG, 8.0, "Huertas Vega", "Ninguno");
        Ingredient pimientoRojo = saveOrUpdateIngredient(ingMap, ingredientRepo, "Pimiento Rojo Morrón", "Verduras", 2.80, Unit.KG, 18.0, "Huertas Vega", "Ninguno");
        Ingredient pimientoVerde = saveOrUpdateIngredient(ingMap, ingredientRepo, "Pimiento Verde Italiano", "Verduras", 2.30, Unit.KG, 15.0, "Huertas Vega", "Ninguno");
        Ingredient zanahoria = saveOrUpdateIngredient(ingMap, ingredientRepo, "Zanahoria de Huerta", "Verduras", 1.10, Unit.KG, 20.0, "Huertas Vega", "Ninguno");
        Ingredient espinaca = saveOrUpdateIngredient(ingMap, ingredientRepo, "Espinaca Fresca Baby", "Verduras", 5.50, Unit.KG, 10.0, "Huertas Vega", "Ninguno");
        Ingredient aguacate = saveOrUpdateIngredient(ingMap, ingredientRepo, "Aguacate Hass Selección", "Verduras", 5.90, Unit.KG, 30.0, "Frutas Tropicales", "Ninguno");
        Ingredient portobello = saveOrUpdateIngredient(ingMap, ingredientRepo, "Champiñón Portobello", "Verduras", 3.80, Unit.KG, 8.0, "Hongos de La Rioja", "Ninguno");
        Ingredient rucula = saveOrUpdateIngredient(ingMap, ingredientRepo, "Rúcula Silvestre", "Verduras", 7.20, Unit.KG, 5.0, "Huertas Vega", "Ninguno");

        // Lácteos, Quesos y Huevos
        Ingredient nata = saveOrUpdateIngredient(ingMap, ingredientRepo, "Nata 35% M.G.", "Lácteos", 3.80, Unit.L, 2.0, "Lácteos Central", "Lactosa");
        Ingredient mascarpone = saveOrUpdateIngredient(ingMap, ingredientRepo, "Queso Mascarpone", "Lácteos", 7.50, Unit.KG, 0.0, "Lácteos Italia", "Lactosa");
        Ingredient parmesano = saveOrUpdateIngredient(ingMap, ingredientRepo, "Queso Parmesano Reggiano DOP", "Lácteos", 22.00, Unit.KG, 5.0, "Lácteos Italia", "Lactosa");
        Ingredient quesoCabra = saveOrUpdateIngredient(ingMap, ingredientRepo, "Queso de Cabra Rulo", "Lácteos", 11.50, Unit.KG, 5.0, "Queserías del Valle", "Lactosa");
        Ingredient mantequilla = saveOrUpdateIngredient(ingMap, ingredientRepo, "Mantequilla Tradicional", "Lácteos", 7.80, Unit.KG, 2.0, "Lácteos Central", "Lactosa");
        Ingredient leche = saveOrUpdateIngredient(ingMap, ingredientRepo, "Leche Entera Fresca", "Lácteos", 1.05, Unit.L, 1.0, "Lácteos Central", "Lactosa");
        Ingredient huevo = saveOrUpdateIngredient(ingMap, ingredientRepo, "Huevo Campero XL", "Huevos", 0.25, Unit.UD, 10.0, "Avícola Real", "Huevo");

        // Aceites y Grasas
        Ingredient aove = saveOrUpdateIngredient(ingMap, ingredientRepo, "Aceite Oliva Virgen Extra", "Aceites", 8.90, Unit.L, 0.0, "Olivas del Sur", "Ninguno");
        Ingredient aceiteGirasol = saveOrUpdateIngredient(ingMap, ingredientRepo, "Aceite de Girasol Alto Oleico", "Aceites", 2.10, Unit.L, 0.0, "OleoSur", "Ninguno");
        Ingredient vinagreJerez = saveOrUpdateIngredient(ingMap, ingredientRepo, "Vinagre de Jerez Reserva", "Condimentos", 6.50, Unit.L, 0.0, "Bodegas Jerez", "Sulfitos");

        // Especias y Hierbas
        Ingredient salFina = saveOrUpdateIngredient(ingMap, ingredientRepo, "Sal Marina Fina", "Especias", 0.65, Unit.KG, 0.0, "Salinas del Mar", "Ninguno");
        Ingredient salMaldon = saveOrUpdateIngredient(ingMap, ingredientRepo, "Sal en Escamas Maldon", "Especias", 12.00, Unit.KG, 0.0, "Salinas del Mar", "Ninguno");
        Ingredient pimienta = saveOrUpdateIngredient(ingMap, ingredientRepo, "Pimienta Negra Grano", "Especias", 18.00, Unit.KG, 0.0, "Especias El Reloj", "Ninguno");
        Ingredient pimenton = saveOrUpdateIngredient(ingMap, ingredientRepo, "Pimentón de la Vera Dulce DOP", "Especias", 14.50, Unit.KG, 0.0, "Pimentón La Vera", "Ninguno");
        Ingredient azafran = saveOrUpdateIngredient(ingMap, ingredientRepo, "Azafrán en Hebra DOP", "Especias", 1200.00, Unit.KG, 0.0, "Azafranes Manchegos", "Ninguno");
        Ingredient romero = saveOrUpdateIngredient(ingMap, ingredientRepo, "Romero Fresco", "Hierbas", 15.00, Unit.KG, 10.0, "Hierbas del Campo", "Ninguno");
        Ingredient hierbabuena = saveOrUpdateIngredient(ingMap, ingredientRepo, "Hierbabuena Fresca", "Hierbas", 14.00, Unit.KG, 10.0, "Hierbas del Campo", "Ninguno");

        // Cereales, Harinas y Panadería
        Ingredient arroz = saveOrUpdateIngredient(ingMap, ingredientRepo, "Arroz Bomba", "Cereales", 3.20, Unit.KG, 0.0, "Grano de Oro", "Ninguno");
        Ingredient harina = saveOrUpdateIngredient(ingMap, ingredientRepo, "Harina de Trigo Repostería", "Cereales", 1.10, Unit.KG, 0.0, "Harinas del Molino", "Gluten");
        Ingredient panBrioche = saveOrUpdateIngredient(ingMap, ingredientRepo, "Pan Brioche Gourmet", "Panadería", 0.60, Unit.UD, 0.0, "Panadería Artesana", "Gluten, Lactosa, Huevo");
        Ingredient bizcochos = saveOrUpdateIngredient(ingMap, ingredientRepo, "Bizcochos de Soletilla", "Repostería", 4.50, Unit.KG, 0.0, "Repostería Tradicional", "Gluten, Huevo");

        // Bebidas y Alcoholes
        Ingredient vinoTinto = saveOrUpdateIngredient(ingMap, ingredientRepo, "Vino Tinto Crianza", "Bebidas", 4.50, Unit.L, 0.0, "Bodegas Rioja", "Sulfitos");
        Ingredient vinoBlanco = saveOrUpdateIngredient(ingMap, ingredientRepo, "Vino Blanco Verdejo", "Bebidas", 3.80, Unit.L, 0.0, "Bodegas Rueda", "Sulfitos");
        Ingredient ronBlanco = saveOrUpdateIngredient(ingMap, ingredientRepo, "Ron Blanco Carta Blanca", "Bebidas", 9.50, Unit.L, 0.0, "Destilerías Caribe", "Ninguno");
        Ingredient cafe = saveOrUpdateIngredient(ingMap, ingredientRepo, "Café Grano Especialidad Arábica", "Bebidas", 18.50, Unit.KG, 5.0, "Tueste Gourmet", "Ninguno");

        // Dulces, Frutas y Chocolates
        Ingredient azucar = saveOrUpdateIngredient(ingMap, ingredientRepo, "Azúcar Blanco", "Dulces", 1.15, Unit.KG, 0.0, "Azucareras Ibéricas", "Ninguno");
        Ingredient chocolate = saveOrUpdateIngredient(ingMap, ingredientRepo, "Chocolate Negro 70%", "Dulces", 9.80, Unit.KG, 0.0, "Chocolates Maestros", "Soja, Frutos de cáscara");
        Ingredient limon = saveOrUpdateIngredient(ingMap, ingredientRepo, "Limón Extra", "Frutas", 1.60, Unit.KG, 25.0, "Frutas del Segura", "Ninguno");
        Ingredient lima = saveOrUpdateIngredient(ingMap, ingredientRepo, "Lima Fresca", "Frutas", 3.20, Unit.KG, 25.0, "Frutas Tropicales", "Ninguno");
        Ingredient fresas = saveOrUpdateIngredient(ingMap, ingredientRepo, "Fresas de Huelva", "Frutas", 4.20, Unit.KG, 15.0, "Huertas del Sur", "Ninguno");

        // ==========================================
        // 2. RECETAS Y ESCANDALLOS DETALLADOS
        // ==========================================

        // --- CATEGORÍA: ENTRANTES ---

        // 1. Croquetas Cremosas de Jamón Ibérico
        Recipe rCroquetas = new Recipe("Croquetas Cremosas de Jamón Ibérico", Category.ENTRANTE, 8, 75.0, 10.0);
        rCroquetas.setRealSellingPriceWithVat(11.50);
        rCroquetas.setNotes("Elaborar una bechamel muy untuosa a fuego lento con leche fresca y mantequilla. Reposo de la masa 24h antes de bolear y freír.");
        rCroquetas.addItem(new RecipeItem(leche, 1.0, Unit.L));
        rCroquetas.addItem(new RecipeItem(mantequilla, 120, Unit.G));
        rCroquetas.addItem(new RecipeItem(harina, 120, Unit.G));
        rCroquetas.addItem(new RecipeItem(jamon, 250, Unit.G));
        rCroquetas.addItem(new RecipeItem(huevo, 3, Unit.UD));
        rCroquetas.addItem(new RecipeItem(aceiteGirasol, 300, Unit.ML));
        rCroquetas.addItem(new RecipeItem(salFina, 5, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rCroquetas);

        // 2. Pulpo a la Gallega con Patatas y Pimentón
        Recipe rPulpo = new Recipe("Pulpo a la Gallega con Patatas y Pimentón", Category.ENTRANTE, 2, 70.0, 10.0);
        rPulpo.setRealSellingPriceWithVat(19.50);
        rPulpo.setNotes("Cocer las patatas cortadas en rodajas de 1 cm. Emplatar el pulpo templado sobre la cama de patata y regar con AOVE y pimentón de la Vera.");
        rPulpo.addItem(new RecipeItem(pulpo, 400, Unit.G));
        rPulpo.addItem(new RecipeItem(patata, 350, Unit.G));
        rPulpo.addItem(new RecipeItem(aove, 60, Unit.ML));
        rPulpo.addItem(new RecipeItem(pimenton, 8, Unit.G));
        rPulpo.addItem(new RecipeItem(salMaldon, 6, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rPulpo);

        // 3. Tartar de Salmón Noruego y Aguacate Hass
        Recipe rTartar = new Recipe("Tartar de Salmón Noruego y Aguacate Hass", Category.ENTRANTE, 2, 72.0, 10.0);
        rTartar.setRealSellingPriceWithVat(15.00);
        rTartar.setNotes("Cortar el salmón a cuchillo en dados de 5 mm. Aliñar al momento con zumo de lima recién exprimido, cebolla dulce picada y AOVE.");
        rTartar.addItem(new RecipeItem(salmon, 300, Unit.G));
        rTartar.addItem(new RecipeItem(aguacate, 180, Unit.G));
        rTartar.addItem(new RecipeItem(lima, 50, Unit.G));
        rTartar.addItem(new RecipeItem(aove, 30, Unit.ML));
        rTartar.addItem(new RecipeItem(cebolla, 40, Unit.G));
        rTartar.addItem(new RecipeItem(salFina, 4, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rTartar);

        // 4. Ensalada Templada de Queso de Cabra y Frutos Rojos
        Recipe rEnsalada = new Recipe("Ensalada Templada de Queso de Cabra y Frutos Rojos", Category.ENTRANTE, 2, 75.0, 10.0);
        rEnsalada.setRealSellingPriceWithVat(12.50);
        rEnsalada.setNotes("Caramelizar ligeramente el rulo de cabra con soplete. Mezclar hojas tiernas de espinaca baby y rúcula con fresas y vinagreta de Jerez.");
        rEnsalada.addItem(new RecipeItem(espinaca, 150, Unit.G));
        rEnsalada.addItem(new RecipeItem(rucula, 80, Unit.G));
        rEnsalada.addItem(new RecipeItem(quesoCabra, 160, Unit.G));
        rEnsalada.addItem(new RecipeItem(fresas, 100, Unit.G));
        rEnsalada.addItem(new RecipeItem(aove, 40, Unit.ML));
        rEnsalada.addItem(new RecipeItem(vinagreJerez, 15, Unit.ML));
        rEnsalada.addItem(new RecipeItem(salFina, 3, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rEnsalada);

        // --- CATEGORÍA: PLATOS PRINCIPALES ---

        // 5. Solomillo de Ternera a la Pimienta
        Recipe rSolomillo = new Recipe("Solomillo de Ternera a la Pimienta", Category.PRINCIPAL, 4, 75.0, 10.0);
        rSolomillo.setRealSellingPriceWithVat(24.00);
        rSolomillo.setNotes("Marcar el solomillo a fuego fuerte. Reducir la nata con el vino tinto y la pimienta negra recién molida.");
        rSolomillo.addItem(new RecipeItem(solomillo, 800, Unit.G));
        rSolomillo.addItem(new RecipeItem(patata, 600, Unit.G));
        rSolomillo.addItem(new RecipeItem(nata, 250, Unit.ML));
        rSolomillo.addItem(new RecipeItem(pimienta, 20, Unit.G));
        rSolomillo.addItem(new RecipeItem(vinoTinto, 100, Unit.ML));
        rSolomillo.addItem(new RecipeItem(aove, 50, Unit.ML));
        saveOrUpdateRecipe(recMap, recipeRepo, rSolomillo);

        // 6. Paella de Gamba Roja de Dénia
        Recipe rPaella = new Recipe("Paella de Gamba Roja de Dénia", Category.PRINCIPAL, 2, 70.0, 10.0);
        rPaella.setRealSellingPriceWithVat(22.50);
        rPaella.setNotes("Sofreír las cabezas de las gambas para extraer el jugo y el coral. Nacarar el arroz bomba con azafrán en hebra y cocinar 18 minutos.");
        rPaella.addItem(new RecipeItem(arroz, 250, Unit.G));
        rPaella.addItem(new RecipeItem(gamba, 400, Unit.G));
        rPaella.addItem(new RecipeItem(aove, 80, Unit.ML));
        rPaella.addItem(new RecipeItem(tomate, 150, Unit.G));
        rPaella.addItem(new RecipeItem(ajo, 20, Unit.G));
        rPaella.addItem(new RecipeItem(azafran, 0.5, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rPaella);

        // 7. Lubina Salvaje al Horno con Panaderas
        Recipe rLubina = new Recipe("Lubina Salvaje al Horno con Panaderas", Category.PRINCIPAL, 2, 72.0, 10.0);
        rLubina.setRealSellingPriceWithVat(21.00);
        rLubina.setNotes("Hornear las patatas panadera y cebolla 20 min previamente. Añadir la lubina abierta a la espalda durante 12 min a 190°C con vino blanco.");
        rLubina.addItem(new RecipeItem(lubina, 700, Unit.G));
        rLubina.addItem(new RecipeItem(patata, 400, Unit.G));
        rLubina.addItem(new RecipeItem(cebolla, 150, Unit.G));
        rLubina.addItem(new RecipeItem(vinoBlanco, 100, Unit.ML));
        rLubina.addItem(new RecipeItem(aove, 60, Unit.ML));
        rLubina.addItem(new RecipeItem(ajo, 15, Unit.G));
        rLubina.addItem(new RecipeItem(salFina, 6, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rLubina);

        // 8. Hamburguesa Gourmet de Ternera en Pan Brioche
        Recipe rBurger = new Recipe("Hamburguesa Gourmet de Ternera en Pan Brioche", Category.PRINCIPAL, 2, 74.0, 10.0);
        rBurger.setRealSellingPriceWithVat(14.50);
        rBurger.setNotes("Carne picada de ternera a la parrilla, queso parmesano fundido, panceta crujiente, rúcula y guarnición de patatas fritas caseras.");
        rBurger.addItem(new RecipeItem(solomillo, 360, Unit.G));
        rBurger.addItem(new RecipeItem(panBrioche, 2, Unit.UD));
        rBurger.addItem(new RecipeItem(panceta, 80, Unit.G));
        rBurger.addItem(new RecipeItem(parmesano, 40, Unit.G));
        rBurger.addItem(new RecipeItem(tomate, 80, Unit.G));
        rBurger.addItem(new RecipeItem(rucula, 30, Unit.G));
        rBurger.addItem(new RecipeItem(patata, 300, Unit.G));
        rBurger.addItem(new RecipeItem(aceiteGirasol, 200, Unit.ML));
        saveOrUpdateRecipe(recMap, recipeRepo, rBurger);

        // 9. Risotto Cremoso de Portobello y Parmesano
        Recipe rRisotto = new Recipe("Risotto Cremoso de Portobello y Parmesano", Category.PRINCIPAL, 2, 76.0, 10.0);
        rRisotto.setRealSellingPriceWithVat(15.50);
        rRisotto.setNotes("Nacarar el arroz bomba con cebolla dulce y vino blanco verdejo. Cocción agregando caldo caliente y mantecar al final con mantequilla y parmesano.");
        rRisotto.addItem(new RecipeItem(arroz, 220, Unit.G));
        rRisotto.addItem(new RecipeItem(portobello, 250, Unit.G));
        rRisotto.addItem(new RecipeItem(parmesano, 60, Unit.G));
        rRisotto.addItem(new RecipeItem(mantequilla, 40, Unit.G));
        rRisotto.addItem(new RecipeItem(cebolla, 80, Unit.G));
        rRisotto.addItem(new RecipeItem(vinoBlanco, 80, Unit.ML));
        rRisotto.addItem(new RecipeItem(aove, 30, Unit.ML));
        saveOrUpdateRecipe(recMap, recipeRepo, rRisotto);

        // 10. Paletilla de Cordero Lechal Asada al Romero
        Recipe rCordero = new Recipe("Paletilla de Cordero Lechal Asada al Romero", Category.PRINCIPAL, 2, 70.0, 10.0);
        rCordero.setRealSellingPriceWithVat(26.50);
        rCordero.setNotes("Asado lento de paletilla con vino blanco, ajo machacado y ramas de romero fresco a 140°C durante 2 horas. Golpe de grill final a 200°C.");
        rCordero.addItem(new RecipeItem(cordero, 1200, Unit.G));
        rCordero.addItem(new RecipeItem(patata, 400, Unit.G));
        rCordero.addItem(new RecipeItem(vinoBlanco, 150, Unit.ML));
        rCordero.addItem(new RecipeItem(romero, 15, Unit.G));
        rCordero.addItem(new RecipeItem(aove, 50, Unit.ML));
        rCordero.addItem(new RecipeItem(ajo, 20, Unit.G));
        rCordero.addItem(new RecipeItem(salFina, 10, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rCordero);

        // --- CATEGORÍA: GUARNICIONES ---

        // 11. Patatas Panadera al Horno con Romero
        Recipe rPanaderas = new Recipe("Patatas Panadera al Horno con Romero", Category.GUARNICION, 4, 78.0, 10.0);
        rPanaderas.setRealSellingPriceWithVat(5.50);
        rPanaderas.setNotes("Patatas cortadas en rodajas finas con cebolla juliana y pimiento verde. Rociar con AOVE, vino blanco y romero fresco. Hornear a 180°C.");
        rPanaderas.addItem(new RecipeItem(patata, 800, Unit.G));
        rPanaderas.addItem(new RecipeItem(cebolla, 200, Unit.G));
        rPanaderas.addItem(new RecipeItem(pimientoVerde, 100, Unit.G));
        rPanaderas.addItem(new RecipeItem(aove, 80, Unit.ML));
        rPanaderas.addItem(new RecipeItem(vinoBlanco, 60, Unit.ML));
        rPanaderas.addItem(new RecipeItem(romero, 10, Unit.G));
        rPanaderas.addItem(new RecipeItem(salFina, 8, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rPanaderas);

        // 12. Salteado de Huerta al Wok con AOVE
        Recipe rSalteado = new Recipe("Salteado de Huerta al Wok con AOVE", Category.GUARNICION, 4, 78.0, 10.0);
        rSalteado.setRealSellingPriceWithVat(6.50);
        rSalteado.setNotes("Salteado en wok a fuego vivo con pimientos tricolor, zanahoria y champiñones Portobello. Textura al dente y crujiente.");
        rSalteado.addItem(new RecipeItem(pimientoRojo, 200, Unit.G));
        rSalteado.addItem(new RecipeItem(pimientoVerde, 150, Unit.G));
        rSalteado.addItem(new RecipeItem(zanahoria, 180, Unit.G));
        rSalteado.addItem(new RecipeItem(portobello, 200, Unit.G));
        rSalteado.addItem(new RecipeItem(cebolla, 150, Unit.G));
        rSalteado.addItem(new RecipeItem(aove, 60, Unit.ML));
        rSalteado.addItem(new RecipeItem(salFina, 6, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rSalteado);

        // --- CATEGORÍA: SALSAS Y BASES ---

        // 13. Salsa Brava Casera Tradicional
        Recipe rBrava = new Recipe("Salsa Brava Casera Tradicional", Category.SALSA, 10, 80.0, 10.0);
        rBrava.setRealSellingPriceWithVat(3.50);
        rBrava.setNotes("Pochar cebolla y ajo en AOVE. Agregar pimentón de la Vera dulce y picante, dorar harina suavemente y desglasar con vino blanco antes de triturar.");
        rBrava.addItem(new RecipeItem(cebolla, 250, Unit.G));
        rBrava.addItem(new RecipeItem(ajo, 25, Unit.G));
        rBrava.addItem(new RecipeItem(pimenton, 25, Unit.G));
        rBrava.addItem(new RecipeItem(harina, 30, Unit.G));
        rBrava.addItem(new RecipeItem(aove, 70, Unit.ML));
        rBrava.addItem(new RecipeItem(vinoBlanco, 100, Unit.ML));
        rBrava.addItem(new RecipeItem(salFina, 8, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rBrava);

        // 14. Salsa Alioli Suave de Ajo Morado
        Recipe rAlioli = new Recipe("Salsa Alioli Suave de Ajo Morado", Category.SALSA, 8, 82.0, 10.0);
        rAlioli.setRealSellingPriceWithVat(3.00);
        rAlioli.setNotes("Emulsión tradicional de huevos camperos, ajo morado y zumo de limón fresco con mezcla equilibrada de girasol y AOVE para no amargar.");
        rAlioli.addItem(new RecipeItem(huevo, 2, Unit.UD));
        rAlioli.addItem(new RecipeItem(aceiteGirasol, 280, Unit.ML));
        rAlioli.addItem(new RecipeItem(aove, 40, Unit.ML));
        rAlioli.addItem(new RecipeItem(ajo, 12, Unit.G));
        rAlioli.addItem(new RecipeItem(limon, 15, Unit.G));
        rAlioli.addItem(new RecipeItem(salFina, 4, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rAlioli);

        // --- CATEGORÍA: POSTRES ---

        // 15. Tiramisú Clásico de Mascarpone y Café
        Recipe rTiramisu = new Recipe("Tiramisú Clásico de Mascarpone y Café", Category.POSTRE, 6, 76.0, 10.0);
        rTiramisu.setRealSellingPriceWithVat(6.50);
        rTiramisu.setNotes("Batir yemas con azúcar, incorporar queso mascarpone italiano y claras a punto de nieve. Calar bizcochos de soletilla en café arábica recién hecho.");
        rTiramisu.addItem(new RecipeItem(mascarpone, 500, Unit.G));
        rTiramisu.addItem(new RecipeItem(huevo, 4, Unit.UD));
        rTiramisu.addItem(new RecipeItem(azucar, 120, Unit.G));
        rTiramisu.addItem(new RecipeItem(bizcochos, 200, Unit.G));
        rTiramisu.addItem(new RecipeItem(cafe, 50, Unit.G));
        rTiramisu.addItem(new RecipeItem(chocolate, 40, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rTiramisu);

        // 16. Coulant de Chocolate Negro Fluido
        Recipe rCoulant = new Recipe("Coulant de Chocolate Negro Fluido", Category.POSTRE, 4, 78.0, 10.0);
        rCoulant.setRealSellingPriceWithVat(6.50);
        rCoulant.setNotes("Fundir chocolate negro 70% con mantequilla. Mezclar con huevos batidos, azúcar y harina tamizada. Hornear 8 min exactos a 200°C.");
        rCoulant.addItem(new RecipeItem(chocolate, 200, Unit.G));
        rCoulant.addItem(new RecipeItem(mantequilla, 150, Unit.G));
        rCoulant.addItem(new RecipeItem(huevo, 4, Unit.UD));
        rCoulant.addItem(new RecipeItem(azucar, 90, Unit.G));
        rCoulant.addItem(new RecipeItem(harina, 60, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rCoulant);

        // 17. Tarta de Queso Fluida al Horno
        Recipe rTartaQueso = new Recipe("Tarta de Queso Fluida al Horno", Category.POSTRE, 8, 77.0, 10.0);
        rTartaQueso.setRealSellingPriceWithVat(6.80);
        rTartaQueso.setNotes("Batir mascarpone con azúcar, huevos enteros, nata 35% y cucharada de harina. Hornear a 210°C durante 28 min para textura cremosa y corazón fluido.");
        rTartaQueso.addItem(new RecipeItem(mascarpone, 600, Unit.G));
        rTartaQueso.addItem(new RecipeItem(nata, 350, Unit.ML));
        rTartaQueso.addItem(new RecipeItem(huevo, 5, Unit.UD));
        rTartaQueso.addItem(new RecipeItem(azucar, 220, Unit.G));
        rTartaQueso.addItem(new RecipeItem(harina, 20, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rTartaQueso);

        // --- CATEGORÍA: BEBIDAS ---

        // 18. Mojito Cubano Artesanal con Hierbabuena
        Recipe rMojito = new Recipe("Mojito Cubano Artesanal con Hierbabuena", Category.BEBIDA, 1, 82.0, 10.0);
        rMojito.setRealSellingPriceWithVat(8.50);
        rMojito.setNotes("Majar hojas de hierbabuena fresca con azúcar y lima. Añadir ron blanco de calidad y completar con hielo frappé.");
        rMojito.addItem(new RecipeItem(ronBlanco, 60, Unit.ML));
        rMojito.addItem(new RecipeItem(lima, 60, Unit.G));
        rMojito.addItem(new RecipeItem(azucar, 20, Unit.G));
        rMojito.addItem(new RecipeItem(hierbabuena, 10, Unit.G));
        saveOrUpdateRecipe(recMap, recipeRepo, rMojito);

        // 19. Sangría Gourmet de Tinto y Cítricos
        Recipe rSangria = new Recipe("Sangría Gourmet de Tinto y Cítricos", Category.BEBIDA, 4, 80.0, 10.0);
        rSangria.setRealSellingPriceWithVat(16.00);
        rSangria.setNotes("Macerar rodajas finas de limón y fresas frescas con vino tinto crianza, azúcar y ron blanco durante 2 horas en frío. Servir con hielo.");
        rSangria.addItem(new RecipeItem(vinoTinto, 750, Unit.ML));
        rSangria.addItem(new RecipeItem(limon, 120, Unit.G));
        rSangria.addItem(new RecipeItem(fresas, 100, Unit.G));
        rSangria.addItem(new RecipeItem(azucar, 50, Unit.G));
        rSangria.addItem(new RecipeItem(ronBlanco, 50, Unit.ML));
        saveOrUpdateRecipe(recMap, recipeRepo, rSangria);

        System.out.println("Sembrado completado con éxito. Total ingredientes: " + ingredientRepo.findAll().size()
                + " | Total escandallos: " + recipeRepo.findAll().size());
    }

    private static Ingredient saveOrUpdateIngredient(
            Map<String, Ingredient> map,
            IngredientRepository repo,
            String name,
            String category,
            double purchasePrice,
            Unit unit,
            double wastePercentage,
            String supplier,
            String allergens) {
        Ingredient ing = map.get(name.trim().toLowerCase());
        if (ing == null) {
            ing = new Ingredient(name, category, purchasePrice, unit, wastePercentage, supplier, allergens);
        } else {
            ing.setCategory(category);
            ing.setPurchasePrice(purchasePrice);
            ing.setUnit(unit);
            ing.setWastePercentage(wastePercentage);
            ing.setSupplier(supplier);
            ing.setAllergens(allergens);
        }
        repo.save(ing);
        map.put(name.trim().toLowerCase(), ing);
        return ing;
    }

    private static void saveOrUpdateRecipe(
            Map<String, Recipe> map,
            RecipeRepository repo,
            Recipe recipe) {
        Recipe existing = map.get(recipe.getName().trim().toLowerCase());
        if (existing != null) {
            recipe.setId(existing.getId());
        }
        repo.save(recipe);
        map.put(recipe.getName().trim().toLowerCase(), recipe);
    }

    public static void main(String[] args) {
        IngredientRepository ingRepo = new IngredientRepository();
        RecipeRepository recRepo = new RecipeRepository();
        seedFullCatalog(ingRepo, recRepo);
    }
}
