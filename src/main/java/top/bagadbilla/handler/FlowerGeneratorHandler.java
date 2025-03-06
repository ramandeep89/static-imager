package top.bagadbilla.handler;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import top.bagadbilla.model.generation.fx.FlowersFX;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class FlowerGeneratorHandler {


    public static byte[] getResponse(int width, int height)
            throws IOException, ExecutionException, InterruptedException {
        Callable<WritableImage> callable =
                () -> new FlowersFX(width, height).draw().getCanvas().snapshot(null, null);
        RunnableFuture<WritableImage> future = new FutureTask<>(callable);
        Platform.runLater(future);
        WritableImage snapshot = future.get();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", stream);
        return stream.toByteArray();
    }
}
