package top.bagadbilla.model.generation.fx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import top.bagadbilla.model.generation.BaseGeneration;

public class BaseFX extends BaseGeneration {

    protected final Canvas canvas;
    protected final GraphicsContext ctx;

    protected BaseFX(int width, int height) {
        super(width, height);
        canvas = new Canvas(this.width, this.height);
        ctx = canvas.getGraphicsContext2D();
    }

    public Canvas getCanvas() {
        return canvas;
    }

}
