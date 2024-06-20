package top.bagadbilla.model.generation.javafx;

import javafx.scene.canvas.Canvas;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;

import java.util.Random;

public class FlowersFX extends BaseFX {
    private static final String[] modes = {"daisies", "multi", "quad", "roses", "crosshatch", "blueYellow", "greenRed"};
    private final int MINPETAL = 5;
    private final int MAXPETAL = 13;
    private final Random random;
    private final String mode;

    protected FlowersFX(Canvas canvas, int width, int height) {
        super(canvas, width, height);
        this.random = new Random();
        mode = modes[random.nextInt(modes.length)];
    }

    private Color randomColor(float minSaturation, float maxSaturation, float minBrightness, float maxBrightness) {
        float h = random.nextFloat(0, 360);
        float s = random.nextFloat(minSaturation, maxSaturation);
        float l = random.nextFloat(minBrightness, maxBrightness);
        return Color.web(String.format("hsl(%f, %f%%, %f%%)", h, s, l));
    }

    private void setRandomEffect() {
        if (Math.random() < 0.12) context.setEffect(new ColorAdjust(0, -Math.random() * .8, 0, 0));
    }

    private void generateRandomBackground() {
        Color background;
        if (mode.equals("daisies"))
            background = randomColor(50f, 100f, 0f, 50f);
        else background = randomColor(50f, 100f, 0f, 100f);
        context.save();
        context.setFill(background);
        context.fillRect(0, 0, width, height);
        context.restore();
    }

    @Override
    public void generate() {
        context.clearRect(0, 0, width, height);
        setRandomEffect();
        generateRandomBackground();
    }
}
