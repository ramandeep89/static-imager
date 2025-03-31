package top.bagadbilla.model.generation;

import java.awt.*;
import java.util.Vector;

public class ColorScale {
    public static Vector<Color> lerp(Color colorStart, Color colorEnd, float fraction) {
        Vector<Color> vector = new Vector<>();
        vector.add(colorStart);
        int diffR = colorEnd.getRed() - colorStart.getRed();
        int diffG = colorEnd.getGreen() - colorStart.getGreen();
        int diffB = colorEnd.getBlue() - colorStart.getBlue();
        for (int i = 1; i * fraction < 1; i++)
            vector.add(
                    new Color(
                            (int) (colorStart.getRed() + (diffR * i * fraction)),
                            (int) (colorStart.getGreen() + (diffG * i * fraction)),
                            (int) (colorStart.getBlue() + (diffB * i * fraction))
                    ));

        vector.add(colorEnd);
        return vector;
    }
}
