package recursion;

import java.awt.image.BufferedImage;

/**
 * A fractal renderer that will render a fractal pixel by pixel.
 *
 * @author Jeff Niu
 */
public abstract class AbstractRenderer {

    /**
     * The number of threads to use. My computer has eight virtual cores,
     * so I set this to eight.
     */
    private static final int NUM_THREADS = 8;

    /**
     * The render parameters. N is the render dimension and T is the
     * maximum number of iterations.
     */
    private final int N, T;

    /**
     * Create a new fractal renderer.
     *
     * @param N the render dimension
     * @param T the maximum number of iterations
     */
    public AbstractRenderer(int N, int T) {
        int b = N % NUM_THREADS;
        // Ensure that the render dimension can be split into the correct
        // number of threads
        if (b != 0) {
            N += b;
        }
        this.N = N;
        this.T = T;
    }

    /**
     * Take a set of escape time values and map it to a buffered image
     * using a particular coloring method.
     *
     * @param img the image to which the colors will be mapped
     * @param cm the color map to use
     * @param values the escape time values
     * @return a colored buffered image
     */
    private BufferedImage color(BufferedImage img, ColorMap cm,
            int[] values) {
        int[] rgb = new int[values.length];
        for (int g = 0; g < values.length; g++) {
            rgb[g] = cm.getRGB(values[g], T);
        }
        for (int r = 0; r < N; r++) {
            for (int i = 0; i < N; i++) {
                img.setRGB(r, i, rgb[r * N + i]);
            }
        }
        return img;
    }

    /**
     * Render a fractal.
     *
     * @param img the image to which to render
     * @param cm the color map to use
     * @param xc the center x
     * @param yc the center y
     * @param s the zoom scale
     */
    public void render(BufferedImage img, ColorMap cm,
            double xc, double yc, double s) {
        // Create the render threads
        AbstractRenderThread[] threads
                = new AbstractRenderThread[NUM_THREADS];
        int[] values = new int[N * N]; // escape time values
        int b = N / NUM_THREADS; // strip width
        int Wi = 0, Wf = b;
        try {
            for (int t = 0; t < NUM_THREADS; t++) {
                threads[t] = getRenderThread(Wi, Wf, N, T, values,
                        xc, yc, s);
                threads[t].start();
                threads[t].join();
                Wi += b;
                Wf += b;
            }
        } catch (InterruptedException ex) {
            // If something happens to interrupt the rendering,
            // simply color what we have and perhaps deal with the 
            // black banding that will occur
        }
        // Color the image with the values
        color(img, cm, values);
    }

    /**
     * Get the render thread specific to the fractal that will be rendered.
     *
     * @param Wi the start x
     * @param Wf the finish x
     * @param N the render dimension
     * @param T the maximum iterations
     * @param values the array of escape time values
     * @param xc the center x
     * @param yc the center y
     * @param s the zoom scale
     * @return a render thread
     */
    public abstract AbstractRenderThread getRenderThread(int Wi, int Wf,
            int N, int T, int[] values, double xc, double yc, double s);

}
