package top.bagadbilla.model.generation.fx;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import top.bagadbilla.util.ColorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlowersFX extends BaseFX {
    private static final int MINPETAL = 5;
    private static final int MAXPETAL = 13;
    private static final String[] MODES = {"daisies", "multi", "quad", "roses", "crosshatch", "blueYellow", "greenRed"};
    private static final Map<String, FlowerColor> COLORZ = Map.of("daisies", new FlowerColor(45, 92, 52, 12, 38, 97, 20, false), "roses", new FlowerColor(1, 91, 46, 1, 91, 30, 20, true), "crosshatch", new FlowerColor(1, 91, 46, 0, 0, 0, 80, true), "greenRed", new FlowerColor(102, 72, 27, 348, 89, 42, 20, false), "blueYellow", new FlowerColor(45, 99, 50, 240, 67, 58, 10, false));

    private final List<Point> positions = new ArrayList<>();
    private final Options options;
    private Color pistilC, petalC, pistilC2, petalC2;

    public FlowersFX(int width, int height) {
        super(width, height);

        options = new Options(
                pick(), chance(), chance(), getRandomInt(1, 4), chance(), 4, getRandomFloat(.5f, .9f), chance(.25f));
    }

    @Override
    public FlowersFX draw() {
        ctx.clearRect(0, 0, width, height);
        ctx.applyEffect(getRandomColorAdjust());
        Color bg;
        if (options.mode.equals("daisies")) bg = getRandomColor(50, 100, 0, 50);
        else bg = getRandomColor(50, 100, 0, 100);
        ctx.save();
        ctx.setFill(bg);
        ctx.fillRect(0, 0, width, height);
        ctx.restore();

        drawFlowers();

        return this;
    }

    private void drawFlowers() {
        FlowerColor colorOpt = COLORZ.get(options.mode);
        ThemeColors colors = getThemeColors(colorOpt);
        if (options.invertColors) {
            petalC = colors.pistil;
            pistilC = colors.petal;
        } else {
            petalC = colors.petal;
            pistilC = colors.pistil;
        }

        colors = getThemeColors(colorOpt);
        if (options.invertColors) {
            petalC2 = colors.pistil;
            pistilC2 = colors.petal;
        } else {
            if (colorOpt != null && colorOpt.mix) {
                petalC2 = colors.pistil;
                pistilC2 = colors.petal;
            } else {
                petalC2 = colors.petal;
                pistilC2 = colors.pistil;
            }
        }

        int numFlower = (options.mode.equals("multi")) ? getRandomInt(50, 1000) : getRandomInt(40, 250);
        for (int i = 0; i < numFlower; i++) {
            if (options.mode.equals("multi")) {
                petalC = getRandomColor(50, 100, 0, 100);
                pistilC = getRandomColor(50, 100, 0, 100);
                petalC2 = getRandomColor(50, 100, 0, 100);
                pistilC2 = getRandomColor(50, 100, 0, 100);
            }
            drawFlower();
        }
    }

    private void drawFlower() {
        Flower f = getRandomFlower();
        if (f == null) return;
        positions.add(new Point(f.x, f.y));

        ctx.save();
        ctx.setGlobalAlpha(f.opacity);
        ctx.setLineWidth(f.lw);
        ctx.translate(f.x, f.y);
        ctx.rotate(Math.toDegrees(f.rad));
        ctx.translate(-f.x, -f.y);

        ctx.setFill(chance() ? petalC2 : petalC);
        ctx.setStroke(ctx.getFill());

        //petals
        for (int i = 0; i < f.petals; i++) {
            ctx.translate(f.x, f.y);
            ctx.rotate(f.rotate);
            ctx.translate(-f.x, -f.y);
            ctx.beginPath();
            ctx.moveTo(f.x, f.y - f.shiftOut);

            for (int j = 0; j < options.petalIterations; j++) {
                float sp1 = f.r * f.petalSpread1;
                float len1 = f.r * f.petalLength1;
                float sp2 = f.r * f.petalSpread2;
                float len2 = f.r * f.petalLength2;
                float endX = f.petalBase;
                float endY = f.shiftOut;
                ctx.translate(f.x, f.y);
                if (j % 2 == 0) {/*symmetric rotations for more symmetric petals, rando for some irregularity*/
                    ctx.rotate((j * ((double) 360 / options.petalIterations)));
                } else {
                    ctx.rotate((j * (getRandomInt(1, 4))));
                }

                ctx.translate(-f.x, -f.y);
                ctx.bezierCurveTo(f.x - sp1,/*control point #1 'upper left'*/
                        f.y - len1,
                        f.x + sp2,/*control point #2 'upper right'*/
                        f.y - len2,
                        f.x + endX,/*ending point*/
                        f.y - endY);
                if (options.innerDetails) ctx.quadraticCurveTo(
                        f.x,/*control point*/
                        f.y + f.r,
                        f.x + getRandomInt(0, 5),/*ending point*/
                        f.y);
            }

            ctx.fill();
            ctx.closePath();
            ctx.beginPath();
        }
        //pistil
        for (int j = 0; j < 3; j++) {
            ctx.arc(f.x + getRandomFloat(-2, 2), f.y + getRandomFloat(-2, 2),
                    f.r / f.centerDivisor, f.r / f.centerDivisor,
                    0, 360);
        }

        ctx.setStroke(petalC);
        ctx.setFill(
                !options.mode.equals("crosshatch") && !options.mode.equals("daisies") ?
                        chance() ? pistilC2 : pistilC :
                        pistilC
        );
        ctx.setGlobalAlpha(1);
        ctx.fill();
        ctx.restore();
    }

    private Flower getRandomFlower() {
        int r = options.allowLarge && chance(.05f) ? getRandomInt(50, 150) : getRandomInt(20, 50);
        int x = getRandomInt(0, width);
        int y = getRandomInt(0, height);

        if (options.spaceAround && positions.stream().anyMatch(point ->
                distance(new Point(x, y), new Point(point.x, point.y)) < r * options.padding))
            return null;

        int lw = 4;
        int angle = getRandomInt(0, 360);
        float rad = (float) ((angle * Math.PI) / 180);
        float opacity = getRandomFloat(options.minOpacity, 1);
        int petals = r <= 40 ? getRandomInt(3, 9) : getRandomInt(MINPETAL, MAXPETAL);
        float shiftOut = getRandomFloat(0, (float) r / 3);
        int petalBase = getRandomInt(0, 5);
        float centerDivisor = getRandomFloat(3, 9);
        float petalSpread1 = getRandomFloat(.3f, petals <= 7 ? .7f : .5f);
        float petalLength1 = getRandomFloat(.9f, 2);
        float petalSpread2 = getRandomFloat(petalSpread1 - .2f, petalSpread1 + .2f);
        float petalLength2 = getRandomFloat(.9f, 2);
        int rotate = (int) Math.floor((double) 360 / petals);

        return new Flower(r, x, y, lw, angle, rad, opacity, petals, shiftOut, petalBase, centerDivisor, petalSpread1, petalLength1, petalSpread2, petalLength2, rotate);
    }

    private int distance(Point p1, Point p2) {
        return (int) Math.floor(Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2)));
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

    private String pick() {
        return FlowersFX.MODES[(int) Math.floor(Math.random() * FlowersFX.MODES.length)];
    }

    private Color getRandomColor(float minSat, float maxSat, float minBright, float maxBright) {
        float h = getRandomFloat(0, 360);
        float s = getRandomFloat(minSat, maxSat);
        float l = getRandomFloat(minBright, maxBright);
        return ColorHelper.HSLToFXColor(h, s, l);
    }

    private ThemeColors getThemeColors(FlowerColor opt) {
        Color dPetal = getRandomColor(0, 100, 0, 100);
        Color dPistil = getRandomColor(0, 100, 0, 100);

        if (opt == null) return new ThemeColors(dPetal, dPistil);

        double[] dPetalHSL = ColorHelper.FXColorToHSL(dPetal);
        double[] dPistilHSL = ColorHelper.FXColorToHSL(dPistil);

        float dPetalH = (float) dPetalHSL[0];
        float dPistilH = (float) dPistilHSL[0];
        float dPetalS = (float) dPetalHSL[1];
        float dPistilS = (float) dPistilHSL[1];
        float dPetalL = (float) dPetalHSL[2];
        float dPistilL = (float) dPistilHSL[2];

        if (opt.h != 0) dPetalH = opt.h + getRandomInt(-opt.vary, opt.vary);
        if (opt.s != 0) dPetalS = opt.s + getRandomInt(-opt.vary, opt.vary);
        if (opt.l != 0) dPetalL = opt.l + getRandomInt(-opt.vary, opt.vary);
        if (opt.h2 != 0) dPistilH = opt.h2 + getRandomInt(-opt.vary, opt.vary);
        if (opt.s2 != 0) dPistilS = opt.s2 + getRandomInt(-opt.vary, opt.vary);
        if (opt.l2 != 0) dPistilL = opt.l2 + getRandomInt(-opt.vary, opt.vary);

        return new ThemeColors(
                ColorHelper.HSLToFXColor(dPetalH, Math.max(Math.min(dPetalS, 100), 0), Math.max(Math.min(dPetalL, 100), 0)),
                ColorHelper.HSLToFXColor(dPistilH, Math.max(Math.min(dPistilS, 100), 0), Math.max(Math.min(dPistilL, 100), 0))
        );
    }

    private ColorAdjust getRandomColorAdjust() {
        ColorAdjust colorAdjust = new ColorAdjust();
        if (chance(0.12f)) {
            colorAdjust.setSaturation(-Math.random());
        }
        return colorAdjust;
    }

    private record Point(int x, int y) {
    }

    //    hsl=pistil color hsl2=petal color
    private record FlowerColor(int h, int s, int l, int h2, int s2, int l2, int vary, boolean mix) {
    }

    private record Options(String mode, boolean spaceAround, boolean allowLarge, int padding, boolean innerDetails,
                           int petalIterations, float minOpacity, boolean invertColors) {
    }

    private record ThemeColors(
            Color pistil,
            Color petal
    ) {
    }

    private record Flower(
            int r,
            int x,
            int y,
            int lw,
            int angle,
            float rad,
            float opacity,
            int petals,
            float shiftOut,
            int petalBase,
            float centerDivisor,
            float petalSpread1,
            float petalLength1,
            float petalSpread2,
            float petalLength2,
            int rotate
    ) {
    }
}
