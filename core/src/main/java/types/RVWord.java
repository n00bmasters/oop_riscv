package types;

import java.math.BigInteger;

public class RVWord {
    private static int xlen;
    private BigInteger value;

    public RVWord(BigInteger value) {
        this.value = value;
        trunc();
    }

    public RVWord signExtend(int currentLen) {
        int signBit = value.shiftRight(currentLen - 1).and(BigInteger.ONE).intValue();
        if (signBit == 1) {
            BigInteger res = value;
            while (currentLen != xlen) {
                res = res.setBit(currentLen++);
            }
            return new RVWord(res);
        } else return new RVWord(value);
    }

    public RVWord add(RVWord other) {
        RVWord res = new RVWord(value.add(other.value));
        res.trunc();
        return res;
    }

    public RVWord sub(RVWord other) {
        RVWord res = new RVWord(value.subtract(other.value));
        res.trunc();
        return res;
    }

    public RVWord xor(RVWord other) {
        return new RVWord(value.xor(other.value));
    }

    public RVWord or(RVWord other) {
        return new RVWord(value.or(other.value));
    }

    public RVWord and(RVWord other) {
        return new RVWord(value.and(other.value));
    }

    public RVWord shl(int value) {
        RVWord res = new RVWord(this.value.shiftLeft(value));
        res.trunc();
        return res;
    }

    public RVWord shr(int value) {
        RVWord res = new RVWord(this.value.shiftRight(value));
        res.trunc();
        return res;
    }

    public void trunc() { value = value.and(BigInteger.ONE.shiftLeft(xlen).subtract(BigInteger.ONE)); }

    public static void setXlen(int value) { xlen = value; }

    public static int getXlen() { return xlen; }

    public int getBits(int from, int to) {
        return value.shiftRight(from).and(BigInteger.valueOf((1 << (to - from + 1)) - 1)).intValue();
    }
}
