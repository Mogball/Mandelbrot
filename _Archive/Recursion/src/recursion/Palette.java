package recursion;

/**
 * A palette is a color map that maps escape time values to a repeating set
 * of RGB values. The palette objects are most easily created by taking a
 * far more easily defined spectrum object and then sampling at a
 * particular rate, which becomes the period.
 *
 * @author Jeff Niu
 */
public class Palette implements ColorMap {

    private final int[] palette;
    private final int P;

    /**
     * Create a color Palette from an already-defined color Spectrum. It is
     * far easier to generate a Palette from a color Spectrum than to do so
     * manually.
     *
     * @param sp the Spectrum from which to create this Palette
     * @param P analogous to the maximum iteration, this parameter
     * describes the sample resolution on the spectrum and defines the
     * period over which this palette repeats
     */
    public Palette(Spectrum sp, int P) {
        this.P = P;
        palette = new int[P];
        // Sample the color spectrum
        for (int p = 0; p < P; p++) {
            palette[p] = sp.getRGB(p, P);
        }
    }

    /**
     * Get the RGB value at this escape time.
     *
     * @param t the escape time
     * @param T the maximum escape time (not used)
     * @return the RGB value
     */
    @Override
    public int getRGB(int t, int T) {
        t %= P;
        return palette[t];
    }

    /**
     * Get the color palette
     *
     * @return
     */
    public int[] getPalette() {
        return palette;
    }

    /**
     * Get the period.
     *
     * @return
     */
    public int getPeriod() {
        return P;
    }

}
