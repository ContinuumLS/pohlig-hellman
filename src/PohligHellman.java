import java.math.*;
import java.security.SecureRandom;

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
    int intMess;
    BigInteger prime;
    BigInteger message;

    // Constructors

    public PohligHellman() {
        SecureRandom rand = new SecureRandom();
        intMess = rand.nextInt(1,50);
        message = BigInteger.valueOf(intMess);
        prime = generatePrime();
    }

    public PohligHellman(int m){
        intMess = m;
        message  = BigInteger.valueOf(m);
        prime = generatePrime();
    }

    /*public PohligHellman(String m){
        //mess = tostring(m);
        // BigInteger message  = BigInteger.valueOf(m);
        prime = generatePrime();
    }*/

    // Mutators

    public static BigInteger generatePrime(){
        SecureRandom rand = new SecureRandom();
        //BigInteger prime = BigInteger.probablePrime(1024, rand);
        BigInteger prime = generateBigPrimeInt(308);
        return prime;
    }

    public BigInteger generateEnKey(){
        // enKey = n st gcd(enKey, prime-1) = 1

        BigInteger enKey;
        BigInteger candidate = generateBigInteger(100);

        while(!isRelativelyPrime(prime.subtract(BigInteger.ONE), candidate)){
            candidate = generateBigInteger(100);
        }
        
        enKey = candidate;
        return enKey;
    }

    // REWRITE OUR OWN VERSION - rewrite modInverse
    public BigInteger generateDeKey(BigInteger enKey){
        // deKey = n st e * d = 1 % prime

        BigInteger deKey;
        deKey = enKey.modInverse(prime.subtract(BigInteger.ONE));
        return deKey;
    }

    // REWRITE OUR OWN VERSION - rewrite modPow
    public BigInteger encrypt(BigInteger message, BigInteger enKey){
        BigInteger ciphText = message.modPow(enKey, prime); // REPLACE w/ message.mod(enKey)
        return ciphText;
    }

    // REWRITE OUR OWN VERSION - rewrite modPow
    public BigInteger decrpyt(BigInteger cipherText,  BigInteger deKey){
        BigInteger decMessage = cipherText.modPow(deKey, prime);
        return decMessage;
    }

    public static BigInteger generateBigInteger(int bigIntLength){
        SecureRandom rand = new SecureRandom();
        BigInteger bigNum = BigInteger.valueOf(rand.nextInt(1,9));
        for(int i = 0; i<bigIntLength; i++){
            BigInteger randBig = BigInteger.valueOf(rand.nextInt(0,10));
            bigNum = bigNum.multiply(BigInteger.TEN.pow(1)).add(randBig);
        }
        bigNum = bigNum.multiply(BigInteger.TEN.pow(1)).add(BigInteger.valueOf(2*(rand.nextInt(0, 5)) + 1));
        return bigNum;
    }

    // NOT WORKING
    public static BigInteger generateBigPrimeInt(int bigIntLength){
        BigInteger bigPrime = BigInteger.ZERO;
        BigInteger candidate = BigInteger.ZERO;
        Boolean probPrime = false;
        Boolean testResult;

        while(probPrime != true){
            candidate = generateBigInteger(bigIntLength);
            PrimalityTest pt = new PrimalityTest(candidate);
            testResult = pt.test(10);
            if (testResult == true){
                bigPrime = candidate;
                probPrime = true;
            }
        }

        return bigPrime;
    }

    // REWRITE OUR OWN VERSION - rewrite gcd
    public Boolean isRelativelyPrime(BigInteger x, BigInteger y){
        // Euclidean Algorithm
        // We want to make this a safe prime probably
        BigInteger a;
        BigInteger b;
        if(x.compareTo(y) > 0){ a = x; b = y; }
        else if (x.equals(y)){return false;}
        else{ a = y; b = x; }
        
        return (a.gcd(b)).equals(BigInteger.ONE);
        //return ;
    }

    // Obersvers

    public int getIntMessage(){
        return intMess;
    }

    public BigInteger getMessage(){
        return message;
    }
    
    public BigInteger getPrime(){
        return prime;
    }
}
