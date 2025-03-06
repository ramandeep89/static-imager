package top.bagadbilla.model.generation.fx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import top.bagadbilla.model.generation.BaseGeneration;

public abstract class BaseFX extends BaseGeneration {

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

    public abstract BaseFX draw();

}
