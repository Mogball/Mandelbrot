package recursion;

/**
 * A renderer for the Julia set.
 *
 * @author Jeff Niu
 */
public class JuliaRenderer extends AbstractRenderer {

    /**
     * The Julia argument.
     */
    private final double cx, cy;

    /**
     * Create a Julia renderer.
     * 
     * @param N the rendering dimension
     * @param T the maximum number of iterations
     * @param cx the argument x
     * @param cy the argument y
     */
    public JuliaRenderer(int N, int T, double cx, double cy) {
        super(N, T);
        this.cx = cx;
        this.cy = cy;
    }

    /**
     * Render a Julia render thread.
     * 
     * @param Wi the start x
     * @param Wf the finish x
     * @param N the rendering dimension
     * @param T the maximum iterations
     * @param values the escape time values
     * @param xc the center x
     * @param yc the center y
     * @param s the zoom scale
     * @return a render thread
     */
    @Override
    public AbstractRenderThread getRenderThread(int Wi, int Wf, int N,
            int T, int[] values, double xc, double yc, double s) {
        return new JuliaRenderThread(Wi, Wf, N, T, values,
                xc, yc, s, cx, cy);
    }

}
