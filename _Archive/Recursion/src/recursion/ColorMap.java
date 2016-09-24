package recursion;

/**
 * A color map is something that can map an escape time value to an RGB
 * value.
 *
 * @author Jeff Niu
 */
public interface ColorMap {

    /**
     * Get the RGB value of an escape time value.
     *
     * @param t the escape time
     * @param T the maximum escape time
     * @return an RGB value
     */
    public int getRGB(int t, int T);

}
