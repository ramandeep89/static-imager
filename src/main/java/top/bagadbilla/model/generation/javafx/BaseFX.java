package top.bagadbilla.model.generation.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import top.bagadbilla.model.generation.BaseGeneration;

public abstract class BaseFX extends BaseGeneration {
    protected final GraphicsContext context;
    protected BaseFX(Canvas canvas, int width, int height) {
        super(width, height);
        this.context = canvas.getGraphicsContext2D();
    }
}
