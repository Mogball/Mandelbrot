package recursion;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import static java.lang.Math.PI;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The Tree Fractal Interface handles the Tree Fractal display itself and
 * various sliders that can be used to mess around with the Tree Fractal
 * parameters.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class TreeFractalInterface extends JPanel implements ChangeListener {

    /**
     * A slider to adjust the render depth of the tree from 0 to 20, where
     * 0 renders only the trunk. Be warned that render depths that exceed
     * 15 may cause lag.
     */
    private JSlider depthSlider;
    /**
     * A slider to adjust the relative angles of the branches. Values are
     * stored in integers from 0 to 200 and scaled down to be a number
     * between 0.0 and 2.0, and these numbers represent multiples of pi.
     */
    private JSlider angleSlider;
    /**
     * A slider to adjust the size ratio of each successive branch. Values
     * are integers from 0 to 100 then scaled down to 0.0 to 1.0.
     */
    private JSlider sizeSlider;

    /**
     * The current render depth of the tree.
     */
    private int depth;
    /**
     * The current change in angle of the tree.
     */
    private double angle;
    /**
     * The current size ratio of the tree.
     */
    private double size;

    /**
     * The tree fractal display.
     */
    private final TreeFractalDisplay display;

    /**
     * Create a new user interface.
     *
     * @param screen the screen size
     * @param display the tree fractal display
     */
    public TreeFractalInterface(Dimension screen,
            TreeFractalDisplay display, TreeFractalSimulator sim) {
        this.display = display;

        // Initialize the parameter values
        depth = 10;
        angle = 0.25;
        size = 0.7;

        // Initialize the GUI
        init(screen, sim);
    }

    /**
     * Initialize the GUI components.
     *
     * @param screen the screen size
     */
    private void init(Dimension screen, TreeFractalSimulator sim) {
        setPreferredSize(screen);
        setLayout(null);

        // Set the location of the display
        Dimension dispSize = display.getSize();
        display.setBounds(
                (screen.width - dispSize.width) / 2,
                (screen.height - dispSize.height) / 2,
                1280, 720);
        display.setBorder(BorderFactory.createEtchedBorder(
                EtchedBorder.LOWERED));
        add(display);

        // Create and configure the depth slider
        depthSlider = new JSlider(0, 20, depth);
        depthSlider.addChangeListener(this);
        depthSlider.setMinorTickSpacing(1);
        depthSlider.setMajorTickSpacing(5);
        depthSlider.createStandardLabels(5);
        depthSlider.setPaintLabels(true);
        depthSlider.setPaintTicks(true);
        depthSlider.setOrientation(JSlider.HORIZONTAL);
        depthSlider.setBounds(550, 30, 500, 60);
        depthSlider.setName("Depth");
        JLabel depthSliderLabel = new JLabel("Tree Depth");
        depthSliderLabel.setBounds(550, 0, 100, 40);
        add(depthSlider);
        add(depthSliderLabel);

        // Create and configure the angle factor slider
        // Scale to higher magnitudes to achieve better value resolution
        angleSlider = new JSlider(0, 200, (int) (angle * 100));
        angleSlider.addChangeListener(this);
        angleSlider.setMinorTickSpacing(10);
        angleSlider.setMajorTickSpacing(25);
        // We manually set the labels to be appropriate multiples of pi
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Hashtable<Integer, JLabel> aLabels = new Hashtable<>();
        aLabels.put(0, new JLabel("0"));
        aLabels.put(25, new JLabel("\u03C0/4")); // unicode \u03C0 for pi
        aLabels.put(50, new JLabel("\u03C0/2"));
        aLabels.put(75, new JLabel("3\u03C0/4"));
        aLabels.put(100, new JLabel("\u03C0"));
        aLabels.put(125, new JLabel("5\u03C0/4"));
        aLabels.put(150, new JLabel("3\u03C0/2"));
        aLabels.put(175, new JLabel("7\u03C0/4"));
        aLabels.put(200, new JLabel("2\u03C0"));
        angleSlider.setLabelTable(aLabels);
        angleSlider.setPaintLabels(true);
        angleSlider.setPaintTicks(true);
        angleSlider.setOrientation(JSlider.VERTICAL);
        angleSlider.setBounds(20, 100, 60, 500);
        angleSlider.setName("Angle");
        JLabel angleSliderLabel = new JLabel("Branch Angle");
        angleSliderLabel.setBounds(20, 70, 100, 40);
        add(angleSlider);
        add(angleSliderLabel);

        // Create and configure the size factor slider
        // Scale to higher magnitudes to achieve better value resolution
        sizeSlider = new JSlider(0, 100, (int) (size * 100));
        sizeSlider.addChangeListener(this);
        sizeSlider.setMinorTickSpacing(5);
        sizeSlider.setMajorTickSpacing(10);
        @SuppressWarnings("UseOfObsoleteCollectionType")
        // Manually create the decimal value labels
        Hashtable<Integer, JLabel> sLabels = new Hashtable<>();
        for (int l = sizeSlider.getMinimum(); l <= sizeSlider.getMaximum();
                l += 20) {
            sLabels.put(l, new JLabel(String.format("%.2f", l / 100.0)));
        }
        sizeSlider.setLabelTable(sLabels);
        sizeSlider.setPaintLabels(true);
        sizeSlider.setPaintTicks(true);
        sizeSlider.setOrientation(JSlider.VERTICAL);
        sizeSlider.setBounds(1520, 100, 60, 500);
        sizeSlider.setName("Size");
        JLabel sizeSliderLabel = new JLabel("Size Factor");
        sizeSliderLabel.setBounds(1520, 70, 100, 40);
        add(sizeSlider);
        add(sizeSliderLabel);

        // Return to the main menu
        JButton back = new JButton("Back");
        back.setFont(new Font("SansSerif", Font.PLAIN, 16));
        back.addActionListener((ActionEvent ae) -> {
            sim.setVisible(false);
            sim.dispose();
            Recursion rec = new Recursion();
        });
        back.setBounds(40, 830, 70, 30);
        add(back);

        // Render the initial tree
        renderTree();
    }

    /**
     * This method is called to re-render the tree fractal when a change
     * has been made to the parameters.
     */
    private void renderTree() {
        TreeFractal f = TreeFractal.create(depth, display.getSize(),
                size, angle * PI);
        display.setFractal(f);
        repaint();
    }

    /**
     * This method will be called when a JSlider has been adjusted.
     *
     * @param ce the change event object
     */
    @Override
    public void stateChanged(ChangeEvent ce) {
        // This ChangeListener should only be listening to JSliders
        Object source = ce.getSource();
        if (source instanceof JSlider) {
            JSlider slider = (JSlider) source;
            // Based on the name, perform an action
            switch (slider.getName()) {
                case "Depth":
                    depth = slider.getValue();
                    break;
                case "Angle":
                    angle = slider.getValue() / 100.0; // un-scale
                    break;
                case "Size":
                    size = slider.getValue() / 100.0; // un-scale
                    break;
            }
            // Redraw the tree
            renderTree();
        }
    }

}
