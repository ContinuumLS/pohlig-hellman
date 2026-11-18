import java.math.*;

public class Main {
    public static void main(String[] args) {
        run();
}

    public static void run() {
        PohligHellman ph = new PohligHellman(67);

        BigInteger enKey = ph.generateEnKey(); //NOTDONE
        BigInteger deKey = ph.generateDeKey(enKey); //WORKING
        BigInteger message = ph.getMessage(); //WORKING
        BigInteger prime = ph.getPrime(); //WORKING

       BigInteger ciphText = ph.encrypt(message, enKey, prime); //SHOULDWORK
       System.out.println(ciphText);

       BigInteger decMessage = ph.decrpyt(ciphText, deKey, prime); //SHOUDLWORK

       int decIntMessage = decMessage.intValue();
       //translate back to ascii??
       System.out.println("Decrypted message is " + decIntMessage + "!");
    }
}
