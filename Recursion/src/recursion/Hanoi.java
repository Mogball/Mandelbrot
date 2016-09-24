package recursion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This program will implement a recursive algorithm that solves the Towers
 * of Hanoi problem.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class Hanoi extends JPanel {

    /**
     * In the main method, the input configuration of the Towers of Hanoi
     * program is accepted.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Hanoi();
    }

    /**
     * If two pegs are selected, this method returns the other peg.
     *
     * @param startPeg the start peg
     * @param targetPeg the move to peg
     * @return the other peg
     */
    private static int getOtherPeg(int startPeg, int targetPeg) {
        List<Integer> pegs = new ArrayList<>();
        pegs.add(0);
        pegs.add(1);
        pegs.add(2);
        pegs.remove((Integer) startPeg);
        pegs.remove((Integer) targetPeg);
        return pegs.get(0);
    }

    /**
     * Get the integer value from a text field.
     *
     * @param field the text field
     * @param def the default value if something goes wrong
     * @return the integer value
     */
    private static int getInt(JTextField field, int def) {
        int i;
        try {
            i = Integer.parseInt(field.getText());
        } catch (NumberFormatException ex) {
            i = def;
            field.setText(String.valueOf(def));
        }
        return i;
    }

    /**
     * The pegs in the problem.
     */
    private Peg[] pegs;
    /**
     * The window dimension.
     */
    private Dimension d;
    /**
     * The animation delay.
     */
    private int delay;
    /**
     * The text fields for entering the parameters.
     */
    private JTextField startField, targetField, numTilesField;

    /**
     * Create the Towers of Hanoi simulator.
     */
    public Hanoi() {
        init();
    }

    /**
     * Initiate the GUI.
     */
    private void init() {
        JFrame sim = new JFrame("Towers of Hanoi");
        Dimension dim = new Dimension(1600, 900);
        setPreferredSize(dim);
        setLayout(null);

        Font font = new Font("sansSerif", Font.PLAIN, 18);

        // Create the fields
        startField = new JTextField("0");
        startField.setFont(font);
        startField.setBounds(50, 50, 100, 30);
        add(startField);
        targetField = new JTextField("2");
        targetField.setFont(font);
        targetField.setBounds(50, 100, 100, 30);
        add(targetField);
        JLabel n = new JLabel("n = ");
        n.setFont(font);
        n.setBounds(20, 150, 40, 30);
        add(n);
        numTilesField = new JTextField("20");
        numTilesField.setFont(font);
        numTilesField.setBounds(50, 150, 100, 30);
        add(numTilesField);

        // This button will initiate the animation
        JButton solve = new JButton("Solve");
        solve.setFont(font);
        solve.setBounds(50, 200, 100, 30);
        add(solve);
        solve.addActionListener(this::solve);

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

        sim.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sim.setSize(dim);
        sim.setLayout(null);
        sim.setContentPane(this);
        sim.setLocationRelativeTo(null);
        sim.setResizable(false);
        sim.pack();
        sim.setVisible(true);
    }

    /**
     * Start the animation.
     *
     * @param ae
     */
    private void solve(ActionEvent ae) {
        // The solving thread so that the animation is updated.
        Thread thread = new Thread(() -> {
            int startPeg = getInt(startField, 0);
            int targetPeg = getInt(targetField, 2);
            int numTiles = getInt(numTilesField, 20);
            int otherPeg = getOtherPeg(startPeg, targetPeg);
            pegs = Peg.getPegs(startPeg, numTiles);
            // Calculate the tile width
            double width = 500.0 / (numTiles + 1);
            d = new Dimension((int) width, (int) width);
            repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            // Calculate the delay
            delay = (int) (10_000 / pow(2, numTiles));

            // Move the tower
            pegs[startPeg].bottom.moveTo(pegs[targetPeg], pegs[otherPeg],
                    null, Hanoi.this);
        });
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.start();
    }

    /**
     * Paint the tiles.
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        if (pegs != null) {
            int x = 300;
            int y = 800;
            for (Peg peg : pegs) {
                peg.draw(g, x, y, d);
                x += 500;
            }
        }
    }

    /**
     * Wait for the delay.
     */
    public void waitFor() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ex) {

        }
    }

}
