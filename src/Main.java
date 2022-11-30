import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.security.MessageDigest;


public class Main {

    public enum Bank{
        OLLIE, CONOR
    }

    /**
     * Our account number in Ollie's system is: 73448470
     */

    static public Bank selected = Bank.CONOR;
    public static final Scanner scanner = new Scanner(System.in);

    // Bank 1's public key pair
    public static final BigInteger CONOR_EXPONENT = BigInteger.ZERO;
    public static final BigInteger CONOR_N = BigInteger.ONE; // These two things are the Bank's public

    // Bank 2's public key pair
    public static final BigInteger OLLIE_EXPONENT = new BigInteger("7");
    public static final BigInteger OLLIE_N = new BigInteger("45102246070852877005834805071565697303530753565236670573550117115010865770545266090895421231898028549377566326963374616157756280120181748932018677751293313293523827859377666639166849915682609959979131340115018334098535898721356842525455183760413239157639236971732220612271453478131788159610221783610160493541");

    public static void main(String[] args) {
        System.out.println("---------- Ligma Inc. ----------");
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

    /*
    We verify by calculating f(xi, yi) for each chunk, finding the product, and then checking if the
    product is equal to f(xi, yi)^d^e, because we get ample information from either choice
     */
    public static void verifyBillPrompt() {
        System.out.println("Enter choice string:");
        String choices = scanner.nextLine();
        System.out.println("Enter inputs:");
        String inputs = scanner.nextLine();
        // todo: double check with customer to see what format they will give us these numbers in
        System.out.println("Enter the signed bill(?):");
        BigInteger signed = new BigInteger(scanner.nextLine());

        List<List<BigInteger>> converted = convertStringToArray(inputs);

        boolean billValid = billChunksAreValid(choices, converted, signed);

        System.out.println(billValid ? "Bill is valid!": "Bill is invalid!");
    }

    private static boolean billChunksAreValid(String choices, List<List<BigInteger>> converted, BigInteger signed) {
        List<BigInteger> result = new ArrayList<>();
        for (int i = 0; i < choices.length(); i++)  {
            List<BigInteger> cur = converted.get(i);

            switch (choices.charAt(i)){
                case '1' -> {
                    BigInteger ai = cur.get(0);
                    BigInteger ci = cur.get(1);
                    BigInteger yi = cur.get(2);

                    BigInteger resG = new BigInteger(g(ai,ci), 2);
                    String fxiyi = f(resG, yi);

                    result.add(new BigInteger(fxiyi, 2));
                }
                case '0' -> {
                    BigInteger aiXORi = cur.get(0);
                    BigInteger di = cur.get(1);
                    BigInteger xi = cur.get(2);

                    BigInteger resG = new BigInteger(g(aiXORi,di), 2);
                    String fxiyi = f(xi, resG);

                    result.add(new BigInteger(fxiyi, 2));
                }
            }
        }

        // multiply all elements in the list together to get the product.
        BigInteger product = result.stream().reduce(BigInteger.ONE, BigInteger::multiply);

        BigInteger raised = signed.modPow(getE(), getN());

        return raised.equals(product);
    }



    public static List<List<BigInteger>> convertStringToArray(String inputs){
        inputs = inputs.replace("[", "");
        inputs = inputs.replace("]", "");

        String[] vals = inputs.split(", ");

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
        System.out.println("The randomly generated chunk choices are: " + generateChunkChoices());
        prompt();
    }

    public static String generateChunkChoices() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            result.append(new Random().nextInt(2));
        }
        return result.toString();
    }


    public static void prompt() {
        System.out.println("Currently partnered with the bank of " + selected);
        System.out.println("Enter an option: \n1. Verify bill\n2. Generate chunk choices\n3. Switch banks");
        switch (new Scanner(System.in).nextInt()) {
            case 1 ->
                verifyBillPrompt();
            case 2 ->
                generateChunkChoicesPrompt();
            case 3 ->
                changeBanksPrompt();
            default ->
                System.exit(0);
        }
        prompt();
    }

    private static void changeBanksPrompt() {
        System.out.println("Select a bank:\n1. The Bank of Conor\n2. The Bank of Ollie");
        selected = switch (new Scanner(System.in).nextInt()){
            case 1 -> Bank.CONOR;
            case 2 -> Bank.OLLIE;
            default -> Bank.CONOR;
        };
        System.out.println("Selected " + selected + "!");
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

    public static BigInteger getE(){
        return selected == Bank.CONOR ? CONOR_EXPONENT: OLLIE_EXPONENT;
    }

    public static BigInteger getN(){
        return selected == Bank.OLLIE ? OLLIE_N : CONOR_N;
    }

}