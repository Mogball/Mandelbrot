package recursion;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * An abstract display displays a fractal that can be rendered pixel by
 * pixel.
 *
 * @author Jeff Niu
 */
public @SuppressWarnings("serial")
abstract class AbstractDisplay extends JPanel implements
        MouseListener,
        MouseWheelListener {

    /**
     * The initial zoom on the fractal.
     */
    public static final double START_ZOOM = 4.0;
    /**
     * The initial max iterations.
     */
    public static final int START_T = 256;

    /**
     * The simulator for this fractal.
     */
    private final AbstractSimulator sim;

    /**
     * The dimension of the display.
     */
    private final int N;

    /**
     * The current maximum number of iterations.
     */
    private int T;

    /**
     * The current zoom scale.
     */
    private double s;
    /**
     * The current center x.
     */
    private double xc;
    /**
     * The current center y.
     */
    private double yc;

    /**
     * Whether the renderer is active.
     */
    private volatile boolean rendering;

    /**
     * The color map used to color the fractal.
     */
    private ColorMap cm;

    /**
     * The image that represents the display.
     */
    private final BufferedImage img;

    /**
     * Create a new display.
     *
     * @param sim the fractal simulator
     * @param N the dimension of the display (N by N)
     */
    public AbstractDisplay(AbstractSimulator sim, int N) {
        this.sim = sim;
        this.N = N;

        // Create the buffered image
        img = new BufferedImage(N, N, BufferedImage.TYPE_INT_RGB);

        // Initialize the fractal parameters
        xc = 0.0;
        yc = 0.0;
        s = START_ZOOM;
        T = 256;

        // Set the default color palette
        cm = new Palette(Spectrum.GreyScale, 256);

        init();
    }

    public void changeParameters(double xc, double yc,
            double s, int T) {
        this.xc = xc;
        this.yc = yc;
        this.s = s;
        this.T = T;
        render();
    }

    /**
     * Change the fractal parameters.
     *
     * @param xc the new center x
     * @param yc the new center y
     * @param s the new zoom scale
     * @param T the new maximum iterations
     * @param cm the new color map
     */
    public void changeParameters(double xc, double yc,
            double s, int T,
            ColorMap cm) {
        this.cm = cm;
        changeParameters(xc, yc, s, T);
    }

    /**
     * Update the simulator with the changed fractal parameters.
     */
    public void updateParameters() {
        sim.updateParameters(xc, yc, s, T);
    }

    /**
     * Initialize the GUI of the display.
     */
    private void init() {
        setPreferredSize(new Dimension(N, N));
        addMouseListener(this);
        addMouseWheelListener(this);

        // Perform the first render
        render();
    }

    /**
     * Render the fractal.
     */
    protected void render() {
        rendering = true;

        // Get the correct renderer and then render the fractal
        AbstractRenderer renderer = getRenderer(N, T);
        renderer.render(img, cm, xc, yc, s);
        // Update the parameters to the simulator and repaint the display
        updateParameters();
        repaint();

        rendering = false;
    }

    /**
     * This method will return a fractal renderer particular to the fractal
     * that will be rendered.
     *
     * @param N the rendering dimension N by N
     * @param T the maximum number of iterations
     * @return a fractal renderer
     */
    public abstract AbstractRenderer getRenderer(int N, int T);

    /**
     * Paint the display as the fractal image.
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    /**
     * Respond to mouse clicking. Left click re-centers the display on the
     * point clicked. Right click zooms in by a factor of five.
     *
     * @param me
     */
    @Override
    public void mouseClicked(MouseEvent me) {
        if (!rendering) {
            if (me.getButton() == MouseEvent.BUTTON1) {
                // Left mouse button recenters screen on mouse
                Point p = me.getPoint();
                xc += (p.x - N / 2) * s / N;
                yc += (p.y - N / 2) * s / N;
            } else if (me.getButton() == MouseEvent.BUTTON3) {
                // Right mouse button zooms in magnification M = 5
                s /= 5;
            }
            render();
        }
    }

    /**
     * When the user moves the mouse wheel, zoom in or out.
     *
     * @param mwe
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent mwe) {
        int dn = mwe.getWheelRotation();
        if (dn > 0) {
            // Zoom out
            s *= 2;
        } else {
            // Zoom in
            s /= 2;
        }
        render();
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

}
