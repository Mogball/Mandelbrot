package recursion;

/**
 * A rendering thread for the Julia set.
 *
 * @author Jeff Niu
 */
public class JuliaRenderThread extends AbstractRenderThread {

    /**
     * The Julia argument.
     */
    private final double cx, cy;

    /**
     * Create the render thread
     *
     * @param Wi the start x
     * @param Wf the finish x
     * @param N the render dimension
     * @param T the maximum number of iterations
     * @param values the escape time values
     * @param xc the center x
     * @param yc the center y
     * @param s the zoom scale
     * @param cx the argument x
     * @param cy the argument y
     */
    public JuliaRenderThread(int Wi, int Wf, int N, int T,
            int[] values, double xc, double yc, double s,
            double cx, double cy) {
        super(Wi, Wf, N, T, values, xc, yc, s);
        this.cx = cx;
        this.cy = cy;
    }

    /**
     * Compute the escape time value of the Julia set of the Mandelbrot set
     * at a particular point.
     *
     * @param x0 the point x
     * @param y0 the point y
     * @param T the maximum escape time
     * @return the escape time
     */
    @Override
    public int compute(double x0, double y0, int T) {
        double x = x0;
        double y = y0;
        double xSq = x * x;
        double ySq = y * y;
        int t = 0;
        while (xSq + ySq < 4 && t < T) {
            y = x * y;
            y += y;
            y += cy;
            x = xSq - ySq + cx;
            xSq = x * x;
            ySq = y * y;
            t++;
        }
        return t;
    }

}
