package recursion;

import java.awt.Dimension;
import java.awt.Graphics;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

/**
 * A Tree Fractal object represents a tree fractal, made by branching two
 * branches from one at a particular angle with a reduced size.
 *
 * @author Jeff Niu
 */
public class TreeFractal {

    /**
     * Obtain the height of a tree fractal based on its parameters. Note
     * that this height represents the vertical distance from the fractal
     * origin to the top of the tree. It is possible that the complete
     * height of the tree, from its lowest point to its highest, may be
     * larger if some of the branches exceed the trunk. The assumption of
     * this formula is that the center-most branch will indeed the largest
     * height and the formula is based on the sum of a convergent geometric
     * sequence. This and the following situations are modelled
     * heuristically (i.e. no rigorous proof -- gut feeling kind of deal)
     * and the mind-set is one in which we perceive the branches to be in a
     * race to climb the highest.
     *
     * @param l0 the length of the trunk
     * @param r the size factor
     * @param d0 the change in angle
     * @return the height of the tree
     */
    public static double getPrimaryHeight(double r, double d0) {
        double H0 = (1 + r * cos(d0)) / (1 - r * r);
        return H0;
    }

    /**
     * Obtain the height of the first secondary branch. Depending on the
     * parameters, the height of the secondary branches may converge above
     * the height of the primary branch, in which case we obtain the
     * "triangle" effect. This branch will always converge above all
     * similar branches. This calculation is based on the assumption that a
     * particular branch maximizes its vertical climb by oscillating about
     * the 0 degree mark.
     *
     * @param l0 the trunk height
     * @param r the size factor
     * @param d0 the change in angle
     * @return the branch height
     */
    public static double getSecondaryHeight(double r, double d0) {
        // Avoid dividing by zero
        if (d0 % (2 * PI) == 0) {
            return 0; // in this case, H1 < H0 always
        }
        d0 %= 2 * PI;
        if (d0 > PI) {
            d0 = 2 * PI - d0;
        }
        int a = (int) floor(2 * PI / d0);
        double D0 = 0.0;
        for (int i = 0; i < a; i++) {
            D0 += pow(r, i) * cos(i * d0);
        }
        double D12 = pow(r, a) * (cos(a * d0)
                + r * cos((a + 1) * d0)) / (1 - r * r);
        double H1 = (D0 + D12);
        return H1;
    }

    /**
     * This method will obtain the error in measurement from the first
     * method {@link #getHeight()}. If the bottommost branch exceeds the
     * trunk, then this method will return a negative number that
     * represents that error. Otherwise, it will return zero. Like the
     * previous method, the assumption is made that the branch will
     * maximize its height by oscillating about the 0 degree mark (cardinal
     * angles).
     *
     * @param l0 the trunk height
     * @param r the size factor
     * @param d0 the angle factor
     * @return the negative offset if there is one or else zero
     */
    public static double getTertiaryHeight(double r, double d0) {
        // Avoid dividing by zero
        if (d0 % (2 * PI) == 0) {
            return 0; // in this case, the branches will never go below
        }
        d0 %= 2 * PI;
        if (d0 > PI) {
            d0 = 2 * PI - d0;
        }
        int a = (int) floor(PI / d0);
        double D0 = 0.0;
        for (int i = 0; i < a; i++) {
            D0 += pow(r, i) * cos(i * d0);
        }
        double D12 = pow(r, a) * (cos(a * d0)
                + r * cos((a + 1) * d0)) / (1 - r * r);
        double H2 = (D0 + D12);
        return H2;
    }

    /**
     * Obtain the width of a tree fractal based on its parameters. Here, we
     * calculate half of the tree width (since the tree is symmetrical) by
     * finding the maximum distance a branch may travel to the right, in
     * which case the branch would oscillate about the 90 degree mark.
     *
     * @param l0 the truck length
     * @param r the size factor
     * @param d0 the change in angle
     * @return the width of the three
     */
    public static double getHalfWidth(double r, double d0) {
        if (d0 % (2 * PI) == 0) {
            return 0;
        }
        d0 %= 2 * PI;
        int a = (int) floor(PI / 2 / d0);
        double D0 = 0.0;
        for (int i = 0; i < a; i++) {
            D0 += pow(r, i) * sin(i * d0);
        }
        double D12 = pow(r, a) * (sin(a * d0)
                + r * sin((a + 1) * d0)) / (1 - r * r);
        double W = D0 + D12;
        return abs(W);
    }

