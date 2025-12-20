package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

class JsonStorage {

    private static final String FILE = "katalog.json";
    private static final Gson gson = new Gson();

    /* ================= SAVE KATALOG ================= */
    public static void saveKatalog(List<Produk> list) {
        try (Writer writer = new FileWriter(FILE)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= LOAD KATALOG ================= */
    public static List<Produk> loadKatalog() {
        try (Reader reader = new FileReader(FILE)) {
            Type type = new TypeToken<ArrayList<Produk>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /* ================= SAVE HISTORY ================= */
    public static void saveHistory(List<String[]> history) {
        try (Writer writer = new FileWriter("history.json")) {
            gson.toJson(history, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ================= LOAD HISTORY ================= */
    public static List<String[]> loadHistory() {
        try (Reader reader = new FileReader("history.json")) {
            Type type = new TypeToken<ArrayList<String[]>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}