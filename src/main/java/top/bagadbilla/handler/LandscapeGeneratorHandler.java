package top.bagadbilla.handler;

import top.bagadbilla.model.landscape.LandscapeGraphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LandscapeGeneratorHandler {
    public static byte[] getResponse(int width, int height, float hue) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        LandscapeGraphics graphics = new LandscapeGraphics(g, width, height);
        if (hue < 0.0f) graphics.newColorScheme();
        else graphics.newColorScheme(hue);
        graphics.generateLandscape();
        graphics.smoothen();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);
        return stream.toByteArray();
    }
}
