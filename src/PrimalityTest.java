import java.math.BigInteger;
import java.util.Random;

/**
 * An instance of this class represents a wrapper around a BigInteger n to
 * determine whether n is probably prime according to the Miller-Rabin
 * primality algorithm.
 */
public class PrimalityTest {
    // JC: Use seeded RNG value for consistent results
    private static final Random rng = new Random(42);
    private BigInteger n;
    private BigInteger nMinusOne;

    public PrimalityTest(BigInteger n) {
        this.n = n;
        this.nMinusOne = n.subtract(BigInteger.ONE);
    }

    /**
     * Tests if the n in this PrimalityTest is prime where k=4.
     * @return true if probably prime, false if composite
     */
    public boolean test() {
        return this.test(4);
    }

    /**
     * Tests if the n in this PrimalityTest is prime.
     * @param k number of iterations
     * @return true if probably prime, false if composite
     */
    public boolean test(int k) {
        if (n.compareTo(new BigInteger("3")) != 1 ||
            n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false;
        }

        // n - 1 = (2^s)d -> n = 1 + (2^s)d
        // Choose d where d is odd and s >= 1
        BigInteger d = nMinusOne;
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
        }

        // Run Miller-Rabin for k iterations (or until proven composite)
        for (int i = 0; i < k; i++) {
            if (!this.millerRabin(d)) {
                return false;
            }
        }

        return true;
    }

    private boolean millerRabin(BigInteger d) {
        // Choose random a in [2, n-2] (and hope we don't get a repeat)
        BigInteger a;
        do {
            // Assert a >= 2
            a = new BigInteger(n.bitLength(), rng).add(BigInteger.TWO);
        } while (a.compareTo(n.subtract(BigInteger.TWO)) > 0);

        // Calc x = a^d (mod n)
        BigInteger x = a.modPow(d, n);

        // If x = +-1 (mod n) => probably prime
        if (x.equals(BigInteger.ONE) || x.equals(nMinusOne)) {
            return true;
        }

        while (!d.equals(nMinusOne)) {
            // x - x^2 (mod n)
            x = x.pow(2).mod(n);
            d = d.multiply(BigInteger.TWO);

            // x_i = 1 => composite
            // x_i = n - 1 (mod n) => probably prime
            if (x.equals(BigInteger.ONE)) {
                return false;
            } else if (x.equals(nMinusOne)) {
                return true;
            }
        }

        // x_s-1 = n - 1 (mod n) => composite
        return false;
    }
}
