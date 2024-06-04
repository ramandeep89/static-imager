package top.bagadbilla.model.generate;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class RainbowRectanglesGraphics extends BaseGraphics {

    public RainbowRectanglesGraphics(Graphics2D g, int width, int height) {
        super(g, width, height);
    }

    public RainbowRectanglesGraphics(Graphics2D g) {
        super(g);
    }

    public void generate() {
//        fill(Color.WHITE);
        double size = Math.random() * 60 + 10;
        double baseHueOffset = Math.random() * 360;
        int hueMode = (int) Math.floor(Math.random() * 4);
        for (double x = 0; x < width + size; x += size) {
            for (double y = 0; y < height + size; y += size) {
                drawRectangle(size, x, y, baseHueOffset, hueMode);
            }
        }
    }

    private void drawRectangle(double size, double x, double y, double baseHueOffset, int hueMode) {
        double widthModification = Math.random() + 1;
        double heightModification = Math.random() * 3 + 1;
        double width = size * widthModification;
        double height = size * heightModification;
        Rectangle2D rect = new Rectangle2D.Double(-width / 2, -height / 2, width, height);
        AffineTransform at = new AffineTransform();
        at.rotate((Math.random() - 0.5) * 0.1);
        at.translate(x, y);
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
        Color color = HSLColor.toRGB((float) hue, 80F, (float) l, 0.7F);
        g.setColor(color);
        g.fill(at.createTransformedShape(rect));
    }

}