package recursion;

import static java.lang.Math.atan2;
import static java.lang.Math.hypot;

/**
 * This class represents a complex number z = a + bi. This is not used
 * because it is more efficient to use primitives directly.
 *
 * @author Jeff Niu
 */
public class Complex {

    /**
     * The real part.
     */
    private final double re;
    /**
     * The imaginary part.
     */
    private final double im;

    /**
     * Create a new complex number.
     *
     * @param re the real part
     * @param im the imaginary part
     */
    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    /**
     * @return the magnitude
     */
    public double abs() {
        return hypot(re, im);
    }

    /**
     * Add two complex numbers.
     *
     * @param z the number to add
     * @return the sum
     */
    public Complex plus(Complex z) {
        double real = this.re + z.re;
        double imag = this.im + z.im;
        return new Complex(real, imag);
    }

    /**
     * Subtract two complex numbers.
     *
     * @param z the number to subtract
     * @return the difference
     */
    public Complex minus(Complex z) {
        double real = this.re - z.re;
        double imag = this.im - z.im;
        return new Complex(real, imag);
    }

    /**
     * Multiply two complex numbers.
     *
     * @param z the number to multiply
     * @return the product
     */
    public Complex times(Complex z) {
        double real = this.re * z.re - this.im * z.im;
        double imag = this.re * z.im + this.im * z.re;
        return new Complex(real, imag);
    }

    /**
     * Scalar multiplication.
     *
     * @param r a scalar
     * @return the product
     */
    public Complex times(double r) {
        return new Complex(r * re, r * im);
    }

    /**
     * @return the conjugate
     */
    public Complex conj() {
        return new Complex(re, -im);
    }

    /**
     * @return the reciprocal
     */
    public Complex recip() {
        double s = re * re + im * im;
        return new Complex(re / s, -im / s);
    }

    /**
     * Raise a complex number to a power. Can be used in multibrot sets
     * though it is very slow.
     *
     * @param z the base
     * @param a the exponent
     * @return the resultant number
     */
    public static Complex pow(Complex z, int a) {
        if (a == 0) {
            return new Complex(1, 0);
        } else if (a > 0) {
            Complex e = z;
            for (int i = 1; i < a; i++) {
                e = e.times(z);
            }
            return e;
        } else {
            return pow(z.recip(), -a);
        }
    }

    /**
     * @return the real part
     */
    public double re() {
        return re;
    }

    /**
     * @return the imaginary part
     */
    public double im() {
        return im;
    }

    /**
     * @return the complex argument
     */
    public double arg() {
        return atan2(im, re);
    }

}
