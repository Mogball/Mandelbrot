package recursion;

/**
 * A Tile represents one piece in the Towers of Hanoi puzzle. Each tile has
 * a unique size and a position. The implementation resembles a linked
 * list.
 *
 * @author Jeff Niu
 */
public class Tile {

    /**
     * The unique size of this Tile. No other Tile may have the same size.
     */
    public final int size;

    /**
     * The Tile directly above this tile. A value of null indicates that
     * there is no Tile above this one.
     */
    public Tile next;

    /**
     * This Peg on which this Tile is currently located.
     */
    private Peg currentPeg;

    /**
     * Create a new Tile with a size, a reference to the next Tile, and a
     * reference to the current Peg on which it is stacked.
     *
     * @param size the size of the tile
     * @param next the next Tile
     * @param currentPeg the current Peg
     */
    public Tile(int size, Tile next, Peg currentPeg) {
        this.size = size;
        this.next = next;
        this.currentPeg = currentPeg;
    }

    /**
     * This recursive algorithm will move this Tile to another Peg along
     * with all the Tiles stacked on top.
     *
     * @param targetPeg
     * @param otherPeg
     * @param prev
     */
    public void moveTo(Peg targetPeg, Peg otherPeg, Tile prev, Hanoi han) {
        // If there are Tiles on top of this one, move them
        if (next != null) {
            Peg current = currentPeg; // keep reference to the current Peg
            Tile move = next; // keep reference to the next Tile

            // Get the reference of the Tile on which the Tiles above this
            // Tile will be moved
            Tile nextTop = otherPeg.getTop();

            // Move the stack above this Tile to the other Peg
            move.moveTo(otherPeg, targetPeg, this, han);

            // this.next should be null so move this Tile to the target
            moveTo(targetPeg, null, prev, han);

            // Move the rest of the stack back onto this Tile
            move.moveTo(targetPeg, current, nextTop, han);
        } else {
            han.waitFor();
            
            // Detach this Tile to the one below it or the Peg
            if (prev != null) {
                prev.next = null;
            } else {
                currentPeg.bottom = null;
            }

            // Attach this Tile to the next Peg or the Tile at its top
            Tile nextPrev = targetPeg.getTop();
            if (nextPrev != null) {
                nextPrev.next = this;
            } else {
                targetPeg.bottom = this;
            }
            currentPeg = targetPeg;
            
            han.repaint();
        }
    }
    
    /**
     * Give a String representation of this Tile.
     *
     * @return the size of the Tile
     */
    @Override
    public String toString() {
        return String.valueOf(size);
    }

}
