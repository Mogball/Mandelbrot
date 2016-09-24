package recursion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * This class handles the display of a tree fractal.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class TreeFractalDisplay extends JPanel {

    /**
     * The tree fractal to display.
     */
    private TreeFractal f;

    /**
     * The size of the display zone.
     */
    private final Dimension size;

    /**
     * Create a new Tree Fractal Display with a particular size.
     *
     * @param screen the desired size
     */
    public TreeFractalDisplay(Dimension screen) {
        // Set up the display
        this.size = screen;
        setPreferredSize(screen);
        setLayout(null);
    }

    /**
     * Get the size of this display.
     *
     * @return this display's size
     */
    @Override
    public Dimension getSize() {
        return size;
    }

    /**
     * Set the tree fractal to display.
     *
     * @param f
     */
    public void setFractal(TreeFractal f) {
        this.f = f;
    }

    /**
     * Override the paint method to paint a white background and then the
     * tree fractal.
     *
     * @param g the Graphics to draw to
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw white background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size.width, size.height);

        // Draw the tree if it exists
        g.setColor(Color.BLACK);
        if (f != null) {
            f.draw(g);
        }
    }

}
