package top.bagadbilla.handler;

import top.bagadbilla.model.generation.graphics.RainbowRectanglesGraphics;
import top.bagadbilla.util.ImageByteConverter;

import java.io.IOException;

/*
Based on https://codepen.io/DonKarlssonSan/pen/BaNeEyp
 */

public class RainbowRectangleGeneratorHandler {

    public static byte[] getResponse(int width, int height) throws IOException {
        RainbowRectanglesGraphics graphics = new RainbowRectanglesGraphics(width, height);
        return ImageByteConverter.toByteArray(
                graphics.generateImage().smoothen().getImage());
    }
}
