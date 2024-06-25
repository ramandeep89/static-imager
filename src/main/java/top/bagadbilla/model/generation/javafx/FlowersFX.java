package top.bagadbilla.model.generation.javafx;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FlowersFX extends BaseFX {
    private static final String[] modes = {"multi", "daisies", "roses", "crosshatch", "blueYellow", "greenRed"};
    private static final Map<String, Colors> colors = Map.of("daisies", new Colors(45, 92, 52, 12, 38, 97, 20, false), "roses", new Colors(1, 91, 46, 1, 91, 30, 20, true), "crosshatch", new Colors(1, 91, 46, 0, 0, 0, 80, true), "greenRed", new Colors(102, 72, 27, 348, 89, 42, 20, false), "blueYellow", new Colors(45, 99, 50, 240, 67, 58, 10, false));
    private static final int MIN_PETAL = 5;
    private static final int MAX_PETAL = 13;

    private final List<Point2D> points = new ArrayList<>();

    private final Random random;
    private final String mode;
    private final boolean spaceAround;
    private final boolean allowLarge;
    private final int padding;
    private final boolean innerDetails;
    private final int petalIterations;
    private final float minOpacity;
    private final boolean invertColors;
    private final ThemeColors[] themeColors = new ThemeColors[2];


    public FlowersFX(Canvas canvas, int width, int height, long seed) {
        super(canvas, width, height);
        this.random = new Random(seed);
        mode = modes[random.nextInt(modes.length)];
        spaceAround = random.nextBoolean();
        allowLarge = random.nextBoolean();
        padding = random.nextInt(1, 4);
        innerDetails = random.nextBoolean();
        petalIterations = 4;
        minOpacity = random.nextFloat(.5f, .9f);
        invertColors = random.nextDouble() < .25;
    }

    private Color randomColor(float minHue, float maxHue, float minSaturation, float maxSaturation, float minLightness, float maxLightness) {
        float h = random.nextFloat(minHue, maxHue);
        float s = random.nextFloat(minSaturation, maxSaturation);
        float l = random.nextFloat(minLightness, maxLightness);
        return Color.web(String.format("hsl(%f, %f%%, %f%%)", h, s, l));
    }

    private void setRandomEffect() {
        if (random.nextDouble() < 0.12) context.setEffect(new ColorAdjust(0, -random.nextDouble() * .8, 0, 0));
    }

    private void generateRandomBackground() {
        Color background;
        if (mode.equals("daisies")) background = randomColor(0f, 360f, 50f, 100f, 0f, 50f);
        else background = randomColor(0f, 360f, 50f, 100f, 0f, 100f);
        context.save();
        context.setFill(background);
        context.fillRect(0, 0, width, height);
        context.restore();
    }

    private ThemeColors generateRandomThemeColors(boolean respectMix) {
        if (mode.equals("multi")) {
            return new ThemeColors(randomColor(0, 360, 50, 100, 0, 100), randomColor(0, 360, 50, 100, 0, 100));
        }
        Colors colors = FlowersFX.colors.get(mode);
        Color petal = randomColor(colors.petalH + random.nextInt(-colors.vary, 0), colors.petalH + random.nextInt(0, colors.vary), colors.petalS + random.nextInt(-colors.vary, 0), colors.petalS + random.nextInt(0, colors.vary), colors.petalL + random.nextInt(-colors.vary, 0), colors.petalL + random.nextInt(0, colors.vary));
        Color pistil = randomColor(colors.pistilH + random.nextInt(-colors.vary, 0), colors.pistilH + random.nextInt(0, colors.vary), colors.pistilS + random.nextInt(-colors.vary, 0), colors.pistilS + random.nextInt(0, colors.vary), colors.pistilL + random.nextInt(-colors.vary, 0), colors.pistilL + random.nextInt(0, colors.vary));
        ThemeColors themeColors;
        if (!respectMix) {
            if (invertColors) themeColors = new ThemeColors(petal, pistil);
            else themeColors = new ThemeColors(pistil, petal);
        } else {
            if (invertColors || FlowersFX.colors.get(mode).mix) themeColors = new ThemeColors(petal, pistil);
            else themeColors = new ThemeColors(pistil, petal);
        }
        return themeColors;
    }

    @Override
    public void generate() {
        context.clearRect(0, 0, width, height);
        setRandomEffect();
        generateRandomBackground();

        themeColors[0] = generateRandomThemeColors(false);
        themeColors[1] = generateRandomThemeColors(true);
        int numFlowers = mode.equals("multi") ? random.nextInt(50, 1000) : random.nextInt(40, 250);
        for (int i = 0; i < numFlowers; i++) {
            if (mode.equals("multi")) {
                themeColors[0] = generateRandomThemeColors(false);
                themeColors[1] = generateRandomThemeColors(false);
            }
            drawFlower();
        }
    }

    private void drawFlower() {
        Flower flower = generateRandomFlower();
        if (flower == null) return;
        points.add(new Point2D(flower.x, flower.y));
        context.save();
        context.setGlobalAlpha(flower.opacity);
        context.setLineWidth(flower.lw);
        context.translate(flower.x, flower.y);
        context.rotate(flower.angle);
        context.translate(-flower.x, -flower.y);

        context.setFill(random.nextDouble() < .5 ? themeColors[1].petal : themeColors[0].petal);
        context.setStroke(context.getFill());

        //petals
        for (int i = 0; i < flower.petals; i++) {
            context.translate(flower.x, flower.y);
            context.rotate(flower.rotate);
            context.translate(-flower.x, -flower.y);
            context.beginPath();
            context.moveTo(flower.x, flower.y - flower.shiftOut);

            for (int j = 0; j < petalIterations; j++) {
                double sp1 = flower.r * flower.petalSpread1;
                double len1 = flower.r * flower.petalLength1;
                double sp2 = flower.r * flower.petalSpread2;
                double len2 = flower.r * flower.petalLength2;
                double endX = flower.petalBase;
                double endY = flower.shiftOut;

                context.translate(flower.x, flower.y);
                if (j % 2 == 0) context.rotate(j * (360d / petalIterations) * Math.PI / 180);
                else context.rotate(j * random.nextInt(1, 4) * Math.PI / 180);
                context.translate(-flower.x, -flower.y);

                context.bezierCurveTo(flower.x - sp1, flower.y - len1, flower.x + sp2, flower.y - len2, flower.x() + endX, flower.y - endY);
                if (innerDetails)
                    context.quadraticCurveTo(flower.x, flower.y + flower.r, flower.x * random.nextInt(5), flower.y);

                context.fill();
                context.closePath();

                context.beginPath();
            }
        }

        //pistil
        for (int i = 0; i < 3; i++) {
            context.arc(flower.x + random.nextDouble(-2, 2), flower.y + random.nextDouble(-2, 2), flower.r / flower.centerDivisor, flower.r / flower.centerDivisor, 0, 360);
        }

        context.setStroke(themeColors[0].petal);
        context.setFill(!mode.equals("crosshatch") && !mode.equals("daisies") ? random.nextDouble() < .5 ? themeColors[0].pistil : themeColors[1].pistil : themeColors[0].pistil);
        context.setGlobalAlpha(1);
        context.fill();
        context.restore();
    }

    @Nullable
    private Flower generateRandomFlower() {
        int r = allowLarge && random.nextDouble() < .5 ? random.nextInt(50, 150) : random.nextInt(20, 50);
        int x = random.nextInt(0, width);
        int y = random.nextInt(0, height);
        if (spaceAround && points.stream().anyMatch(point2D -> Math.hypot(point2D.getX() - x, point2D.getY() - y) < r * padding))
            return null;
        int lw = 4;
        double angle = random.nextDouble(360) * Math.PI / 180;
        double opacity = random.nextDouble(minOpacity, 1);
        int petals = r <= 40 ? random.nextInt(3, 9) : random.nextInt(MIN_PETAL, MAX_PETAL);
        double shiftOut = random.nextDouble(r / 3d);
        int petalBase = random.nextInt(5);
        double centerDivisor = random.nextDouble(3, 9);
        double petalSpread1 = random.nextDouble(.3, petals <= 7 ? .7 : .5);
        double petalLength1 = random.nextDouble(.9, 2);
        double petalSpread2 = random.nextDouble(petalSpread1 - .2, petalSpread1 + .2);
        double petalLength2 = random.nextDouble(.9, 2);
        double rotate = (360d / petals);
        return new Flower(r, x, y, lw, angle, opacity, petals, shiftOut, petalBase, centerDivisor, petalSpread1, petalLength1, petalSpread2, petalLength2, rotate);
    }

    private record Colors(float pistilH, float pistilS, float pistilL, float petalH, float petalS, float petalL,
                          int vary, boolean mix) {
    }

    private record ThemeColors(Color petal, Color pistil) {
    }

    private record Flower(int r, double x, double y, int lw, double angle, double opacity, int petals, double shiftOut,
                          int petalBase, double centerDivisor, double petalSpread1, double petalLength1,
                          double petalSpread2, double petalLength2, double rotate) {

    }
}
