package top.bagadbilla.handler.javafx;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import top.bagadbilla.model.javafx.RainbowRectangleGraphics;
import top.bagadbilla.util.ImageByteConverter;

import java.awt.image.RenderedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/*
Based on https://codepen.io/DonKarlssonSan/pen/BaNeEyp
 */

public class RainbowRectangleGeneratorHandler extends BaseFXHandler {

    public static byte[] getResponse(int width, int height) throws ExecutionException, InterruptedException {
        FutureTask<byte[]> task = new FutureTask<>(() -> {
            WritableImage image = new WritableImage(width, height);
            RainbowRectangleGraphics graphics = new RainbowRectangleGraphics(width, height);
            Canvas canvas = graphics.draw();
            canvas.snapshot(null, image);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
            return ImageByteConverter.toByteArray(renderedImage);
        });
        Platform.runLater(task);
        return task.get();
    }
}
