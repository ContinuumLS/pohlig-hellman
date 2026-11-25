import java.math.*;
import java.security.SecureRandom;
import java.util.ArrayList;

/* TO-DO:
** rewrite modInverse
** rewrite modPow
** rewrite gcd (Euclid Alg)
**
** Might Be Cool:
** potentially write functions to convert string messages of length>1 char to and from integer form?
** write a program to try and fail to crack it - "Describe how Eve can try to break the system through all possible attacks." might best be documented through implementing attacks
*/
public class PohligHellman {
    /* Basic Steps:
     * 1. Given g^x = h (mod p)
     * 2. Calculate n = order(g)
     * 3. Factor R = prime factorization of n
     * 4. For each prime factor r in R:
     *   a. Calculate g^(n/(r^e)) and h^(n/(r^e))
     *   b. Solve (g^(n/(r^e)))^x = h^(n/(r^e))
     *   c. Solution is x (mod r^e)
     * 5. Solve for all x using CRT
     */

    private int intMess;
    private BigInteger prime;
    private BigInteger message;

    // Constructors

    public PohligHellman() {
        SecureRandom rand = new SecureRandom();
        intMess = rand.nextInt(1, 50);
        message = BigInteger.valueOf(intMess);
        prime = generatePrime();
    }

    public PohligHellman(int m) {
        intMess = m;
        message = BigInteger.valueOf(m);
        prime = generatePrime();
    }

    /*public PohligHellman(String m){
        //mess = tostring(m);
        // BigInteger message  = BigInteger.valueOf(m);
        prime = generatePrime();
    }*/

    // Mutators

    public static BigInteger generatePrime() {
        SecureRandom rand = new SecureRandom();
        //BigInteger prime = BigInteger.probablePrime(1024, rand);
        BigInteger prime = generateBigPrimeInt(308);
        return prime;
    }

    public BigInteger generateEnKey() {
        // enKey = n st gcd(enKey, prime-1) = 1

        BigInteger enKey;
        BigInteger candidate = generateBigInteger(100);

        while (!isRelativelyPrime(prime.subtract(BigInteger.ONE), candidate)) {
            candidate = generateBigInteger(100);
        }

        enKey = candidate;
        return enKey;
    }

    // REWRITE OUR OWN VERSION - rewrite modInverse
    public BigInteger generateDeKey(BigInteger enKey) {
        // deKey = n st e * d = 1 % prime

        BigInteger deKey;
        deKey = enKey.modInverse(prime.subtract(BigInteger.ONE));
        return deKey;
    }

    // REWRITE OUR OWN VERSION - rewrite modPow
    public BigInteger encrypt(BigInteger message, BigInteger enKey) {
        BigInteger ciphText = message.modPow(enKey, prime); // REPLACE w/ message.mod(enKey)
        return ciphText;
    }

    // REWRITE OUR OWN VERSION - rewrite modPow
    public BigInteger decrpyt(BigInteger cipherText, BigInteger deKey) {
        BigInteger decMessage = cipherText.modPow(deKey, prime);
        return decMessage;
    }

    public static BigInteger generateBigInteger(int bigIntLength) {
        SecureRandom rand = new SecureRandom();
        BigInteger bigNum = BigInteger.valueOf(rand.nextInt(1, 9));
        for (int i = 0; i < bigIntLength; i++) {
            BigInteger randBig = BigInteger.valueOf(rand.nextInt(0, 10));
            bigNum = bigNum.multiply(BigInteger.TEN.pow(1)).add(randBig);
        }
        bigNum = bigNum.multiply(BigInteger.TEN.pow(1)).add(
            BigInteger.valueOf(2 * (rand.nextInt(0, 5)) + 1));
        return bigNum;
    }

    // NOT WORKING
    public static BigInteger generateBigPrimeInt(int bigIntLength) {
        BigInteger bigPrime = BigInteger.ZERO;
        BigInteger candidate = BigInteger.ZERO;
        boolean probPrime = false;
        boolean testResult;

        while (probPrime != true) {
            candidate = generateBigInteger(bigIntLength);
            PrimalityTest pt = new PrimalityTest(candidate);
            testResult = pt.test(10);
            if (testResult == true) {
                bigPrime = candidate;
                probPrime = true;
            }
        }

        return bigPrime;
    }

    // REWRITE OUR OWN VERSION - rewrite gcd
    public boolean isRelativelyPrime(BigInteger x, BigInteger y) {
        // Euclidean Algorithm
        // We want to make this a safe prime probably
        BigInteger a;
        BigInteger b;
        if (x.compareTo(y) > 0) {
            a = x;
            b = y;
        } else if (x.equals(y)) {
            return false;
        } else {
            a = y;
            b = x;
        }

        return (a.gcd(b)).equals(BigInteger.ONE);
        // return this.gcd(x, y).equals(BigInteger.ONE);
    }

    // Observers

    public int getIntMessage() {
        return intMess;
    }

    public BigInteger getMessage() {
        return message;
    }

    public BigInteger getPrime() {
        return prime;
    }

    // Util

    private BigInteger gcd(BigInteger a, BigInteger b) {
        return !b.equals(BigInteger.ZERO)
            ? this.gcd(b, a.mod(b))
            : a;
    }

    private BigInteger order(BigInteger a, BigInteger n) {
        // Basic Assertion: gcd(a,n) != 1 => a,n are not coprime
        if (!this.isRelativelyPrime(a, n)) {
            throw new ArithmeticException("a and n must be coprime (1)");
        }

        BigInteger res = BigInteger.ONE;
        BigInteger k = BigInteger.ONE;

        while (k.compareTo(n) < 0) { // k < n
            res = res.multiply(a).mod(n);
            if (res.equals(BigInteger.ONE)) {
                return k;
            }
            k = k.add(BigInteger.ONE);
        }

        throw new ArithmeticException("a and n must be coprime (2)");
    }

    /**
     * Returns a 2D array consisting of the prime factorization of n.
     *
     * Each element in the array is of the convention [prime, exponent]
     * @param n the BigInteger to factor
     * @return an array of prime-exponent pairs that make up n's prime
     *         factorization
     */
    private int[][] factorize(BigInteger n) {
        ArrayList<Integer[]> factors = new ArrayList<>();
        while (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            if (factors.isEmpty()) {
                factors.add(new Integer[]{2, 1});
            } else {
                factors.getLast()[1]++;
            }
            n = n.divide(BigInteger.TWO);
        }

        // JC: Fingers crossed that this doesn't crash. BigInteger objects are
        // a memory cost. Would like to avoid polluting memory with them if
        // possible.
        for (int i = 3; i * i <= n.intValueExact(); i += 2) {
            while (n.mod(new BigInteger(""+i)).equals(BigInteger.ZERO)) {
                if (factors.getLast()[0] != i) {
                    factors.add(new Integer[]{i, 1});
                } else {
                    factors.getLast()[1]++;
                }
                n = n.divide(new BigInteger(""+i));
            }
        }

        if (n.compareTo(BigInteger.TWO) > 0) {
            factors.add(new Integer[]{n.intValueExact(), 1});
        }

        return (int[][]) factors.toArray();
    }
}
