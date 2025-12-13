package types;

public class Utils {
    public static int getBits(int value, int from, int to) {
        return (value >> from) & ((1 << (to - from + 1)) - 1);
    }
}
