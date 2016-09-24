package recursion;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * In the Tree Fractal Simulator, a user may play around with the various
 * parameters of a Tree Fractals: the angle factor, size factor, and render
 * depth.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class TreeFractalSimulator extends JFrame {

    /**
     * Start a new instance of Tree Fractal Simulator.
     */
    public TreeFractalSimulator() {
        super("TreeFractal");

        // Set up of the interface
        Dimension screen = new Dimension(1600, 900); // window size
        Dimension dispSize = new Dimension(1280, 720); // display size

        // The fractal display
        TreeFractalDisplay display = new TreeFractalDisplay(dispSize);

        // The user interface
        TreeFractalInterface ui = new TreeFractalInterface(
                screen, display, this);

        // Set up the frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screen);
        setLayout(null);
        setContentPane(ui);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
        setVisible(true);
    }

}
