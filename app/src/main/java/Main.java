import java.math.BigInteger;
import types.RVWord;

public class Main {
    public static void main(String[] args) {
        RVWord testWord = new RVWord(BigInteger.valueOf(32));
        System.out.printf("types.RVWord xlen = %d\n", testWord.getXlen());
    }
}
