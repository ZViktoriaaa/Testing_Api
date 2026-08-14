package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = PropertyReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            PROPERTIES.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Cannot read config.properties", e);
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}