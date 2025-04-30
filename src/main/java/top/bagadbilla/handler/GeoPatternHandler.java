package top.bagadbilla.handler;

import top.bagadbilla.model.generation.svg.GeoPatternsSVG;

import java.util.HashMap;
import java.util.Map;

public class GeoPatternHandler {
    public static String getResponse(int width, int height, String pattern, long seed) {
        Map<String, String> map = new HashMap<>();
        map.put("seed", String.valueOf(seed));
        if (pattern != null)
            map.put("pattern", pattern);
        return new GeoPatternsSVG(width, height, map).generateSVG().getSVG();
    }
}
