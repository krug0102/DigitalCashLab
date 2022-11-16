import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.security.MessageDigest;


public class Main {

    // todo: change this to the public key later

    public static final Scanner scanner = new Scanner(System.in);
    public static final BigInteger e = BigInteger.ZERO;
    public static final BigInteger n = BigInteger.ONE; // These two things are the Bank's public

    public static void main(String[] args) {
        System.out.println("---------- Ligma Inc. ----------");
        System.out.println(convertStringToArray("[[22,33,12],[214,52,12]]"));
        System.out.println(g(BigInteger.valueOf(310830192), BigInteger.valueOf(48927492)));
        prompt();
    }

    /*
    We 'bout to take Conner for all he's worth!
     */

    /*
    1. Generate a string of zeros and ones, give it to the customer.
    2. The customer either gives back I ⊕ ai, di, and xi for a 0 OR a
    ai, ci, and yi for a 1.
    3. Use those inputs to validate f(xi,yi) and make sure its not double spent.
    4. If it is valid and has not been double spent, deposit in the merchant bank account.
     */
    /*
    - ai is a binary number the same length as I, which is the customer account number concatenated with the bill number,
     so 18 digits.

    - ci and di are both random numbers for each chunk
     */

    /*  Tasks:
    implement SHA
    use BigInteger
    method getBytes of string class for SHA

        Details:
    key ~ 1000 bit modulus, generate 500-bit or so primes
    account number is 8 digits, bill number will be 10 digits
    a,c,d are between 0 and 2^(128)-1
    Use SHA-256 on concatenation of inputs for the XOR with a and padded with zeros on the left to 128 bits
    Has functions f,g use result of SHA-256 on the concatenation of the two inputs in the order given in the description
    k = k' = 10 for testing
     */

    /*
    As merchants, we only verify cash is valid and deposit the cash in the bank.
    We also check with the bank to make sure the bill number, x, hasn't been used before.
     */

    /*
    Customers send merchants (x, f(x)^d).  We verify by calculating f(x) = f(x)^d^e using the bank's public exponent e.
     */
    public static boolean verifyBill(BigInteger fx) {
        boolean equals = fx.equals(fx.modPow(e, n));
        return equals; // check that integer is not cut off
    }

    /*
    We verify by calculating f(xi, yi), because we get ample information from either choice
    We feel like we're missing one important piece of information that we need to verify.
     */
    public static void verifyBillPrompt() {
        System.out.println("Enter choice string:");
        String choices = scanner.nextLine();
        System.out.println("Enter inputs:");
        String inputs = scanner.nextLine();
        List<List<BigInteger>> converted = convertStringToArray(inputs);

        for (char choice : choices.toCharArray()) {

        }
    }

    public static List<List<BigInteger>> convertStringToArray(String inputs){
        inputs = inputs.replace("[", "");
        inputs = inputs.replace("]", "");

        String[] vals = inputs.split(",");

        List<List<BigInteger>> result = new ArrayList<>();
        List<BigInteger> temp = new ArrayList<>();
        for (int i = 0; i < vals.length; i++) {
            temp.add(new BigInteger(vals[i]));
            if (i % 3 == 2) {
                result.add(temp);
                temp = new ArrayList<>();
            }
        }
        return result;
    }

    public static void generateChunkChoicesPrompt() {
        System.out.println("The randomly generated chunk choices are: " + calculateOptionString());
        prompt();
    }

    public static String calculateOptionString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            result.append(new Random().nextInt(2));
        }
        return result.toString();
    }


    public static void prompt() {
        System.out.println("Enter an option: \n1. Verify bill\n2. Generate chunk choices");
        switch (new Scanner(System.in).nextInt()) {
            case 1:
                verifyBillPrompt();
                break;
            case 2:
                generateChunkChoicesPrompt();
                break;
            default:
                System.exit(0);
        }
        prompt();
    }

    // todo: convert BigIntegers into binary representation so SHA can hash.  Padding needs to be done for the XOR with ai.
    public static String f(BigInteger x, BigInteger y) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        String originalString = x.toString(2) + y.toString(2);
        return byteArrayToBinary(digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8)));
    }

    public static String g(BigInteger x, BigInteger y) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        String originalString = x.toString(2) + y.toString(2);
        return byteArrayToBinary(digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8)));
    }

    public static String byteArrayToBinary(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            result.append(Integer.toBinaryString(input[i]));
        }
        return result.toString();
    }

}