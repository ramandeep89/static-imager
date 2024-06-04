package top.bagadbilla.model.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RainbowRectangleGraphics {

    private final int width;
    private final int height;

    public RainbowRectangleGraphics(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Canvas draw() {
        Canvas canvas = new Canvas(width, height);
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.setFill(Color.WHITE);
        ctx.fill();
        drawRectangles(ctx);
        return canvas;
    }

    private void drawRectangles(GraphicsContext ctx) {
        double size = Math.random() * 60 + 10;
        double baseHueOffset = Math.random() * 360;
        int hueMode = (int) Math.floor(Math.random() * 4);
        for (double x = 0; x < width + size; x += size) {
            for (double y = 0; y < height + size; y += size) {
                drawRectangle(ctx, size, x, y, baseHueOffset, hueMode);
            }
        }
    }

    private void drawRectangle(GraphicsContext ctx, double size, double x, double y, double baseHueOffset, int hueMode) {
        ctx.save();
        ctx.translate(x, y);
        ctx.rotate((Math.random() - 0.5) * 0.1);
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
        Color color = Color.web(String.format("hsla(%f, 80%%, %f%%, 0.7)", hue, l));
        ctx.setFill(color);
        double widthModification = Math.random() + 1;
        double heightModification = Math.random() * 3 + 1;
        double width = size * widthModification;
        double height = size * heightModification;
        ctx.beginPath();
        ctx.rect(-width / 2, -height / 2, width, height);
        ctx.fill();
        ctx.restore();
    }
}
