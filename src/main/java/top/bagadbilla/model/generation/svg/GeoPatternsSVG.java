package top.bagadbilla.model.generation.svg;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class GeoPatternsSVG extends BaseSVG {

    public GeoPatternsSVG(int width, int height, long seed) {
        super(width, height);
        StringBuilder builder = new StringBuilder();
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
        return Integer.parseInt(hash.substring(index, len > 0 ? len : 1), 16);
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

    private String fillColor(int val) {
        return (val % 2 == 0) ? FILL_COLOR_LIGHT : FILL_COLOR_DARK;
    }

    private double fillOpacity(int val) {
        return map(val, 0, 15, OPACITY_MIN, OPACITY_MAX);
    }

    @Override
    public GeoPatternsSVG generateSVG() {
        return this;
    }

    private final String hash;

    private static final String FILL_COLOR_DARK = "#222";
    private static final String FILL_COLOR_LIGHT = "#ddd";
    private static final String STROKE_COLOR = "#000";
    private static final double STROKE_OPACITY = 0.02;
    private static final double OPACITY_MIN = 0.02;
    private static final double OPACITY_MAX = 0.15;

    private static final Map<String, String> DEFAULTS = Map.of(
            "baseColor", "#933c3c"
    );

    private enum PATTERNS {
        octogons,
        overlappingCircles,
        plusSigns,
        xes,
        sineWaves,
        hexagons,
        overlappingRings,
        plaid,
        triangles,
        squares,
        concentricCircles,
        diamonds,
        tessellation,
        nestedSquares,
        mosaicSquares,
        chevrons
    }

}
