package recursion;

import java.util.Hashtable;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A simulator for a Julia set.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class JuliaSimulator extends AbstractSimulator
        implements ChangeListener {

    /**
     * The Julia argument.
     */
    private double cx, cy;

    /**
     * Create a Julia simulator at an initial value for the Julia argument.
     *
     * @param cx the argument x
     * @param cy the argument y
     */
    public JuliaSimulator(double cx, double cy) {
        this.cx = cx;
        this.cy = cy;
        init(cx, cy);
    }

    /**
     * Initialize the GUI
     *
     * @param cx the argument x
     * @param cy the argument y
     */
    private void init(double cx, double cy) {
        // The sliders for the Julia argument
        JLabel centerX = new JLabel("Center x");
        centerX.setBounds(1220, 270, 50, 30);
        add(centerX);
        JSlider xSlider = new JSlider(-2000, 2000, (int) (cx * 1000));
        xSlider.addChangeListener(this);
        xSlider.setMinorTickSpacing(50);
        xSlider.setMajorTickSpacing(200);
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Hashtable<Integer, JLabel> xLabels = new Hashtable<>();
        for (int l = xSlider.getMinimum(); l <= xSlider.getMaximum();
                l += 200) {
            xLabels.put(l, new JLabel(String.format("%.2f", l / 1000.0)));
        }
        xSlider.setLabelTable(xLabels);
        xSlider.setPaintLabels(true);
        xSlider.setPaintTicks(true);
        xSlider.setOrientation(JSlider.VERTICAL);
        xSlider.setBounds(1200, 300, 100, 500);
        xSlider.setName("cx");
        add(xSlider);
        JLabel centerY = new JLabel("Center y");
        centerY.setBounds(1420, 270, 50, 30);
        add(centerY);
        JSlider ySlider = new JSlider(-2000, 2000, (int) (cy * 1000));
        ySlider.addChangeListener(this);
        ySlider.setMinorTickSpacing(50);
        ySlider.setMajorTickSpacing(200);
        ySlider.setLabelTable(xLabels);
        ySlider.setPaintLabels(true);
        ySlider.setPaintTicks(true);
        ySlider.setOrientation(JSlider.VERTICAL);
        ySlider.setBounds(1400, 300, 100, 500);
        ySlider.setName("cy");
        add(ySlider);

        // Initialize the Julia argument
        ((JuliaDisplay) getDisplay()).changeJuliaParameter(cx, cy);
    }

    /**
     * Return the Julia display.
     *
     * @param sim the Julia simulator
     * @param N the render dimension
     * @return the display
     */
    @Override
    public AbstractDisplay getDisplay(AbstractSimulator sim, int N) {
        return new JuliaDisplay(sim, N, cx, cy);
    }

    /**
     * When the slider is moved, change the Julia argument.
     *
     * @param ce
     */
    @Override
    public void stateChanged(ChangeEvent ce) {
        Object source = ce.getSource();
        if (source instanceof JSlider) {
            JSlider slider = (JSlider) source;
            switch (slider.getName()) {
                case "cx":
                    cx = slider.getValue() / 1000.0;
                    break;
                case "cy":
                    cy = slider.getValue() / 1000.0;
                    break;
            }
            // We know for sure that getDisplay() will return a Julia
            // Display since the method getDisplay(...) returns an instance
            // of Julia Display
            ((JuliaDisplay) getDisplay()).changeJuliaParameter(cx, cy);
        }
    }

    /**
     * Do not do anything.
     */
    @Override
    public void back() {
    }

}
