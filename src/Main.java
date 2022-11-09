import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
import java.security.MessageDigest;

public class Main {

    // todo: change this to the public key later

    public static final Scanner scanner = new Scanner(System.in);
    public static final BigInteger e = BigInteger.ZERO;

    public static void main(String[] args) {
        System.out.println("---------- Ligma Inc. ----------");
        prompt();
    }

    /*
    We 'bout to take Conner for all he's worth!
     */

    /*
    As merchants, we only verify cash is valid and deposit the cash in the bank.
    We also check with the bank to make sure the bill number, x, hasn't been used before.
     */

    /*
    Customers send merchants (x, f(x)^d).  We verify by calculating f(x) = f(x)^d^e using the bank's public exponent e.
     */
    public static boolean verifyBill(BigInteger fx){
        boolean equals = fx.equals(fx.pow(e.intValue()));
        return equals; // check that integer is not cut off
    }

    public static void verifyBillPrompt(){
        System.out.println("Enter bill number:");
        BigInteger x = scanner.nextBigInteger();
        System.out.println("Enter f(x)^d:");
        BigInteger fxd = scanner.nextBigInteger();
        boolean valid = verifyBill(fxd);
        System.out.println("Bill #: " + x + " has been received and " + (valid ? "is VALID": "is INVALID"));
    }

    public static void depositCashPrompt(){

    }

    public static String calculateOptionString(){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            result.append(new Random().nextInt(2));
        }
        return result.toString();
    }


    public static void prompt() {
        System.out.println("Enter an option: \n1. Verify bill\n2. Deposit cash");
        switch (new Scanner(System.in).nextInt()){
            case 1:
                verifyBillPrompt();
                break;
            case 2:
                depositCashPrompt();
                break;
            default:
                System.exit(0);
        }
        prompt();
    }
}