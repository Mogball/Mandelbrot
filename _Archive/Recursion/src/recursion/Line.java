package recursion;

import java.awt.Polygon;
import static java.lang.Math.round;

/**
 * A line represents two points.
 *
 * @author Jeff Niu
 */
@SuppressWarnings("serial")
public class Line extends Polygon {

    /**
     * Create a line at two points.
     * 
     * @param x the first point x
     * @param y the first point y
     * @param x1 the second point x
     * @param y1 the second point y
     */
    Line(double x, double y, double x1, double y1) {
        addPoint((int) round(x), (int) round(y));
        addPoint((int) round(x1), (int) round(y1));
    }

}
