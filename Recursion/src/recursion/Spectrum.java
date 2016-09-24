package recursion;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A color spectrum is an object that describes a color changing from one
 * to another smoothly.
 *
 * @author Jeff Niu
 */
public class Spectrum implements ColorMap {

    /**
     * Black White
     */
    public static Spectrum GreyScale;

    /**
     * Rainbow
     */
    public static Spectrum Rainbow;

    /**
     * Red Orange
     */
    public static Spectrum RedOrange;

    /**
     * Black Gold Yellow
     */
    public static Spectrum BlackGoldYellow;

    /**
     * Black Yellow Purple
     */
    public static Spectrum BlackYellowPurple;

    /**
     * Black Yellow White Blue
     */
    public static Spectrum BlackYellowBlue;

    /**
     * Here, we create the color spectra. They are hard-coded and could be
     * easily stored in a text file using a reader, but this is appropriate
     * for now, I hope. The color spectra are created by providing a set of
     * colors from which to transition and a weighting. The weighting
     * determines how much of the whole spectrum this transition will
     * occupy. Spectra are then sampled to create color palettes.
     */
    static {
        GreyScale();
        Rainbow();
        RedOrange();
        BlackGoldYellow();
        BlackYellowPurple();
        BlackYellowBlue();
    }

    private static void GreyScale() {
        Color black = new Color(0, 0, 0);
        Color white = new Color(255, 255, 255);
        double f = 1;
        ColorIndex[] CI = {
            new ColorIndex(black, white, 1)
        };
        GreyScale = new Spectrum(CI, "GreyScale");
    }

    private static void Rainbow() {
        Color[] colorMap = getRGBMap("colorMap.txt", 256);
        double f = 1;
        ColorIndex[] CI = new ColorIndex[colorMap.length - 1];
        for (int c = 0; c <= colorMap.length - 2; c++) {
            CI[c] = new ColorIndex(colorMap[c], colorMap[c + 1], f);
        }
        Rainbow = new Spectrum(CI, "Rainbow");
    }

    private static void RedOrange() {
        Color black = new Color(0, 0, 0);
        Color red1 = new Color(173, 21, 13);
        Color red2 = new Color(240, 99, 64);
        Color white = new Color(255, 255, 255);
        Color red3 = new Color(215, 0, 0);
        Color red4 = new Color(155, 0, 0);
        Color orange = new Color(245, 137, 50);
        double f = 1;
        ColorIndex[] CI = {
            new ColorIndex(black, red1, 0.2),
            new ColorIndex(red1, red2, 0.2),
            new ColorIndex(red2, white, 0.2),
            new ColorIndex(white, red3, 0.5),
            new ColorIndex(red3, red4, 0.3),
            new ColorIndex(red4, orange, 0.2)
        };
        RedOrange = new Spectrum(CI, "Fiery");
    }

    private static void BlackGoldYellow() {
        Color black = new Color(0, 0, 0);
        Color gold1 = new Color(111, 66, 43);
        Color gold2 = new Color(158, 90, 43);
        Color gold3 = new Color(216, 151, 41);
        Color white = new Color(230, 230, 230);
        double f = 1;
        ColorIndex[] CI = {
            new ColorIndex(black, gold1, 1.0),
            new ColorIndex(gold1, gold2, 0.7),
            new ColorIndex(gold2, gold3, 1.0),
            new ColorIndex(gold3, white, 0.2),
            new ColorIndex(white, gold2, 1.2),
            new ColorIndex(gold2, black, 0.5)
        };
        BlackGoldYellow = new Spectrum(CI, "Black and Gold");
    }

    private static void BlackYellowPurple() {
        Color black = new Color(0, 0, 0);
        Color yellow = new Color(255, 127, 0);
        Color purple = new Color(127, 0, 127);
        double f = 1;
        ColorIndex[] CI = {
            new ColorIndex(black, yellow, f),
            new ColorIndex(yellow, purple, f),
            new ColorIndex(purple, black, 0.2)
        };
        BlackYellowPurple = new Spectrum(CI, "Yellow and Purple");
    }

