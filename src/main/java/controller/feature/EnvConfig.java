/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.feature;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author TR_NGHIA
 */

public class EnvConfig {
    private static Properties properties = new Properties();
    private static boolean isLoaded = false;

    static {
        loadEnv();
    }

    private static void loadEnv() {
        if (isLoaded) {
            return;
        }

        try (InputStream is = EnvConfig.class.getClassLoader().getResourceAsStream(".env")) {
            if (is != null) {
                properties.load(is);
                isLoaded = true;
                System.out.println("? ?ã load file .env thành công t? classpath!");
            } else {
                System.err.println("? Không tìm th?y file .env trong classpath.");
                System.err.println("Vui lòng t?o th? m?c 'src/main/resources' và ??t file .env vào ?ó.");
            }
        } catch (IOException ex) {
            System.err.println("? L?i khi ??c file .env t? classpath: " + ex.getMessage());
        }
    }

    public static String get(String key) {
        if (!isLoaded) {
            loadEnv();
        }

        String value = properties.getProperty(key);
        if (value == null) {
            System.err.println("?? C?nh báo: Không tìm th?y key '" + key + "' trong file .env");
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        if (!isLoaded) {
            loadEnv();
        }
        return properties.getProperty(key, defaultValue);
    }

    public static String getRequired(String key) {
        String value = get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("Bi?n môi tr??ng b?t bu?c '" + key + "' không ???c tìm th?y ho?c r?ng!");
        }
        return value;
    }

    public static boolean has(String key) {
        if (!isLoaded) {
            loadEnv();
        }
        return properties.containsKey(key);
    }

    public static void showAllKeys() {
        if (!isLoaded) {
            loadEnv();
        }
        System.out.println("=== Danh sách các key trong .env ===");
        if (properties.isEmpty()) {
            System.out.println("  (Không có key nào)");
        } else {
            properties.keySet().forEach(key -> System.out.println("  ? " + key));
        }
    }

    public static void reload() {
        properties.clear();
        isLoaded = false;
        loadEnv();
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("?? C?nh báo: Không th? parse '" + key + "' thành int, s? d?ng giá tr? m?c ??nh: " + defaultValue);
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
