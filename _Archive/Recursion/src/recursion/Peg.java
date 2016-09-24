package recursion;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 * A Peg represents the positions in the Towers of Hanoi problem. There
 * must always be exactly three Pegs P<sub>0</sub>, P<sub>1</sub>,
 * P<sub>2</sub>. The implementation resembles a linked list.
 *
 * @author Jeff Niu
 */
public class Peg {

    private static final Dimension DIMENSION = new Dimension(10, 50);

    /**
     * Get the array of Pegs that represent the initial state of the game.
     *
     * @param startPos the index of the Peg on which the stack starts,
     * which is an integer 0, 1, or 2
     * @param numTiles the number of initial starting Tiles
     * @return an array of Pegs
     */
    public static Peg[] getPegs(int startPos, int numTiles) {
        Peg[] pegs = new Peg[3]; // create the array of three Pegs
        pegs[0] = new Peg(null);
        pegs[1] = new Peg(null);
        pegs[2] = new Peg(null);

        // Add the correct number of Tiles to the starting Peg
        Peg startPeg = pegs[startPos];
        Tile tile = null;
        for (int i = 1; i <= numTiles; i++) {
            tile = new Tile(i, tile, startPeg);
        }
        startPeg.bottom = tile;

        return pegs;
    }

    /**
     * The Tile at the very bottom of the stack on this Peg. A value of
     * null indicates that this Peg is empty.
     */
    public Tile bottom;

    /**
     * Create a new Peg.
     *
     * @param bottom the Tile to be at the bottom of the Peg
     */
    public Peg(Tile bottom) {
        this.bottom = bottom;
    }

    /**
     * Get the Tile at the top of the stack on this Peg.
     *
     * @return the Tile at the top
     */
    public Tile getTop() {
        // If there are no Tiles, return null
        if (bottom == null) {
            return null;
        }

        // Otherwise, return the top Tile
        Tile tile = bottom;
        while (tile.next != null) {
            tile = tile.next;
        }
        return tile;
    }

    /**
     * Draw the tiles on this peg.
     *
     * @param g
     * @param x the bottom x
     * @param y the bottom y
     * @param d the screen dimension
     */
    public void draw(Graphics g, int x, int y, Dimension d) {
        Tile tile = bottom;
        while (tile != null) {
            int width = tile.size * d.width;
            int height = d.height;
            int tx = x - width / 2;
            y -= height;
            g.fillRect(tx, y, width, height);
            tile = tile.next;
        }
    }

    /**
     * Convert this Peg into a String that displays the Tiles currently
     * stacked on this Peg from bottom to top.
     *
     * @return a String representation of the Peg
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Tile tile = bottom;
        while (tile != null) {
            sb.append(tile.size).append(", ");
            tile = tile.next;
        }
        return sb.toString();
    }

}
