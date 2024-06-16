package top.bagadbilla.handler;

import top.bagadbilla.model.svg.ForestSVG;

public class ForestGeneratorHandler {
    public static String getResponse(int width, int height) {
        return new ForestSVG(width, height).generate();
    }
}
