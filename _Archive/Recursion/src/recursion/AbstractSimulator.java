package recursion;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.pow;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import static recursion.AbstractDisplay.START_T;
import static recursion.AbstractDisplay.START_ZOOM;

/**
 * A general fractal simulator. The reason for this "AbstractSomething"
 * design is because the Mandelbrot and Julia Set simulators both have
 * basically identical code, so this is for code re-usability.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public abstract class AbstractSimulator extends JPanel {

    /**
     * The default render dimension.
     */
    private static final int N = 512;
    /**
     * The default color map sample rate.
     */
    private static final int DEFAULT_SAMPLE_RATE = 256;

    /**
     * The fractal display.
     */
    private AbstractDisplay display;

    /**
     * The text fields in which the user may specify the fractal parameters
     * for the center (x,y), the zoom scale, and the maximum iterations.
     */
    private JTextField xField, yField, sField, tField;

    /**
     * The drop-down selection of available color maps.
     */
    private JComboBox<ColorMap> colorMaps;
    /**
     * The text field in which the user may specific the sample rate. A
     * sample rate of zero will dynamically scale the color map to fit the
     * maximum iterations.
     */
    private JTextField rateField;

    /**
     * The text field for the image capture render dimension.
     */
    private JTextField saveNField;
    /**
     * The text field for the image capture maximum iterations.
     */
    private JTextField saveTField;
    /**
     * The file name for the saved image.
     */
    private JTextField saveNameField;
    /**
     * A text area where information may be displayed.
     */
    private JTextArea outputArea;

    /**
     * Create a new fractal simulator; initiate the GUI.
     */
    public AbstractSimulator() {
        init();
    }

    /**
     * Initialize the general GUI components.
     */
    private void init() {
        JFrame frame = new JFrame("Mandelbrot Set");
        Dimension dim = new Dimension(1600, 900); // the window dimensions

        setPreferredSize(dim);
        setLayout(null);

        // A general font
        Font font = new Font("Serif", Font.BOLD, 20);
        JLabel options = new JLabel("Display Options");
        options.setBounds(92, 30, 200, 50);
        options.setFont(font);
        add(options);

        // Parameter labels
        JLabel xc = new JLabel("x = ");
        xc.setBounds(50, 80, 50, 30);
        xc.setFont(font);
        add(xc);
        JLabel yc = new JLabel("y = ");
        yc.setBounds(50, 120, 50, 30);
        yc.setFont(font);
        add(yc);
        JLabel s = new JLabel("s = ");
        s.setBounds(52, 160, 50, 30);
        s.setFont(font);
        add(s);
        JLabel T = new JLabel("T = ");
        T.setBounds(47, 200, 50, 30);
        T.setFont(font);
        add(T);

        // Parameter text fields
        xField = new JTextField();
        xField.setBounds(85, 80, 170, 30);
        add(xField);
        yField = new JTextField();
        yField.setBounds(85, 120, 170, 30);
        add(yField);
        sField = new JTextField();
        sField.setBounds(85, 160, 170, 30);
        add(sField);
        tField = new JTextField();
        tField.setBounds(85, 200, 170, 30);
        add(tField);

        // Pressing the render button will render the fractal
        Font bFont = new Font("Serif", Font.PLAIN, 16);
        JButton renderButton = new JButton("Render");
        renderButton.setFont(bFont);
        renderButton.setBounds(110, 400, 100, 30);
        renderButton.addActionListener(this::changeParameters);
        add(renderButton);

        // Reset to the initial zoom settings 
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(bFont);
        resetButton.setBounds(275, 140, 100, 30);
        resetButton.addActionListener((ActionEvent ae) -> {
            display.changeParameters(0, 0,
                    START_ZOOM, START_T);
        });
        add(resetButton);

        // Return to the previous menu
        JButton back = new JButton("Back");
        back.setFont(new Font("SansSerif", Font.PLAIN, 16));
        back.addActionListener((ActionEvent ae) -> {
            frame.setVisible(false);
            frame.dispose();
            back();
        });
        back.setBounds(40, 830, 70, 30);
        add(back);

        // Selecting the color map
        JLabel colorMapLabel = new JLabel("Color map");
        colorMapLabel.setFont(font);
        colorMapLabel.setBounds(110, 250, 100, 50);
        add(colorMapLabel);
        colorMaps = new JComboBox<>();
        colorMaps.addItem(Spectrum.GreyScale);
        colorMaps.addItem(Spectrum.Rainbow);
        colorMaps.addItem(Spectrum.BlackGoldYellow);
        colorMaps.addItem(Spectrum.BlackYellowPurple);
        colorMaps.addItem(Spectrum.BlackYellowBlue);
        colorMaps.addItem(Spectrum.RedOrange);
        colorMaps.setBounds(85, 300, 170, 30);
        add(colorMaps);
        JLabel sampling = new JLabel("Sample Rate: ");
        sampling.setBounds(85, 335, 100, 30);
        add(sampling);
        rateField = new JTextField();
        rateField.setBounds(165, 335, 70, 30);
        rateField.setText(String.format("%d", DEFAULT_SAMPLE_RATE));
        add(rateField);

        // Get the display and set it to the simulation
        display = getDisplay(this, N);
        display.setBounds((dim.width - N) / 2, (dim.height - N) / 2, N, N);
        display.setBorder(BorderFactory.createEtchedBorder(
                EtchedBorder.RAISED));
        add(display);

        // Labels for the image capture
        JLabel saveTitle = new JLabel("Save Image");
        saveTitle.setFont(font);
        saveTitle.setBounds(1242, 30, 200, 50);
        add(saveTitle);
        JLabel saveN = new JLabel("N = ");
        saveN.setFont(font);
        saveN.setBounds(1200, 80, 50, 30);
        add(saveN);
        JLabel saveT = new JLabel("T = ");
        saveT.setFont(font);
        saveT.setBounds(1200, 120, 50, 30);
        add(saveT);
        JLabel saveName = new JLabel("File Name: ");
        saveName.setBounds(1165, 160, 100, 30);
        add(saveName);

        // The image capture parameters
        saveNField = new JTextField();
        saveNField.setBounds(1235, 80, 110, 30);
        add(saveNField);
        saveTField = new JTextField();
        saveTField.setBounds(1235, 120, 110, 30);
        add(saveTField);
        saveNameField = new JTextField();
        saveNameField.setBounds(1235, 160, 110, 30);
        add(saveNameField);

        // The output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBounds(1360, 80, 200, 110);
        outputArea.setBorder(BorderFactory.createEtchedBorder());
        add(outputArea);

        // Save image button
        JButton save = new JButton("Save Image");
        save.setFont(bFont);
        save.setBounds(1235, 200, 110, 30);
        save.addActionListener(this::saveImage);
        add(save);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(dim);
        frame.setLayout(null);
        frame.setContentPane(this);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Get the display for this fractal.
     *
     * @param sim this simulator
     * @param N the render dimension
     * @return a fractal display
     */
    public abstract AbstractDisplay getDisplay(
            AbstractSimulator sim, int N);

    /**
     * What this particular simulator does upon hitting the back button.
     */
    public abstract void back();

    /**
     * Update the parameters displayed in the text fields.
     *
     * @param xc the center x
     * @param yc the center y
     * @param s the zoom scale
     * @param T the maximum number of iterations
     */
    public void updateParameters(double xc, double yc, double s, int T) {
        xField.setText(String.format("%.20f", xc));
        yField.setText(String.format("%.20f", yc));
        sField.setText(String.format("%.20f", s));
        tField.setText(String.format("%d", T));
    }

    /**
     * Get the current display.
     *
     * @return the fractal display
     */
    public AbstractDisplay getDisplay() {
        return display;
    }

    /**
     * Get the entered center x.
     *
     * @return the center x
     * @throws NumberFormatException when it is not a number
     */
    public double getCenterX() throws NumberFormatException {
        double x;
        try {
            x = Double.parseDouble(xField.getText());
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid center coordinates.");
            throw ex;
        }
        return x;
    }

    /**
     * Get the entered center y.
     *
     * @return the center y
     * @throws NumberFormatException when it is not a number
     */
    public double getCenterY() throws NumberFormatException {
        double y;
        try {
            y = Double.parseDouble(yField.getText());
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid center coordinates.");
            throw ex;
        }
        return y;
    }

    /**
     * Get the entered zoom scale.
     *
     * @return the zoom scale
     * @throws NumberFormatException when it is not a number
     */
    public double getScaleFactor() throws NumberFormatException {
        double s;
        try {
            s = Double.parseDouble(sField.getText());
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid scale factor.");
            throw ex;
        }
        return s;
    }

    /**
     * Get the entered maximum iterations.
     *
     * @return the specified maximum number of iterations
     * @throws NumberFormatException when it is not a positive integer
     */
    public int getMaxIterations() throws NumberFormatException {
        int T;
        try {
            T = Integer.parseInt(tField.getText());
        } catch (NumberFormatException ex) {
            outputArea.setText("Invalid maximum iterations.");
            throw ex;
        }
        return T;
    }

    /**
     * Get the entered color sample rate.
     *
     * @return the sample rate
     */
    public int getColorSampleRate() {
        int rate;
        try {
            rate = Integer.parseInt(rateField.getText());
            if (rate < 0) {
                rate = DEFAULT_SAMPLE_RATE;
            }
        } catch (NumberFormatException ex) {
            rate = DEFAULT_SAMPLE_RATE;
        }
        rateField.setText(String.format("%d", rate));
        return rate;
    }

    /**
     * Get the color map that the user has currently selected.
     *
     * @return the color map
     */
    public ColorMap getSelectedColorMap() {
        int rate = getColorSampleRate();
        int index = colorMaps.getSelectedIndex();
        ColorMap cm = colorMaps.getItemAt(index);
        if (rate > 0 && cm instanceof Spectrum) {
            // Create the palette at the sample rate
            cm = new Palette((Spectrum) cm, rate);
        }
        return cm;
    }

    /**
     * Upon hitting render, the render parameters are changed.
     *
     * @param ae
     */
    private void changeParameters(ActionEvent ae) {
        try {
            double xc = getCenterX();
            double yc = getCenterY();
            double s = getScaleFactor();
            int T = getMaxIterations();
            ColorMap cm = getSelectedColorMap();
            display.changeParameters(xc, yc, s, T, cm);
        } catch (NumberFormatException ex) {
            display.updateParameters();
        }
    }

    /**
     * Display some text to the output area.
     *
     * @param s the text to display
     */
    public void output(String s) {
        outputArea.setText(s);
    }

    /**
     * Save the current image at the specified image capture parameters.
     *
     * @param ae
     */
    private void saveImage(ActionEvent ae) {
        // Get the fractal parameters
        double xc;
        double yc;
        double s;
        ColorMap cm;
        try {
            xc = getCenterX();
            yc = getCenterY();
            s = getScaleFactor();
            cm = getSelectedColorMap();
        } catch (NumberFormatException ex) {
            display.updateParameters();
            outputArea.setText("Failed to parse fractal parameters.");
            return;
        }

        // Get the save parameters
        int saveN;
        int saveT;
        try {
            saveN = Integer.parseInt(saveNField.getText());
            saveT = Integer.parseInt(saveTField.getText());
        } catch (NumberFormatException ex) {
            saveNField.setText(null);
            saveTField.setText(null);
            saveN = -1;
            saveT = -1;
        }

        // Create the save file
        String name = saveNameField.getText().trim() + ".png";
        File f = new File(name);
        try {
            f.createNewFile();
        } catch (IOException ex) {
            saveN = -1;
            saveT = -1;
        }
        
        // At invalid parameters, reset the fields and return
        if (saveN <= 0 || saveT <= 0) {
            saveNField.setText(null);
            saveTField.setText(null);
            saveNameField.setText(null);
            outputArea.setText("Invalid save image parameters.");
            return;
        }
        
        // Create the image
        BufferedImage img = new BufferedImage(saveN, saveN,
                BufferedImage.TYPE_INT_RGB);
        
        // Get the renderer at the save parameters
        AbstractRenderer renderer = display.getRenderer(saveN, saveT);
        long t = System.nanoTime();
        
        // Display some rendering information
        outputArea.setText("Rendering...\n");
        repaint();
        renderer.render(img, cm, xc, yc, s);
        double dt = (System.nanoTime() - t) / pow(10, 9);
        outputArea.append("Rendering complete t = "
                + String.format("%.3f%n%n", dt));
        outputArea.append("Saving image...\n");
        repaint();
        // Save the image
        try {
            ImageIO.write(img, "PNG", f);
        } catch (IOException ex) {
            outputArea.append("Save failed.");
        }
        outputArea.append("Image saved!");
    }

}
