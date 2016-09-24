package recursion;

/**
 * A renderer for the Mandelbrot set.
 * 
 * @author Jeff Niu
 */
public class MandelbrotRenderer extends AbstractRenderer {

    /**
     * Create a new Mandelbrot renderer.
     * 
     * @param N the render dimension
     * @param T the maximum iterations
     */
    public MandelbrotRenderer(int N, int T) {
        super(N, T);
    }

    /**
     * Get a render thread for a particular vertical strip.
     * 
     * @param Wi the start x
     * @param Wf the start y
     * @param N the render dimension
     * @param T the maximum number of iterations
     * @param values the escape time values
     * @param xc the center x
     * @param yc the center y
     * @param s the zoom scale 
     * @return a render thread
     */
    @Override
    public AbstractRenderThread getRenderThread(int Wi, int Wf, int N,
            int T, int[] values, double xc, double yc, double s) {
        return new MandelbrotRenderThread(Wi, Wf, N, T, values, xc, yc, s);
    }
    
}
