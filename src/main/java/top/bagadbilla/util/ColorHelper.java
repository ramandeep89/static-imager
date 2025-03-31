package top.bagadbilla.util;

import dev.mccue.color.HSL;
import dev.mccue.color.sRGB;

import java.awt.*;
import java.util.Vector;

public class ColorHelper {
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

    public static Color HSLToAWTColor(float hue, float saturation, float lightness) {
        return HSLToAWTColor(hue, saturation, lightness, 1.0F);
    }

    public static Color HSLToAWTColor(float hue, float saturation, float lightness, float alpha) {
        sRGB rgb = dev.mccue.color.Color.HSL(hue, saturation / 100F, lightness / 100F).sRGB();
        return new Color((float) rgb.R(), (float) rgb.G(), (float) rgb.B(), alpha);
    }

    public static javafx.scene.paint.Color HSLToFXColor(float hue, float saturation, float lightness) {
        return HSLToFXColor(hue, saturation, lightness, 1.0F);
    }

    public static javafx.scene.paint.Color HSLToFXColor(float hue, float saturation, float lightness, float alpha) {
        sRGB rgb = dev.mccue.color.Color.HSL(hue, saturation / 100D, lightness / 100D).sRGB();
        return javafx.scene.paint.Color.color(rgb.R(), rgb.G(), rgb.B(), alpha);
    }

    public static double[] FXColorToHSL(javafx.scene.paint.Color color) {
        double[] result = new double[3];
        HSL hsl = dev.mccue.color.Color.sRGB(color.getRed(), color.getGreen(), color.getBlue()).HSL();
        result[0] = hsl.H();
        result[1] = hsl.S() * 100;
        result[2] = hsl.L() * 100;
        return result;
    }
}
