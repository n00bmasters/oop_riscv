package types;

import java.math.BigInteger;

public class RVWord {
    private static int xlen;
    private BigInteger value;

    public RVWord(BigInteger value) {
        this.value = value;
        trunc();
    }

    public static BigInteger getMask() {
        return BigInteger.ONE.shiftLeft(xlen).subtract(BigInteger.ONE);
    }

    public void trunc() {
        this.value = this.value.and(getMask());
    }

    public BigInteger getSignedValue() {
        if (value.testBit(xlen - 1)) {
            return value.subtract(BigInteger.ONE.shiftLeft(xlen));
        }
        return value;
    }

    public RVWord signExtend(int fromLen) {
        BigInteger truncated = this.value.and(BigInteger.ONE.shiftLeft(fromLen).subtract(BigInteger.ONE));

        if (truncated.testBit(fromLen - 1)) {
            BigInteger extended = truncated.subtract(BigInteger.ONE.shiftLeft(fromLen));
            return new RVWord(extended);
        }
        return new RVWord(truncated);
    }

    public RVWord add(RVWord other) {
        return new RVWord(value.add(other.value));
    }

    public RVWord sub(RVWord other) {
        return new RVWord(value.subtract(other.value));
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

    public RVWord not() {
        return new RVWord(this.value.not());
    }

    public RVWord shl(int shift) {
        return new RVWord(this.value.shiftLeft(shift));
    }

    public RVWord srl(int shift) {
        return new RVWord(this.value.shiftRight(shift));
    }

    public RVWord sra(int shift) {
        if (shift == 0) return new RVWord(this.value);
        BigInteger signed = getSignedValue();
        return new RVWord(signed.shiftRight(shift));
    }

    public BigInteger getValue() { return value; }

    public static void setXlen(int value) { xlen = value; }

    public static int getXlen() { return xlen; }

    public int getBits(int from, int to) {
        return value.shiftRight(from).and(BigInteger.valueOf((1 << (to - from + 1)) - 1)).intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RVWord other = (RVWord) o;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
