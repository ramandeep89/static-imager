package top.bagadbilla.model.generation.graphics;

import top.bagadbilla.model.generation.BaseGeneration;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class BaseGraphics extends BaseGeneration {

    protected final Graphics2D g;

    public BaseGraphics(BufferedImage image, int width, int height) {
        super(width, height);
        this.g = image.createGraphics();
    }

    protected void fill(Color color) {
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }

    public void smoothen() {
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }
}
