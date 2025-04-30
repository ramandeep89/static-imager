package top.bagadbilla.model.generation.svg;

import com.google.common.collect.ImmutableMap;
import dev.mccue.color.Color;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeoPatternsSVG extends BaseSVG {

    private static final String FILL_COLOR_DARK = "#222";
    private static final String FILL_COLOR_LIGHT = "#ddd";
    private static final String STROKE_COLOR = "#000";
    private static final double STROKE_OPACITY = 0.02;
    private static final double OPACITY_MIN = 0.02;
    private static final double OPACITY_MAX = 0.15;
    private static final Map<String, String> DEFAULTS = Map.of("baseColor", "#933c3c");
    private final String hash;
    private final Map<String, String> opts;

    public GeoPatternsSVG(int width, int height, Map<String, String> opts) {
        super(width, height);
        tag("defs", Collections.emptyMap());
        tag("pattern", Map.of("id", "pattern", "patternUnits", "userSpaceOnUse"));
        this.opts = new HashMap<>(DEFAULTS);
        if (opts != null) this.opts.putAll(opts);
        StringBuilder builder = new StringBuilder();
        long seed = this.opts.containsKey("seed") ? Long.parseLong(this.opts.get("seed")) : System.currentTimeMillis();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(ByteBuffer.allocate(Long.BYTES).putLong(seed).array());
            for (byte b : md.digest()) {
                builder.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        hash = builder.toString();
    }

    /**
     * Extract a substring from a hex string and parse it as an integer
     *
     * @param hash  - Source hex string
     * @param index - Start index of substring
     * @param len   - Length of substring. Defaults to 1.
     */
    private int hexVal(String hash, int index, int len) {
        return Integer.parseInt(hash.substring(index, Math.min(index + (len > 0 ? len : 1), hash.length())), 16);
    }

    private int hexVal(String hash, int index) {
        return hexVal(hash, index, 0);
    }

    /*
     * Re-maps a number from one range to another
     * http://processing.org/reference/map_.html
     */
    private double map(double value, double vMin, double vMax, double dMin, double dMax) {
        var vRange = vMax - vMin;
        var dRange = dMax - dMin;

        return (value - vMin) * dRange / vRange + dMin;
    }

    private String fillColor(double val) {
        return val % 2 == 0 ? FILL_COLOR_LIGHT : FILL_COLOR_DARK;
    }

    private double fillOpacity(double val) {
        return map(val, 0, 15, OPACITY_MIN, OPACITY_MAX);
    }

    private void setHeight(double height) {
        currentContext().setAttribute("height", String.valueOf(Math.floor(height)));
    }

    private void setWidth(double width) {
        currentContext().setAttribute("width", String.valueOf(Math.floor(width)));
    }

    private String buildHexagonShape(double sideLength) {
        double a = sideLength / 2;
        double b = Math.sin(60 * Math.PI / 180) * sideLength;
        return Arrays.stream(new double[]{0, b, a, 0, a + sideLength, 0, 2 * sideLength, b, a + sideLength, 2 * b, a, 2 * b, 0, b}).mapToObj(String::valueOf).collect(Collectors.joining(","));
    }

    private void geoHexagons() {
        final double scale = hexVal(this.hash, 0);
        final double sideLength = map(scale, 0, 15, 8, 60);
        final double hexHeight = sideLength * Math.sqrt(3);
        final double hexWidth = sideLength * 2;
        final String hex = buildHexagonShape(sideLength);
        double dy, opacity, val;
        int i, x, y;
        String fill;
        Map<String, Object> styles;
        setWidth(hexWidth * 3 + sideLength * 3);
        setHeight(hexHeight * 6);
        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                dy = x % 2 == 0 ? y * hexHeight : y * hexHeight + hexHeight / 2;
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);
                this.polyline(hex, styles).transform(ImmutableMap.of("translate", List.of(x * sideLength * 1.5 - hexWidth / 2, dy - hexHeight / 2)));

                // Add an extra one at top-right, for tiling.
                if (x == 0) {
                    this.polyline(hex, styles).transform(ImmutableMap.of("translate", List.of(6 * sideLength * 1.5 - hexWidth / 2, dy - hexHeight / 2)));
                }

                // Add an extra row at the end that matches the first row, for tiling.
                if (y == 0) {
                    dy = x % 2 == 0 ? 6 * hexHeight : 6 * hexHeight + hexHeight / 2;
                    this.polyline(hex, styles).transform(ImmutableMap.of("translate", List.of(x * sideLength * 1.5 - hexWidth / 2, dy - hexHeight / 2)));
                }

                // Add an extra one at bottom-right, for tiling.
                if (x == 0 && y == 0) {
                    this.polyline(hex, styles).transform(ImmutableMap.of("translate", List.of(6 * sideLength * 1.5 - hexWidth / 2, 5 * hexHeight + hexHeight / 2)));
                }

                i++;
            }
        }
    }

    private void geoSineWaves() {
        double period = Math.floor(map(hexVal(this.hash, 0, 0), 0, 15, 100, 400));
        int amplitude = (int) Math.floor(map(hexVal(this.hash, 1, 0), 0, 15, 30, 100));
        int waveWidth = (int) Math.floor(map(hexVal(this.hash, 2, 0), 0, 15, 3, 30));
        double opacity, val, xOffset;
        String fill, str;
        int i;
        Map<String, Object> styles;
        setWidth(period);
        setHeight(waveWidth * 36);

        for (i = 0; i < 36; i++) {
            val = hexVal(this.hash, i);
            opacity = fillOpacity(val);
            fill = fillColor(val);
            xOffset = period / 4 * 0.7;
            styles = Map.of("fill", "none", "stroke", fill, "opacity", opacity, "stroke-width", waveWidth + "px");

            str = "M0 " + amplitude + " C " + xOffset + " 0, " + (period / 2 - xOffset) + " 0, " + period / 2 + ' ' + amplitude + " S " + (period - xOffset) + " " + amplitude * 2 + ", " + period + " " + amplitude + " S " + (period * 1.5 - xOffset) + " 0, " + period * 1.5 + ", " + amplitude;

            path(str, styles).transform(ImmutableMap.of("translate", List.of(-period / 4, waveWidth * i - amplitude * 1.5)));
            path(str, styles).transform(ImmutableMap.of("translate", List.of(-period / 4, waveWidth * i - amplitude * 1.5 + waveWidth * 36)));
        }
    }

    private List<String> buildChevronShape(double w, double h) {
        var e = h * 0.66;
        return Stream.of(List.of(0, 0, w / 2, h - e, w / 2, h, 0, e, 0, 0), List.of(w / 2, h - e, w, 0, w, e, w / 2, h, w / 2, h - e)).map(l -> l.stream().map(String::valueOf).collect(Collectors.joining(","))).collect(Collectors.toList());
    }

    private void geoChevrons() {
        double chevronWidth = map(hexVal(this.hash, 0), 0, 15, 30, 80);
        double chevronHeight = map(hexVal(this.hash, 0), 0, 15, 30, 80);
        List<String> chevron = buildChevronShape(chevronWidth, chevronHeight);
        Map<String, Object> styles;
        int i, x, y;
        String fill;
        double opacity, val;

        setWidth(chevronWidth * 6);
        setHeight(chevronHeight * 6 * 0.66);

        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY, "stroke-width", 1);

                group(styles).transform(ImmutableMap.of("translate", List.of(x * chevronWidth, y * chevronHeight * 0.66 - chevronHeight / 2))).polyline(chevron, Collections.emptyMap()).end();

                // Add an extra row at the end that matches the first row, for tiling.
                if (y == 0) {
                    group(styles).transform(ImmutableMap.of("translate", List.of(x * chevronWidth, 6 * chevronHeight * 0.66 - chevronHeight / 2))).polyline(chevron, Collections.emptyMap()).end();
                }

                i++;
            }
        }
    }

    private List<double[]> buildPlusShape(double squareSize) {
        return List.of(new double[]{squareSize, 0, squareSize, squareSize * 3}, new double[]{0, squareSize, squareSize * 3, squareSize});
    }

    private void geoPlusSigns() {
        double squareSize = map(hexVal(this.hash, 0), 0, 15, 10, 25);
        double plusSize = squareSize * 3;
        List<double[]> plusShape = buildPlusShape(squareSize);
        Map<String, Object> styles;
        int i, x, y;
        String fill;
        double dx, opacity, val;

        setWidth(squareSize * 12);
        setHeight(squareSize * 12);

        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                dx = y % 2 == 0 ? 0 : 1;
                styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);

                group(styles).transform(ImmutableMap.of("translate", List.of(x * plusSize - x * squareSize + dx * squareSize - squareSize, y * plusSize - y * squareSize - plusSize / 2))).rect(plusShape, Collections.emptyMap()).end();

                // Add an extra column on the right for tiling.
                if (x == 0) {
                    group(styles).transform(ImmutableMap.of("translate", List.of(4 * plusSize - x * squareSize + dx * squareSize - squareSize, y * plusSize - y * squareSize - plusSize / 2))).rect(plusShape, Collections.emptyMap()).end();
                }

                // Add an extra row on the bottom that matches the first row, for tiling
                if (y == 0) {
                    group(styles).transform(ImmutableMap.of("translate", List.of(x * plusSize - x * squareSize + dx * squareSize - squareSize, 4 * plusSize - y * squareSize - plusSize / 2))).rect(plusShape, Collections.emptyMap()).end();
                }

                // Add an extra one at top-right and bottom-right, for tiling
                if (x == 0 && y == 0) {
                    group(styles).transform(ImmutableMap.of("translate", List.of(4 * plusSize - x * squareSize + dx * squareSize - squareSize, 4 * plusSize - y * squareSize - plusSize / 2))).rect(plusShape, Collections.emptyMap()).end();
                }

                i++;
            }
        }
    }

    private void geoXes() {
        double squareSize = map(hexVal(this.hash, 0), 0, 15, 10, 25);
        List<double[]> xShape = buildPlusShape(squareSize);
        double xSize = squareSize * 3 * 0.943;
        Map<String, Object> styles;
        int i, x, y;
        String fill;
        double dy, opacity, val;
        setWidth(xSize * 3);
        setHeight(xSize * 3);
        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                dy = x % 2 == 0 ? y * xSize - xSize * 0.5 : y * xSize - xSize * 0.5 + xSize / 4;
                fill = fillColor(val);
                styles = Map.of("fill", fill, "opacity", opacity);
                group(styles).transform(ImmutableMap.of("translate", List.of(x * xSize / 2 - xSize / 2, dy - y * xSize / 2), "rotate", List.of(45, xSize / 2, xSize / 2))).rect(xShape, Collections.emptyMap()).end();


                // Add an extra column on the right for tiling.
                if (x == 0) {
                    group(styles).transform(ImmutableMap.of("translate", List.of(6 * xSize / 2 - xSize / 2, dy - y * xSize / 2), "rotate", List.of(45, xSize / 2, xSize / 2))).rect(xShape, Collections.emptyMap()).end();
                }

                // // Add an extra row on the bottom that matches the first row, for tiling.
                if (y == 0) {
                    dy = x % 2 == 0 ? 6 * xSize - xSize / 2 : 6 * xSize - xSize / 2 + xSize / 4;
                    group(styles).transform(ImmutableMap.of("translate", List.of(x * xSize / 2 - xSize / 2, dy - 6 * xSize / 2), "rotate", List.of(45, xSize / 2, xSize / 2))).rect(xShape, Collections.emptyMap()).end();
                }

                // These can hang off the bottom, so put a row at the top for tiling.
                if (y == 5) {
                    group(styles).transform(ImmutableMap.of("translate", List.of(x * xSize / 2 - xSize / 2, dy - 11 * xSize / 2), "rotate", List.of(45, xSize / 2, xSize / 2))).rect(xShape, Collections.emptyMap()).end();
                }

                // Add an extra one at top-right and bottom-right, for tiling
                if (x == 0 && y == 0) {
                    group(styles).transform(ImmutableMap.of("translate", List.of(6 * xSize / 2 - xSize / 2, dy - 6 * xSize / 2), "rotate", List.of(45, xSize / 2, xSize / 2))).rect(xShape, Collections.emptyMap()).end();
                }
                i++;
            }
        }
    }

    private void geoOverlappingCircles() {
        int scale = hexVal(this.hash, 0);
        double diameter = map(scale, 0, 15, 25, 200);
        double radius = diameter / 2;
        Map<String, Object> styles;
        int i, x, y;
        String fill;
        double opacity, val;
        setWidth(radius * 6);
        setHeight(radius * 6);
        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", fill, "opacity", opacity);

                circle(x * radius, y * radius, radius, styles);

                // Add an extra one at top-right, for tiling.
                if (x == 0) {
                    circle(6 * radius, y * radius, radius, styles);
                }

                // // Add an extra row at the end that matches the first row, for tiling.
                if (y == 0) {
                    circle(x * radius, 6 * radius, radius, styles);
                }

                // // Add an extra one at bottom-right, for tiling.
                if (x == 0 && y == 0) {
                    circle(6 * radius, 6 * radius, radius, styles);
                }

                i++;
            }
        }
    }

    private String buildOctagonShape(double squareSize) {
        var c = squareSize * 0.33;
        return Stream.of(c, 0, squareSize - c, 0, squareSize, c, squareSize, squareSize - c, squareSize - c, squareSize, c, squareSize, 0, squareSize - c, 0, c, c, 0).map(String::valueOf).collect(Collectors.joining(","));

    }

    private void geoOctagons() {
        double squareSize = map(hexVal(this.hash, 0), 0, 15, 10, 60);
        String tile = buildOctagonShape(squareSize);
        String fill;
        Map<String, Object> styles;
        int i, x, y;
        double opacity, val;
        setWidth(squareSize * 6);
        setHeight(squareSize * 6);
        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);

                polyline(tile, styles).transform(ImmutableMap.of("translate", List.of(x * squareSize, y * squareSize)));

                i++;
            }
        }
    }

    private void geoSquares() {
        double squareSize = map(hexVal(this.hash, 0), 0, 15, 10, 60);
        String fill;
        Map<String, Object> styles;
        int i, x, y;
        double opacity, val;
        setWidth(squareSize * 6);
        setHeight(squareSize * 6);
        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);

                rect(x * squareSize, y * squareSize, squareSize, squareSize, styles);
                i++;
            }
        }
    }

    private void geoConcentricCircles() {
        int scale = hexVal(this.hash, 0);
        double ringSize = map(scale, 0, 15, 10, 60);
        double strokeWidth = ringSize / 5;
        String fill;
        int i, x, y;
        double opacity, val;
        setWidth((ringSize + strokeWidth) * 6);
        setHeight((ringSize + strokeWidth) * 6);
        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);

                circle(x * ringSize + x * strokeWidth + (ringSize + strokeWidth) / 2, y * ringSize + y * strokeWidth + (ringSize + strokeWidth) / 2, ringSize / 2, Map.of("fill", "none", "opacity", opacity, "stroke", fill, "stroke-width", strokeWidth + "px"));


                val = hexVal(this.hash, 39 - i);
                opacity = fillOpacity(val);
                fill = fillColor(val);

                circle(x * ringSize + x * strokeWidth + (ringSize + strokeWidth) / 2, y * ringSize + y * strokeWidth + (ringSize + strokeWidth) / 2, ringSize / 4, Map.of("fill", fill, "fill-opacity", opacity));

                i++;
            }
        }
    }

    private void geoOverlappingRings() {
        int scale = hexVal(this.hash, 0);
        double ringSize = map(scale, 0, 15, 10, 60);
        double strokeWidth = ringSize / 4;
        String fill;
        Map<String, Object> styles;
        int i, x, y;
        double opacity, val;

        setWidth(ringSize * 6);
        setHeight(ringSize * 6);

        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", "none", "opacity", opacity, "stroke", fill, "stroke-width", strokeWidth + "px");

                circle(x * ringSize, y * ringSize, ringSize - strokeWidth / 2, styles);

                // Add an extra one at top-right, for tiling.
                if (x == 0) {
                    circle(6 * ringSize, y * ringSize, ringSize - strokeWidth / 2, styles);
                }

                if (y == 0) {
                    circle(x * ringSize, 6 * ringSize, ringSize - strokeWidth / 2, styles);
                }

                if (x == 0 && y == 0) {
                    circle(6 * ringSize, 6 * ringSize, ringSize - strokeWidth / 2, styles);
                }

                i++;
            }
        }
    }

    private String buildTriangleShape(double sideLength, double height) {
        var halfWidth = sideLength / 2;
        return Stream.of(halfWidth, 0, sideLength, height, 0, height, halfWidth, 0).map(String::valueOf).collect(Collectors.joining(","));
    }

    private void geoTriangles() {
        int scale = hexVal(this.hash, 0);
        double sideLength = map(scale, 0, 15, 15, 80);
        double triangleHeight = sideLength / 2 * Math.sqrt(3);
        String triangle = buildTriangleShape(sideLength, triangleHeight);
        String fill;
        Map<String, Object> styles;
        int i, x, y;
        double opacity, val, rotation;

        setWidth(sideLength * 3);
        setHeight(triangleHeight * 6);

        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);

                if (y % 2 == 0) {
                    rotation = x % 2 == 0 ? 180 : 0;
                } else {
                    rotation = x % 2 != 0 ? 180 : 0;
                }

                polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x * sideLength * 0.5 - sideLength / 2, triangleHeight * y), "rotate", List.of(rotation, sideLength / 2, triangleHeight / 2)));

                // Add an extra one at top-right, for tiling.
                if (x == 0) {
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(6 * sideLength * 0.5 - sideLength / 2, triangleHeight * y), "rotate", List.of(rotation, sideLength / 2, triangleHeight / 2)));
                }
                i++;
            }
        }
    }

    private String buildDiamondShape(double width, double height) {
        return Stream.of(width / 2, 0, width, height / 2, width / 2, height, 0, height / 2).map(String::valueOf).collect(Collectors.joining(","));
    }

    private void geoDiamonds() {
        double diamondWidth = map(hexVal(this.hash, 0), 0, 15, 10, 50);
        double diamondHeight = map(hexVal(this.hash, 1), 0, 15, 10, 50);
        String diamond = buildDiamondShape(diamondWidth, diamondHeight);
        String fill;
        Map<String, Object> styles;
        int i, x, y;
        double opacity, val, dx;

        setWidth(diamondWidth * 6);
        setHeight(diamondHeight * 3);

        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);

                dx = y % 2 == 0 ? 0 : diamondWidth / 2;

                polyline(diamond, styles).transform(ImmutableMap.of("translate", List.of(x * diamondWidth - diamondWidth / 2 + dx, diamondHeight / 2 * y - diamondHeight / 2)));

                // Add an extra one at top-right, for tiling.
                if (x == 0) {
                    polyline(diamond, styles).transform(ImmutableMap.of("translate", List.of(6 * diamondWidth - diamondWidth / 2 + dx, diamondHeight / 2 * y - diamondHeight / 2)));
                }

                // Add an extra row at the end that matches the first row, for tiling.
                if (y == 0) {
                    polyline(diamond, styles).transform(ImmutableMap.of("translate", List.of(x * diamondWidth - diamondWidth / 2 + dx, diamondHeight / 2 * 6 - diamondHeight / 2)));
                }

                // Add an extra one at bottom-right, for tiling.
                if (x == 0 && y == 0) {
                    polyline(diamond, styles).transform(ImmutableMap.of("translate", List.of(6 * diamondWidth - diamondWidth / 2 + dx, diamondHeight / 2 * 6 - diamondHeight / 2)));
                }
                i++;
            }
        }
    }

    private void geoNestedSquares() {
        double blockSize = map(hexVal(this.hash, 0), 0, 15, 4, 12);
        double squareSize = blockSize * 7;
        String fill;
        Map<String, Object> styles;
        int i, x, y;
        double opacity, val;

        setWidth((squareSize + blockSize) * 6 + blockSize * 6);
        setHeight((squareSize + blockSize) * 6 + blockSize * 6);

        i = 0;
        for (y = 0; y < 6; y++) {
            for (x = 0; x < 6; x++) {
                val = hexVal(this.hash, i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", "none", "opacity", opacity, "stroke", fill, "stroke-width", blockSize + "px");

                rect(x * squareSize + x * blockSize * 2 + blockSize / 2, y * squareSize + y * blockSize * 2 + blockSize / 2, squareSize, squareSize, styles);

                val = hexVal(this.hash, 39 - i);
                opacity = fillOpacity(val);
                fill = fillColor(val);
                styles = Map.of("fill", "none", "opacity", opacity, "stroke", fill, "stroke-width", blockSize + "px");

                rect(x * squareSize + x * blockSize * 2 + blockSize / 2 + blockSize * 2, y * squareSize + y * blockSize * 2 + blockSize / 2 + blockSize * 2, blockSize * 3, blockSize * 3, styles);

                i++;
            }
        }
    }

    private String buildRightTriangleShape(double sideLength) {
        return Stream.of(0, 0, sideLength, sideLength, 0, sideLength, 0, 0).map(String::valueOf).collect(Collectors.joining(","));
    }

    private void drawInnerMosaicTile(double x, double y, double triangleSize, double[] vals) {
        String triangle = buildRightTriangleShape(triangleSize);
        double opacity = fillOpacity(vals[0]);
        String fill = fillColor(vals[0]);
        Map<String, Object> styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);
        polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x + triangleSize, y), "scale", List.of(-1, 1)));
        polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x + triangleSize, y + triangleSize * 2), "scale", List.of(1, -1)));
        opacity = fillOpacity(vals[1]);
        fill = fillColor(vals[1]);
        styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);
        polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x + triangleSize, y + triangleSize * 2), "scale", List.of(-1, -1)));
        polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x + triangleSize, y), "scale", List.of(1, 1)));
    }

    private void drawOuterMosaicTile(double x, double y, double triangleSize, double val) {
        double opacity = fillOpacity(val);
        String fill = fillColor(val);
        String triangle = buildRightTriangleShape(triangleSize);
        Map<String, Object> styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY);
        polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x, y + triangleSize), "scale", List.of(1, -1)));
        polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x + triangleSize * 2, y + triangleSize), "scale", List.of(-1, -1)));
        polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x, y + triangleSize), "scale", List.of(1, 1)));
        polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(x + triangleSize * 2, y + triangleSize), "scale", List.of(-1, 1)));
    }

    private void geoMosaicSquares() {
        double triangleSize = map(hexVal(this.hash, 0), 0, 15, 15, 50);
        int i, x, y;

        setWidth(triangleSize * 8);
        setHeight(triangleSize * 8);

        i = 0;
        for (y = 0; y < 4; y++) {
            for (x = 0; x < 4; x++) {
                if (x % 2 == 0) {
                    if (y % 2 == 0) {
                        drawOuterMosaicTile(x * triangleSize * 2, y * triangleSize * 2, triangleSize, hexVal(this.hash, i));
                    } else {
                        drawInnerMosaicTile(x * triangleSize * 2, y * triangleSize * 2, triangleSize, new double[]{hexVal(this.hash, i), hexVal(this.hash, i + 1)});
                    }
                } else {
                    if (y % 2 == 0) {
                        drawInnerMosaicTile(x * triangleSize * 2, y * triangleSize * 2, triangleSize, new double[]{hexVal(this.hash, i), hexVal(this.hash, i + 1)});
                    } else {
                        drawOuterMosaicTile(x * triangleSize * 2, y * triangleSize * 2, triangleSize, hexVal(this.hash, i));
                    }
                }

                i++;
            }
        }
    }

    private void geoPlaid() {
        double height = 0;
        double width = 0;
        String fill;
        int i;
        double opacity, val, space, stripeHeight, stripeWidth;

        // Horizontal stripes
        i = 0;
        while (i < 36) {
            space = hexVal(this.hash, i);
            height += space + 5;

            val = hexVal(this.hash, i + 1);
            opacity = fillOpacity(val);
            fill = fillColor(val);
            stripeHeight = val + 5;

            rect(0, height, "100%", String.valueOf(stripeHeight), Map.of("opacity", opacity, "fill", fill));

            height += stripeHeight;
            i += 2;
        }

        // Vertical stripes
        i = 0;
        while (i < 36) {
            space = hexVal(this.hash, i);
            width += space + 5;

            val = hexVal(this.hash, i + 1);
            opacity = fillOpacity(val);
            fill = fillColor(val);
            stripeWidth = val + 5;

            rect(width, 0, String.valueOf(stripeWidth), "100%", Map.of("opacity", opacity, "fill", fill));

            width += stripeWidth;
            i += 2;
        }
        setWidth(width);
        setHeight(height);
    }

    private String buildRotatedTriangleShape(double sideLength, double triangleHeight) {
        var halfHeight = sideLength / 2;
        return Stream.of(0, 0, triangleHeight, halfHeight, 0, sideLength, 0, 0).map(String::valueOf).collect(Collectors.joining(","));
    }

    private void geoTessellation() {
        // 3.4.6.4 semi-regular tessellation
        double sideLength = map(hexVal(this.hash, 0), 0, 15, 5, 40);
        double hexHeight = sideLength * Math.sqrt(3);
        double hexWidth = sideLength * 2;
        double triangleHeight = sideLength / 2 * Math.sqrt(3);
        String triangle = buildRotatedTriangleShape(sideLength, triangleHeight);
        double tileWidth = sideLength * 3 + triangleHeight * 2;
        double tileHeight = (hexHeight * 2) + (sideLength * 2);
        double opacity, val;
        String fill;
        Map<String, Object> styles;
        int i;

        setWidth(tileWidth);
        setHeight(tileHeight);

        for (i = 0; i < 20; i++) {
            val = hexVal(this.hash, i);
            opacity = fillOpacity(val);
            fill = fillColor(val);
            styles = Map.of("fill", fill, "fill-opacity", opacity, "stroke", STROKE_COLOR, "stroke-opacity", STROKE_OPACITY, "stroke-width", 1);

            switch (i) {
                case 0: // All 4 corners x
                    rect(-sideLength / 2, -sideLength / 2, sideLength, sideLength, styles);
                    rect(tileWidth - sideLength / 2, -sideLength / 2, sideLength, sideLength, styles);
                    rect(-sideLength / 2, tileHeight - sideLength / 2, sideLength, sideLength, styles);
                    rect(tileWidth - sideLength / 2, tileHeight - sideLength / 2, sideLength, sideLength, styles);
                    break;
                case 1: // Center / top square x
                    rect(hexWidth / 2 + triangleHeight, hexHeight / 2, sideLength, sideLength, styles);
                    break;
                case 2: // Side squares x
                    rect(-sideLength / 2, tileHeight / 2 - sideLength / 2, sideLength, sideLength, styles);
                    rect(tileWidth - sideLength / 2, tileHeight / 2 - sideLength / 2, sideLength, sideLength, styles);
                    break;
                case 3: // Center / bottom square x
                    rect(hexWidth / 2 + triangleHeight, hexHeight * 1.5 + sideLength, sideLength, sideLength, styles);
                    break;
                case 4: // Left top / bottom triangle x
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(sideLength / 2, -sideLength / 2), "rotate", List.of(0, sideLength / 2, triangleHeight / 2)));
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(sideLength / 2, tileHeight - -sideLength / 2), "rotate", List.of(0, sideLength / 2, triangleHeight / 2), "scale", List.of(1, -1)));
                    break;
                case 5: // Right top / bottom triangle x
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(tileWidth - sideLength / 2, -sideLength / 2), "rotate", List.of(0, sideLength / 2, triangleHeight / 2), "scale", List.of(-1, 1)));
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(tileWidth - sideLength / 2, tileHeight + sideLength / 2), "rotate", List.of(0, sideLength / 2, triangleHeight / 2), "scale", List.of(-1, -1)));
                    break;
                case 6: // Center / top / right triangle x
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(tileWidth / 2 + sideLength / 2, hexHeight / 2)));
                    break;
                case 7: // Center / top / left triangle x
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(tileWidth - tileWidth / 2 - sideLength / 2, hexHeight / 2), "scale", List.of(-1, 1)));
                    break;
                case 8: // Center / bottom / right triangle x
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(tileWidth / 2 + sideLength / 2, tileHeight - hexHeight / 2), "scale", List.of(1, -1)));
                    break;
                case 9: // Center / bottom / left triangle x
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(tileWidth - tileWidth / 2 - sideLength / 2, tileHeight - hexHeight / 2), "scale", List.of(-1, -1)));
                    break;
                case 10: // Left / middle triangle x
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(sideLength / 2, tileHeight / 2 - sideLength / 2)));
                    break;
                case 11: // Right // middle triangle x
                    polyline(triangle, styles).transform(ImmutableMap.of("translate", List.of(tileWidth - sideLength / 2, tileHeight / 2 - sideLength / 2), "scale", List.of(-1, 1)));
                    break;
                case 12: // Left / top square x
                    rect(0, 0, sideLength, sideLength, styles).transform(ImmutableMap.of("translate", List.of(sideLength / 2, sideLength / 2), "rotate", List.of(-30, 0, 0)));
                    break;
                case 13: // Right / top square x
                    rect(0, 0, sideLength, sideLength, styles).transform(ImmutableMap.of("scale", List.of(-1, 1), "translate", List.of(-tileWidth + sideLength / 2, sideLength / 2), "rotate", List.of(-30, 0, 0)));
                    break;
                case 14: // Left / center-top square x
                    rect(0, 0, sideLength, sideLength, styles).transform(ImmutableMap.of("translate", List.of(sideLength / 2, tileHeight / 2 - sideLength / 2 - sideLength), "rotate", List.of(30, 0, sideLength)));
                    break;
                case 15: // Right / center-top square x
                    rect(0, 0, sideLength, sideLength, styles).transform(ImmutableMap.of("scale", List.of(-1, 1), "translate", List.of(-tileWidth + sideLength / 2, tileHeight / 2 - sideLength / 2 - sideLength), "rotate", List.of(30, 0, sideLength)));
                    break;
                case 16: // Left / center-top square x
                    rect(0, 0, sideLength, sideLength, styles).transform(ImmutableMap.of("scale", List.of(1, -1), "translate", List.of(sideLength / 2, -tileHeight + tileHeight / 2 - sideLength / 2 - sideLength), "rotate", List.of(30, 0, sideLength)));
                    break;
                case 17: // Right / center-bottom square x
                    rect(0, 0, sideLength, sideLength, styles).transform(ImmutableMap.of("scale", List.of(-1, -1), "translate", List.of(-tileWidth + sideLength / 2, -tileHeight + tileHeight / 2 - sideLength / 2 - sideLength), "rotate", List.of(30, 0, sideLength)));
                    break;
                case 18: // Left / bottom square x
                    rect(0, 0, sideLength, sideLength, styles).transform(ImmutableMap.of("scale", List.of(1, -1), "translate", List.of(sideLength / 2, -tileHeight + sideLength / 2), "rotate", List.of(-30, 0, 0)));
                    break;
                case 19: // Right / bottom square x
                    rect(0, 0, sideLength, sideLength, styles).transform(ImmutableMap.of("scale", List.of(-1, -1), "translate", List.of(-tileWidth + sideLength / 2, -tileHeight + sideLength / 2), "rotate", List.of(-30, 0, 0)));
                    break;
            }
        }
    }

    private void generateBackground() {
        Color rgb;
        if (opts.containsKey("color")) rgb = Color.hex(opts.get("color"));
        else {
            double hueOffset = map(hexVal(this.hash, 14, 3), 0, 4095, 0, 359);
            int satOffset = hexVal(this.hash, 17);
            Color baseColor = Color.hex(opts.get("baseColor"));
            double h = (baseColor.HSL().H() - hueOffset + 360) % 360;
            double s = satOffset % 2 == 0 ? Math.min(1, (baseColor.HSL().S() * 100 + satOffset) / 100) : Math.max(0, (baseColor.HSL().S() * 100 - satOffset) / 100);
            rgb = Color.HSL(h, s, baseColor.HSL().L());
        }
        rect(0, 0, "100%", "100%", Map.of("fill", rgb.hex()));
    }

    private void generatePattern() {
        Enum<PATTERNS> pattern;
        if (opts.containsKey("pattern")) pattern = Enum.valueOf(PATTERNS.class, opts.get("pattern"));
        else pattern = PATTERNS.values()[hexVal(this.hash, 20, 0)];
        switch (pattern) {
            case PATTERNS.hexagons -> geoHexagons();
            case PATTERNS.sineWaves -> geoSineWaves();
            case PATTERNS.chevrons -> geoChevrons();
            case PATTERNS.plusSigns -> geoPlusSigns();
            case PATTERNS.xes -> geoXes();
            case PATTERNS.overlappingCircles -> geoOverlappingCircles();
            case PATTERNS.octagons -> geoOctagons();
            case PATTERNS.squares -> geoSquares();
            case PATTERNS.concentricCircles -> geoConcentricCircles();
            case PATTERNS.overlappingRings -> geoOverlappingRings();
            case PATTERNS.triangles -> geoTriangles();
            case PATTERNS.diamonds -> geoDiamonds();
            case PATTERNS.nestedSquares -> geoNestedSquares();
            case PATTERNS.mosaicSquares -> geoMosaicSquares();
            case PATTERNS.plaid -> geoPlaid();
            case PATTERNS.tessellation -> geoTessellation();
            default -> throw new IllegalStateException("Unexpected value: " + pattern);
        }
    }

    @Override
    public GeoPatternsSVG generateSVG() {
        generateBackground();
        generatePattern();
        clear();
        rect(0, 0, "100%", "100%", Map.of("fill", "url(#pattern)"));
        return this;
    }

    private enum PATTERNS {
        octagons, overlappingCircles, plusSigns, xes, sineWaves, hexagons, overlappingRings, plaid, triangles, squares, concentricCircles, diamonds, tessellation, nestedSquares, mosaicSquares, chevrons
    }

}
