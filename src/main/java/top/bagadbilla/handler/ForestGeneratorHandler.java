package top.bagadbilla.handler;

import top.bagadbilla.model.generation.svg.ForestSVG;

import java.io.StringWriter;

public class ForestGeneratorHandler {
    public static String getResponse(int width, int height) {

        StringWriter sw = new StringWriter();
        ForestSVG svg = new ForestSVG(sw, width, height);
        svg.generate();
        return sw.toString();
    }
}
