package top.bagadbilla.handler;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import top.bagadbilla.model.generation.javafx.FlowersFX;
import top.bagadbilla.util.ImageByteConverter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FlowerGeneratorHandler {
    public static byte[] getResponse(int width, int height, long seed) throws ExecutionException, InterruptedException {
        FutureTask<byte[]> future = new FutureTask<>(() -> {
            Canvas canvas = new Canvas();
            FlowersFX flowersFX = new FlowersFX(canvas, width, height, seed);
            flowersFX.generate();
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setViewport(new Rectangle2D(0, 0, width, height));
            return ImageByteConverter.toByteArray(SwingFXUtils.fromFXImage(canvas.snapshot(snapshotParameters, null), null));
        });
        Platform.runLater(future);
        return future.get();
    }
}
