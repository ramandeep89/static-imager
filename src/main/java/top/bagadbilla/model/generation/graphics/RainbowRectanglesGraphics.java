package top.bagadbilla.model.generation.graphics;

import top.bagadbilla.util.ColorHelper;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class RainbowRectanglesGraphics extends BaseGraphics {

    public RainbowRectanglesGraphics(int width, int height) {
        super(width, height);
    }

    public RainbowRectanglesGraphics generateImage() {
        fill(Color.WHITE);
        double size = Math.random() * 60 + 10;
        double baseHueOffset = Math.random() * 360;
        int hueMode = (int) Math.floor(Math.random() * 4);
        for (double x = 0; x < width + size; x += size) {
            for (double y = 0; y < height + size; y += size) {
                drawRectangle(size, x, y, baseHueOffset, hueMode);
            }
        }

        return this;
    }

    private void drawRectangle(double size, double x, double y, double baseHueOffset, int hueMode) {
        g.translate(x, y);
        double widthModification = Math.random() + 1;
        double heightModification = Math.random() * 3 + 1;
        double width = size * widthModification;
        double height = size * heightModification;
        Rectangle2D rect = new Rectangle2D.Double(-width / 2, -height / 2, width, height);
        Shape shape = AffineTransform.getRotateInstance((Math.random() - 0.5) * 0.1)
                .createTransformedShape(rect);
        double randomHueOffset = Math.random() * 20;
        double p = 0;
        if (hueMode == 0) {
            p = (x + y) / 8;
        } else if (hueMode == 1) {
            p = y / 6;
        } else if (hueMode == 2) {
            p = x / 6;
        } else if (hueMode == 3) {
            p = (x - y) / 8;
        }
        double hue = p + randomHueOffset + baseHueOffset;
        double l = Math.random() * 20 + 40;

        Color color = ColorHelper.HSLToAWTColor((float) hue, 80F, (float) l, 0.7F);
        g.setColor(color);
        g.fill(shape);
        g.translate(-x, -y);
    }

}
