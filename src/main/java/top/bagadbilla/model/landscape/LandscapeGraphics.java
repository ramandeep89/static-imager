package top.bagadbilla.model.landscape;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LandscapeGraphics {

    private final int height;
    private final int width;
    private final Graphics2D g;
    // default initial colors
    private Color[] colors = new Color[]{
            new Color(0xAFFFFF),
            new Color(0x74DBEF),
            new Color(0x5E88FC),
            new Color(0x264E86),
            new Color(0x081d38),
    };
    //iterations of the algorithm, warning: the number of points increases exponentially with the iteration count
    private final int iterations = 10;
    private final int steepnessOffset = 0;

    public LandscapeGraphics(Graphics2D g) {
        this.g = g;
        height = 1080;
        width = 1920;
    }

    public LandscapeGraphics(Graphics2D g, int width, int height) {
        this.g = g;
        this.height = height;
        this.width = width;
    }

    public void smoothen() {
        g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public void newColorScheme() {
        newColorScheme((float) Math.random() * 360);
    }

    public void newColorScheme(float hueSeed) {
        Color[] colors = new Color[5];
        colors[0] = HSLColor.toRGB(hueSeed, 50, 90);
        colors[1] = HSLColor.toRGB(hueSeed, 50, 76);
        colors[2] = HSLColor.toRGB(hueSeed, 50, 50);
        colors[3] = HSLColor.toRGB(hueSeed, 50, 30);
        colors[4] = HSLColor.toRGB(hueSeed, 50, 15);
        this.colors = colors;
    }

    private void drawPoints(List<Double> points, Color color, int offset){
        g.setColor(color);
        GeneralPath path = new GeneralPath();
        path.moveTo(0, (double) height / 2 + points.getFirst() + offset);
        for(int i = 0; i < points.size(); i++){
            double x = i * ((double) width / (points.size() - 1));
            double y = (double) height / 2 + points.get(i) + offset;
            path.lineTo(x, y);
        }
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.closePath();
        g.fill(path);
    }

    private void fill(Color color) {
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }

    private List<Double> generatePoints(java.util.List<Double> points, double steepness) {
        List<Double> newList = new ArrayList<>();
        newList.add(points.getFirst());
        for (int i = 1; i < points.size(); i++) {
            //get the average between the two points
            double avg = (points.get(i) + points.get(i - 1)) / 2;

            //get an offset
            double offsetAm = height / (((points.size() - 1) * 20) / steepness);

            //make the offset random and able to be negative
            Random random = new Random();
            double rand = random.nextDouble(-offsetAm, offsetAm);

            //add the randomness to the average (displace the point)
            avg += rand;

            //push the displaced point aswell as the original point
            newList.add(avg);
            newList.add(points.get(i));
        }
        return newList;
    }

    private java.util.List<Double> generateBatchPoints(double steepness) {
        List<Double> points = Arrays.asList(new Double[]{0D, 0D});

        for(var i = 0; i < iterations; i++){
            points = generatePoints(points, steepness);
        }
        return points;
    }

    public void generateLandscape() {
        fill(colors[0]);
        drawPoints(generateBatchPoints(10 + steepnessOffset), colors[1], -200);
        drawPoints(generateBatchPoints(07 + steepnessOffset), colors[2], 0);
        drawPoints(generateBatchPoints(05 + steepnessOffset), colors[3], 100);
        drawPoints(generateBatchPoints(06 + steepnessOffset), colors[4], 200);
    }
}
