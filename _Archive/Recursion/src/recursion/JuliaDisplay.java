package recursion;

/**
 * A display for the Julia Set.
 *
 * @author Jeff Niu
 */
public @SuppressWarnings("serial")
class JuliaDisplay extends AbstractDisplay {

    /**
     * The (x,y) for the Julia argument c.
     */
    private double cx, cy;

    /**
     * Create the Julia display.
     *
     * @param sim the Julia simulator
     * @param N the render dimension
     * @param cx the argument x
     * @param cy the argument y
     */
    public JuliaDisplay(AbstractSimulator sim, int N,
            double cx, double cy) {
        super(sim, N);
        this.cx = cx;
        this.cy = cy;
    }

    /**
     * Alter the Julia parameter.
     *
     * @param cx the new x
     * @param cy the new y
     */
    public void changeJuliaParameter(double cx, double cy) {
        this.cx = cx;
        this.cy = cy;
        render();
    }

    /**
     * Get the Julia renderer.
     *
     * @param N the render dimension
     * @param T the maximum iterations
     * @return the Julia renderer
     */
    @Override
    public AbstractRenderer getRenderer(int N, int T) {
        return new JuliaRenderer(N, T, cx, cy);
    }

}
