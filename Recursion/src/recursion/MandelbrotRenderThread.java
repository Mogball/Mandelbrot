package recursion;

/**
 * A rendering thread for the Mandelbrot set.
 * 
 * @author Jeff Niu
 */
public class MandelbrotRenderThread extends AbstractRenderThread {

    /**
     * Get the render thread.
     * 
     * @param Wi the start x
     * @param Wf the finish x
     * @param N the render dimension
     * @param T the maximum iterations
     * @param values the escape time values
     * @param xc the center x
     * @param yc the center y
     * @param s the zoom scale
     */
    public MandelbrotRenderThread(int Wi, int Wf, int N, int T,
            int[] values, double xc, double yc, double s) {
        super(Wi, Wf, N, T, values, xc, yc, s);
    }

    /**
     * Compute the escape time at a point in the Mandelbrot set.
     * 
     * @param x0 the point x
     * @param y0 the point y
     * @param T the maximum number of iterations
     * @return the escape time value
     */
    @Override
    public int compute(double x0, double y0, int T) {
        double x = 0;
        double y = 0;
        double xSq = x * x;
        double ySq = y * y;
        int t = 0;
        while (xSq + ySq < 4 && t < T) {
            y = x * y;
            y += y;
            y += y0;
            x = xSq - ySq + x0;
            xSq = x * x;
            ySq = y * y;
            t++;
        }
        return t;
    }

}
