package top.bagadbilla.model.generation.graphics;

import top.bagadbilla.model.generation.HSLColor;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LandscapeGraphics extends BaseGraphics {

    // default initial colors
    private Color[] colors = new Color[]{
            new Color(0xAFFFFF),
            new Color(0x74DBEF),
            new Color(0x5E88FC),
            new Color(0x264E86),
            new Color(0x081d38),
    };

    public LandscapeGraphics(int width, int height) {
        super(width, height);
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
        List<Double> points = Arrays.asList(0D, 0D);

        //iterations of the algorithm, warning: the number of points increases exponentially with the iteration count
        int iterations = 10;
        for(var i = 0; i < iterations; i++){
            points = generatePoints(points, steepness);
        }
        return points;
    }

    @Override
    public LandscapeGraphics generateImage() {
        fill(colors[0]);
        int steepnessOffset = 0;
        drawPoints(generateBatchPoints(10 + steepnessOffset), colors[1], -200);
        drawPoints(generateBatchPoints(7 + steepnessOffset), colors[2], 0);
        drawPoints(generateBatchPoints(5 + steepnessOffset), colors[3], 100);
        drawPoints(generateBatchPoints(6 + steepnessOffset), colors[4], 200);

        return this;
    }
}
