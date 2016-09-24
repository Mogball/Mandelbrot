package recursion;

import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

/**
 * A fractal simulator for the Mandelbrot set.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class MandelbrotSimulator extends AbstractSimulator {

    /**
     * Create the simulator.
     */
    public MandelbrotSimulator() {
        init();
    }

    /**
     * Initialize the GUI.
     */
    private void init() {
        /**
         * Hitting this button will create a filled Julia set at the
         * current center point of the Mandelbrot set.
         */
        JButton julia = new JButton("Create Filled Julia Set");
        julia.setFont(new Font("Serif", Font.PLAIN, 16));
        julia.setBounds(700, 800, 200, 50);
        julia.addActionListener(this::createJulia);
        add(julia);
    }

    /**
     * Create the Julia simulator for this point.
     *
     * @param ae
     */
    private void createJulia(ActionEvent ae) {
        try {
            double cx = getCenterX();
            double cy = getCenterY();
            if (cx < -2.0 || cx > 2.0 || cy < -2.0 || cy > 2.0) {
                output("Invalid Julia parameters.");
                return;
            }
            JuliaSimulator sim = new JuliaSimulator(cx, cy);
        } catch (NumberFormatException ex) {
        }
    }

    /**
     * Get an instance of the Mandelbrot display.
     * 
     * @param sim the simulator
     * @param N the render dimension
     * @return a brand new Mandelbrot display
     */
    @Override
    public AbstractDisplay getDisplay(AbstractSimulator sim, int N) {
        return new MandelbrotDisplay(sim, N);
    }

    /**
     * Create a new Recursion menu.
     */
    @Override
    public void back() {
        Recursion rec = new Recursion();
    }

}
