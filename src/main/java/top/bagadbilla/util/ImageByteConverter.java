package top.bagadbilla.util;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageByteConverter {
    public static byte[] toByteArray(RenderedImage image) throws IOException {
        byte[] imageBytes;
        boolean writeSuccess;
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            writeSuccess = ImageIO.write(image, "png", stream);
            imageBytes = stream.toByteArray();
        }
        if (writeSuccess) return imageBytes;
        else throw new IOException("Unable to write image");
    }
}
