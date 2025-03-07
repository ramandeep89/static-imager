package top.bagadbilla.handler;

import top.bagadbilla.model.generation.fx.FlowersFX;
import top.bagadbilla.util.ImageByteConverter;
import top.bagadbilla.util.JavaFXHelper;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class FlowerGeneratorHandler {

    public static byte[] getResponse(int width, int height)
            throws IOException, ExecutionException, InterruptedException {

        return ImageByteConverter.toByteArray(
                JavaFXHelper.getImageFromCallable(
                        () -> new FlowersFX(width, height).draw().getCanvas().snapshot(null, null)
                )
        );
    }
}