    /**
     * A static method that returns a tree fractal rendered to a particular
     * depth with specified parameters and that fits into a certain box.
     * This method facilitates better interface between this class and the
     * tree fractal simulator.
     *
     * @param depth the desired render depth
     * @param screen the screen size
     * @param r the size factor (r = ratio)
     * @param d0 the angle factor (d0 = delta-theta)
     * @return a tree fractal that fits in the screen
     */
    public static TreeFractal create(int depth, Dimension screen,
            double r, double d0) {
        // Grab the height factors of all the heights
        double H0 = getPrimaryHeight(r, d0);
        double H1 = getSecondaryHeight(r, d0);
        double H2 = getTertiaryHeight(r, d0);

        // Decide whether the offset is needed
        double HT;
        double H01 = max(1, max(H0, H1));
        if (H2 > 0) {
            H2 = 0;
        }

        // Calculate the overall height
        HT = H01 - H2;

        // Calculate the width
        double W = 2 * getHalfWidth(r, d0);

        // Determine a suitable trunk length
        double length = min(screen.width / W, screen.height / HT);

        // Initialize the angle
        double angle = PI / 2;

        // Set the tree position to the middle of the screen
        double x = screen.width / 2;
        double y = length * H01;

        // Create the Tree Fractal
        TreeFractal fractal = new TreeFractal(depth, x, y,
                length, angle,
                r, d0);
        return fractal;
    }

    /**
     * The current depth of the tree. For the initial trunk, the depth
     * represents the total render depth. When the depth reaches zero, no
     * further branches are created.
     */
    private final int depth;

    /**
     * The origin coordinates of the tree. The trunk will rise vertically
     * from this point.
     */
    private final double x, y;

    /**
     * The current absolute length of the branch and angle relative to the
     * trunk.
     */
    private final double length, angle;

    /**
     * The size factor (r = ratio) and angle factor (d0 = delta-theta).
     */
    private final double r, d0;

    /**
     * A Line that represents the current branch graphically.
     */
    private Line branch;

    /**
     * The two branches that originate from the endpoint of this branch, if
     * they exist.
     */
    private final TreeFractal[] branches;

    /**
     * Create a tree fractal branch.
     *
     * @param depth the current depth of the branch
     * @param x the origin x-coordinate of the branch
     * @param y the origin y-coordinate of the branch
     * @param length the length of the new branch
     * @param angle the angle of the new change
     * @param r the size factor
     * @param d0 the angle factor
     */
    private TreeFractal(int depth, double x, double y,
            double length, double angle,
            double r, double d0) {
        this.depth = depth;
        this.x = x;
        this.y = y;
        this.length = length;
        this.angle = angle;
        this.r = r;
        this.d0 = d0;

        branches = new TreeFractal[2];

        // Generate this branch and next ones if necessary
        generate();
    }

    /**
     *
     */
    private void generate() {
        // Calculate the branch endpoint and the new branch origin point
        double x1 = x - (int) (cos(angle) * length);
        double y1 = y - (int) (sin(angle) * length);

        // Set the line object for later drawing
        branch = new Line(x, y, x1, y1);

        // If we have not reached depth zero, continue creating branches
        if (depth > 0) {
            branches[0] = new TreeFractal(depth - 1, x1, y1,
                    length * r, angle + d0,
                    r, d0);
            branches[1] = new TreeFractal(depth - 1, x1, y1,
                    length * r, angle - d0,
                    r, d0);
        }
    }

    /**
     * Draw the tree fractal.
     * 
     * @param g the Graphics to which to draw
     */
    public void draw(Graphics g) {
        // Draw this branch
        g.drawPolygon(branch);

        // If sub-branches exist, draw them too
        if (branches[0] != null) {
            branches[0].draw(g);
            branches[1].draw(g);
        }
    }

}