    private static void BlackYellowBlue() {
        Color darkBlue = new Color(0, 0, 128);
        ColorIndex[] CI = {
            new ColorIndex(Color.BLACK, Color.ORANGE, 1),
            new ColorIndex(Color.ORANGE, Color.WHITE, 1),
            new ColorIndex(Color.WHITE, Color.BLUE, 1),
            new ColorIndex(Color.BLUE, darkBlue, 1),
            new ColorIndex(darkBlue, Color.BLACK, 0.05)
        };
        BlackYellowBlue = new Spectrum(CI, "Yellow and Blue");
    }

    /**
     * Load an RGB map.
     *
     * @param fileName
     * @param max
     * @return
     */
    private static Color[] getRGBMap(String fileName, int max) {
        Color[] colorMap = new Color[max];
        File f = new File(fileName);
        BufferedReader rdr;
        try {
            rdr = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            return null;
        }
        for (int i = 0; i < max; i++) {
            String[] values;
            try {
                values = rdr.readLine().split(" ");
            } catch (IOException ex) {
                return null;
            }
            int[] rgb = new int[values.length];
            for (int j = 0; j < values.length; j++) {
                rgb[j] = Integer.valueOf(values[j]);
            }
            colorMap[i] = new Color(rgb[0], rgb[1], rgb[2]);
        }
        return colorMap;
    }

    /**
     * The name of the spectrum.
     */
    private final String name;

    /**
     * The change in red, green, and blue.
     */
    private final int[] dR, dG, dB;
    /**
     * The initial red, green, and blue.
     */
    private final int[] R1, G1, B1;
    /**
     * The weightings.
     */
    private final double[] F;
    /**
     * The cumulative weighting.
     */
    private final double[] Fcul;

    /**
     * Create a spectrum from an array of color indices.
     *
     * @param CI the array of color indices
     * @param name the name of the spectrum
     */
    private Spectrum(ColorIndex[] CI, String name) {
        this.name = name;
        // Calculate the total weighthing
        double fTotal = 0.0;
        for (ColorIndex ci : CI) {
            fTotal += ci.f;
        }
        double f = 0.0;
        int length = CI.length;
        // Create all the arrays; we are going to precompute everything and 
        // store it in memory so that, when a fractal is being colored, 
        // no additional calculations need to be performed.
        F = new double[length];
        Fcul = new double[length + 1];
        dR = new int[length];
        dG = new int[length];
        dB = new int[length];
        R1 = new int[length];
        G1 = new int[length];
        B1 = new int[length];
        for (int g = 0; g < length; g++) {
            // Calculate the percentage weight
            F[g] = CI[g].f / fTotal;
            // Add to the culmulative weight
            Fcul[g] = f;
            f += F[g];
            // Set the initial RGB and change in RGB
            R1[g] = CI[g].c1.getRed();
            G1[g] = CI[g].c1.getGreen();
            B1[g] = CI[g].c1.getBlue();
            dR[g] = CI[g].c2.getRed() - R1[g];
            dG[g] = CI[g].c2.getGreen() - G1[g];
            dB[g] = CI[g].c2.getBlue() - B1[g];
        }
        Fcul[length] = 1;
    }

    /**
     * Get the RGB value.
     *
     * @param t the escape time
     * @param T the maximum escape time
     * @return and RGB value
     */
    @Override
    public int getRGB(int t, int T) {
        double f = t / (double) T;
        int g = 0;
        while (!(Fcul[g] <= f && f <= Fcul[g + 1])) {
            g++;
        }
        f -= Fcul[g];
        f /= F[g];
        int R = R1[g] + (int) (dR[g] * f);
        int G = G1[g] + (int) (dG[g] * f);
        int B = B1[g] + (int) (dB[g] * f);
        int rgb = new Color(R, G, B).getRGB();
        return rgb;
    }

    /**
     * @return the name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * A color index describes one color transition from an initial color
     * to a final color.
     */
    private static class ColorIndex {

        /**
         * The initial and final colors.
         */
        private final Color c1, c2;
        /**
         * The weight.
         */
        private double f;

        /**
         * Create a color index.
         *
         * @param c1 the initial color
         * @param c2 the final color
         * @param f the weighting
         */
        private ColorIndex(Color c1, Color c2, double f) {
            this.c1 = c1;
            this.c2 = c2;
            this.f = f;
        }

    }

}
