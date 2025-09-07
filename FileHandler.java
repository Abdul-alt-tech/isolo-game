package com.isolo.util;

import com.isolo.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String SAVE_DIR = "saves";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static void saveGame(GameState gameState, String filename) {
        try {
            // Create saves directory if it doesn't exist
            Files.createDirectories(Paths.get(SAVE_DIR));
            
            // Write to file
            String json = gson.toJson(gameState);
            Files.write(Paths.get(SAVE_DIR, filename), json.getBytes());
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public static GameState loadGame(String filename) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(SAVE_DIR, filename)));
            return gson.fromJson(json, GameState.class);
        } catch (IOException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    public static List<String> getSavedGames() {
        List<String> savedGames = new ArrayList<>();
        try {
            Files.createDirectories(Paths.get(SAVE_DIR));
            Files.list(Paths.get(SAVE_DIR))
                 .filter(path -> path.toString().endsWith(".json"))
                 .forEach(path -> savedGames.add(path.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Error listing saved games: " + e.getMessage());
        }
        return savedGames;
    }

    public static String generateSaveName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return "isolo_save_" + LocalDateTime.now().format(formatter) + ".json";
    }
}

// Adapter for LocalDateTime serialization
class LocalDateTimeAdapter implements com.google.gson.JsonSerializer<LocalDateTime>, 
                                     com.google.gson.JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public com.google.gson.JsonElement serialize(LocalDateTime src, 
            java.lang.reflect.Type typeOfSrc, 
            com.google.gson.JsonSerializationContext context) {
        return new com.google.gson.JsonPrimitive(formatter.format(src));
    }

    @Override
    public LocalDateTime deserialize(com.google.gson.JsonElement json, 
            java.lang.reflect.Type typeOfT, 
            com.google.gson.JsonDeserializationContext context) throws com.google.gson.JsonParseException {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}
