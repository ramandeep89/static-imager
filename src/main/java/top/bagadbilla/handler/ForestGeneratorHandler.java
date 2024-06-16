package top.bagadbilla.handler;

import top.bagadbilla.model.svg.ForestSVG;

public class ForestGeneratorHandler {
    public static String getResponse() {
        return new ForestSVG().generate();
    }
}
