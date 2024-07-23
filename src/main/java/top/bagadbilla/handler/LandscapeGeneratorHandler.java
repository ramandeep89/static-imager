package top.bagadbilla.handler;

import top.bagadbilla.model.generation.graphics.LandscapeGraphics;
import top.bagadbilla.util.ImageByteConverter;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class LandscapeGeneratorHandler {
    public static byte[] getResponse(int width, int height, float hue, boolean random, boolean defaultHue) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        LandscapeGraphics graphics = new LandscapeGraphics(image, width, height);
        if (random) graphics.newColorScheme();
        else if (!defaultHue) graphics.newColorScheme(hue);
        graphics.generate();
        graphics.smoothen();
        return ImageByteConverter.toByteArray(image);
    }
}
