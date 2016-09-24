package recursion;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This program allows one to simulate the Mandelbrot and Julia sets, a
 * tree fractal, and the Towers of Hanoi.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class Recursion extends JFrame {

    /**
     * Create the main menu.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Recursion rec = new Recursion();
    }

    /**
     * Create the main menu.
     */
    public Recursion() {
        Dimension dim = new Dimension(800, 600);

        JPanel display = (JPanel) getContentPane();
        display.setPreferredSize(dim);
        display.setLayout(null);

        Font titleFont = new Font("Serif", Font.BOLD, 50);
        JLabel title = new JLabel("Fractal Simulator 2K15");
        title.setFont(titleFont);
        title.setBounds(150, 20, 800, 50);
        display.add(title);

        Font buttonFont = new Font("SansSerif", Font.PLAIN, 20);

        // Start Mandelbrot
        JButton mandelbrot = new JButton("Mandelbrot Set");
        mandelbrot.setFont(buttonFont);
        mandelbrot.addActionListener(this::startMandelbrot);
        mandelbrot.setBounds(300, 100, 200, 50);
        display.add(mandelbrot);

        // Start Towers of Hanoi
        JButton hanoi = new JButton("Towers of Hanoi");
        hanoi.setFont(buttonFont);
        hanoi.addActionListener((ActionEvent ae) -> {
            setVisible(false);
            dispose();
            new Hanoi();
        });
        hanoi.setBounds(300, 200, 200, 50);
        display.add(hanoi);

        // Start Tree Fractal
        JButton treeFractal = new JButton("Tree Fractal");
        treeFractal.setFont(buttonFont);
        treeFractal.addActionListener(this::startTreeFractal);
        treeFractal.setBounds(300, 300, 200, 50);
        display.add(treeFractal);

        // Quit
        JButton quit = new JButton("Quit");
        quit.setFont(buttonFont);
        quit.addActionListener(this::quitProgram);
        quit.setBounds(300, 400, 200, 50);
        display.add(quit);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(dim);
        setLayout(null);
        setContentPane(display);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
        setVisible(true);
    }

    /**
     * Close this and create the Mandelbrot Simulator.
     *
     * @param ae
     */
    private void startMandelbrot(ActionEvent ae) {
        setVisible(false);
        dispose();
        MandelbrotSimulator sim = new MandelbrotSimulator();
    }

    /**
     * Close this and create the Tree Fractal Simulator.
     *
     * @param ae
     */
    private void startTreeFractal(ActionEvent ae) {
        setVisible(false);
        dispose();
        TreeFractalSimulator sim = new TreeFractalSimulator();
    }

    /**
     * Quit everything.
     * 
     * @param ae 
     */
    private void quitProgram(ActionEvent ae) {
        Runtime.getRuntime().exit(0);
    }

}
