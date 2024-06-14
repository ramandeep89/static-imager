package top.bagadbilla.handler;

import top.bagadbilla.model.svg.ForestSVG;

public class ForestGeneratorHandler {
    public static String getResponse() throws Exception {
        return new ForestSVG().generate();
    }
}
