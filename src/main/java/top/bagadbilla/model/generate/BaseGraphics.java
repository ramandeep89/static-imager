package top.bagadbilla.model.generate;

import java.awt.*;

public abstract class BaseGraphics {

    protected final Graphics2D g;
    protected final int width;
    protected final int height;

    protected BaseGraphics(Graphics2D g, int width, int height) {
        this.g = g;
        this.width = width;
        this.height = height;
    }

    protected BaseGraphics(Graphics2D g) {
        this.g = g;
        this.width = 1920;
        this.height = 1080;
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

    public abstract void generate();
}
