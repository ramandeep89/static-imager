package top.bagadbilla.handler;

import top.bagadbilla.model.generation.graphics.LandscapeGraphics;
import top.bagadbilla.util.ImageByteConverter;

import java.io.IOException;

public class LandscapeGeneratorHandler {
    public static byte[] getResponse(int width, int height, float hue, boolean random, boolean defaultHue) throws IOException {
        LandscapeGraphics graphics = new LandscapeGraphics(width, height);
        if (random) graphics.newColorScheme();
        else if (!defaultHue) graphics.newColorScheme(hue);
        return ImageByteConverter.toByteArray(
                graphics.generateImage().smoothen().getImage());
    }
}
