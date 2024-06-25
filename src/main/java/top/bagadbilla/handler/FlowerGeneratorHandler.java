package top.bagadbilla.handler;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import top.bagadbilla.model.generation.javafx.FlowersFX;
import top.bagadbilla.util.ImageByteConverter;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FlowerGeneratorHandler {
    public static byte[] getResponse(int width, int height, long seed) throws ExecutionException, InterruptedException {
        FutureTask<byte[]> future = new FutureTask<>(() -> {
            Canvas canvas = new Canvas();
            FlowersFX flowersFX = new FlowersFX(canvas, width, height, seed);
            flowersFX.generate();
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            parameters.setViewport(new Rectangle2D(0, 0, width, height));
            WritableImage writableImage = canvas.snapshot(parameters, null);
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
            return ImageByteConverter.toByteArray(bufferedImage);
        });
        Platform.runLater(future);
        return future.get();
    }
}
