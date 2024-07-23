package top.bagadbilla.handler;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RandomWeightedRedirectHandler {
    public static String getResponse() {
        try (InputStream inputStream = SVGFileHandler.class.getResourceAsStream("/redirect.yaml")) {
            Map<String, Integer> redirectMap = new Yaml().load(Objects.requireNonNull(inputStream));
            List<String> keys = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : redirectMap.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    keys.add(entry.getKey());
                }
            }
            int random = (int) Math.floor(Math.random() * keys.size());
            return keys.get(random);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
