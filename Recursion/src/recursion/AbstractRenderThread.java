package recursion;

/**
 * A general rendering thread of a fractal that is computed pixel by pixel.
 *
 * @author Jeff Niu
 */
public abstract class AbstractRenderThread extends Thread {

    /**
     * The render parameters. N is the render dimension, T is the maximum
     * number of iterations, and the rest define the region in which this
     * particular thread renders the fractal.
     */
    private final int Wi, Wf, Hi, Hf, N, T;

    /**
     * An array of resultant values computed at each pixel.
     */
    private final int[] values;

    /**
     * The fractal parameters. (xc, yc) is the center point and s is the
     * zoom scale.
     */
    private final double xc, yc, s;

    /**
     * Create a new render thread. Each thread will render a vertical strip
     * of pixels.
     *
     * @param Wi the start x
     * @param Wf the finish x
     * @param N the render dimension
     * @param T the maximum number of iterations
     * @param values the array to which the computed values will be stored
     * @param xc the center x
     * @param yc the center y
     * @param s the zoom scale
     */
    public AbstractRenderThread(int Wi, int Wf, int N, int T,
            int[] values, double xc, double yc, double s) {
        this.Wi = Wi;
        this.Wf = Wf;
        this.N = N;
        this.T = T;
        this.values = values;
        this.xc = xc;
        this.yc = yc;
        this.s = s;
        Hi = 0;
        Hf = N;
    }

    /**
     * Render the vertical strip.
     */
    @Override
    public void run() {
        for (int r = Wi; r < Wf; r++) {
            for (int i = Hf - 1; i >= Hi; i--) {
                double x0 = xc + s * (r / (double) N - 0.5);
                double y0 = yc + s * (i / (double) N - 0.5);
                values[r * N + i] = T - compute(x0, y0, T);
            }
        }
    }

    /**
     * This algorithm will compute the escape time t at a particular pixel.  
     * 
     * @param x0 the x coordinate
     * @param y0 the y coordinate
     * @param T the maximum number of iterations
     * @return the escape time 
     */
    public abstract int compute(double x0, double y0, int T);

}
