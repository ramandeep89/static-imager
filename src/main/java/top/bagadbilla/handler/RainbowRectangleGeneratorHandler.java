package top.bagadbilla.handler;

import top.bagadbilla.model.generation.graphics.RainbowRectanglesGraphics;
import top.bagadbilla.util.ImageByteConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/*
Based on https://codepen.io/DonKarlssonSan/pen/BaNeEyp
 */

public class RainbowRectangleGeneratorHandler {

    public static byte[] getResponse(int width, int height) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        RainbowRectanglesGraphics graphics = new RainbowRectanglesGraphics(g, width, height);
        graphics.generate();
        graphics.smoothen();
        return ImageByteConverter.toByteArray(image);
    }
}
