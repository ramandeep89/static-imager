package top.bagadbilla.handler;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import top.bagadbilla.model.generation.fx.FlowersFX;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class FlowerGeneratorHandler {
    private Canvas canvas;

    public FlowerGeneratorHandler(int width, int height) throws ExecutionException, InterruptedException {
        Runnable runnable = () -> canvas = new FlowersFX(width, height).draw().getCanvas();
        RunnableFuture<Void> future = new FutureTask<>(runnable, null);
        Platform.runLater(runnable
        );
        future.get();
    }

    public byte[] getResponse() throws IOException {

        WritableImage snapshot = canvas.snapshot(null, null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", stream);
        return stream.toByteArray();
    }
}
