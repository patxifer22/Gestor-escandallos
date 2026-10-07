package com.escandallos.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidad de persistencia en disco usando formato JSON.
 */
public class JsonStorageUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static <T> void saveList(String filePath, List<T> list) {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            objectMapper.writeValue(file, list);
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo JSON: " + filePath + " - " + e.getMessage());
        }
    }

    public static <T> List<T> loadList(String filePath, Class<T> clazz) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo JSON: " + filePath + " - " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
