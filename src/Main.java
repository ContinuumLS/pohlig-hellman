import java.math.*;

public class Main {
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        PohligHellman ph = new PohligHellman();

        BigInteger message = ph.getMessage();
        System.out.println("Your initial message is " + message);

        BigInteger enKey = ph.generateEnKey();
        BigInteger deKey = ph.generateDeKey(enKey);

        BigInteger ciphText = ph.encrypt(message, enKey);
        System.out.println("\nCipher text is " + ciphText);

        BigInteger decMessage = ph.decrpyt(ciphText, deKey);

        int decIntMessage = decMessage.intValue();
        System.out.println("\nDecrypted message is " + decIntMessage + "!");
    }
}