package recursion;

/**
 * Display for the Mandelbrot Set.
 * 
 * @author Jeff Niu
 */
public @SuppressWarnings("serial")
class MandelbrotDisplay extends AbstractDisplay {

    /**
     * Create the display.
     * 
     * @param sim the Mandelbrot simulator.
     * @param N the render dimension 
     */
    public MandelbrotDisplay(AbstractSimulator sim, int N) {
        super(sim, N);
    }

    /**
     * Get the Mandelbrot renderer.
     * 
     * @param N the render dimension
     * @param T the maximum iterations
     * @return 
     */
    @Override
    public AbstractRenderer getRenderer(int N, int T) {
        return new MandelbrotRenderer(N, T);
    }
}
