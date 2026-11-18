import java.math.*;
import java.util.Random;

public class PohligHellman {
    int intMess;
    BigInteger prime;
    BigInteger message;

    // Constructors

    public PohligHellman() {
        Random rand = new Random();
        intMess = rand.nextInt(1,50);
        message = BigInteger.valueOf(intMess);
        prime = generatePrime();
    }

    public PohligHellman(int m){
        intMess = m;
        message  = BigInteger.valueOf(m);
        prime = generatePrime();
    }

    public PohligHellman(String m){
        //mess = tostring(m);
        // BigInteger message  = BigInteger.valueOf(m);
        prime = generatePrime();
    }

    // Mutators

    public BigInteger encrypt(BigInteger message, BigInteger enKey, BigInteger prime){
        BigInteger ciphText = message.modPow(enKey, prime); // REPLACE w/ message.mod(enKey)
        return ciphText;
    }

    public BigInteger decrpyt(BigInteger cipherText,  BigInteger deKey, BigInteger prime){
        BigInteger decMessage = cipherText.modPow(deKey, prime);
        return decMessage;
    }

    public static BigInteger generatePrime(){
        Random rand = new Random();
        BigInteger prime = BigInteger.probablePrime(2048, rand);
        return prime;
    }

    public BigInteger generateEnKey(){
        BigInteger enKey = BigInteger.ZERO;
        // Logic
        // Could use built in gcd(BigInteger val) method
        // enKey = n st gcd(enKey, prime-1) = 1
        
        return enKey;
    }

    public BigInteger generateDeKey(BigInteger enKey){
        BigInteger deKey = BigInteger.ZERO;
        // Logic
        // deKey = n st e * d = 1 % prime
        deKey = enKey.modInverse(prime);
        return deKey;
    }

    public Boolean isRelativelyPrime(BigInteger x, BigInteger y){
        // Logic
        // We want to make this a safe prime probably
        return false;
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
