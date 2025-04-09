package top.bagadbilla.handler;

import top.bagadbilla.model.generation.svg.GeoPatternsSVG;

import java.util.Map;

public class GeoPatternHandler {
    public static String getResponse(int width, int height, long seed) {
        return new GeoPatternsSVG(width, height, Map.of("seed", String.valueOf(seed))).generateSVG().getSVG();
    }
}
