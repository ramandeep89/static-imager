package top.bagadbilla.util;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import java.awt.image.RenderedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

public class JavaFXHelper {
    public static RenderedImage getImageFromCallable(Callable<WritableImage> callable)
            throws ExecutionException, InterruptedException {
        RunnableFuture<WritableImage> future = new FutureTask<>(callable);
        Platform.runLater(future);
        return SwingFXUtils.fromFXImage(future.get(), null);
    }
}
