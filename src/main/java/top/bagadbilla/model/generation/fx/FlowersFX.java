package top.bagadbilla.model.generation.fx;

import javafx.scene.effect.ColorAdjust;
import top.bagadbilla.model.generation.HSLColor;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class FlowersFX extends BaseFX {
    public FlowersFX(int width, int height) {
        super(width, height);

        mode = pick(MODES);
        spaceAround = chance();
        allowLarge = chance();
        padding = getRandomInt(1, 4);
        innerDetails = chance();
        petalIterations = 4;
        minOpacity = getRandomFloat(.5f, .9f);
        inverColors = chance(.25f);
    }

    private record Point(int x, int y) {
    }

    //    hsl=pistil color hsl2=petal color
    private record FlowerColor(
            int h,
            int s,
            int l,
            int h2,
            int s2,
            int l2,
            int vary,
            boolean mix
    ) {
    }

    private static final int MINPETAL = 5;
    private static final int MAXPETAL = 13;
    private static final String[] MODES = {
            "daisies", "multi", "quad", "roses", "crosshatch", "blueYellow", "greenRed"};

    private float pistilC, petalC, pistilC2, petalC2;
    private final String mode;
    private final boolean spaceAround;
    private final boolean allowLarge;
    private final int padding;
    private final boolean innerDetails;
    private final int petalIterations;
    private final float minOpacity;
    private final boolean inverColors;

    private final List<Point> positions = new ArrayList<>();
    private static final Map<String, FlowerColor> COLORZ =
            Map.of(
                    "daisies", new FlowerColor(45, 92, 52, 12, 38, 97, 20, false),
                    "roses", new FlowerColor(1, 91, 46, 1, 91, 30, 20, true),
                    "crosshatch", new FlowerColor(1, 91, 46, 0, 0, 0, 80, true),
                    "greenRed", new FlowerColor(102, 72, 27, 348, 89, 42, 20, false),
                    "blueYellow", new FlowerColor(45, 99, 50, 240, 67, 58, 10, false));

    public void drawFlowers() {
        ctx.clearRect(0, 0, width, height);
        HSLColor bg;
        if (mode.equals("daisies"))
            bg = getRandomColor(50, 100, 0, 50);
        else bg = getRandomColor(50, 100, 0, 100);
        ctx.save();
        ctx.setFill(bg.getFxColor());
        ctx.fillRect(0, 0, width, height);
        ctx.restore();

    }

    private boolean chance(float limit) {
        return Math.random() < limit;
    }

    private boolean chance() {
        return chance(.5f);
    }

    private float getRandomFloat(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }

    private int getRandomInt(float min, float max) {
        int mini = (int) Math.ceil(min);
        int maxi = (int) Math.floor(max);
        return (int) Math.floor(Math.random() * (maxi - mini + 1)) + mini;
    }

    private String pick(String[] arr) {
        return arr[(int) Math.floor(Math.random() * arr.length)];
    }

    private HSLColor getRandomColor(float minSat, float maxSat, float minBright, float maxBright) {
        float h = getRandomFloat(0, 360);
        float s = getRandomFloat(minSat, maxSat);
        float l = getRandomFloat(minBright, maxBright);
        return new HSLColor(h, s, l);
    }

    public ColorAdjust getRandomColorAdjust() {
        ColorAdjust colorAdjust = new ColorAdjust();
        if (chance(0.12f)) {
            colorAdjust.setSaturation(-Math.random());
        }
        return colorAdjust;
    }
}
