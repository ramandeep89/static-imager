package top.bagadbilla.handler;

import top.bagadbilla.model.generation.svg.ForestSVG;

public class ForestGeneratorHandler {
    public static String getResponse(int width, int height) {

        ForestSVG svg = new ForestSVG(width, height);
        return svg.generateSVG().getSVG();
    }
}
