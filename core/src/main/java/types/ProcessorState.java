package types;

import java.math.BigInteger;

public class ProcessorState {

    private int xlen;
    private int endian;
    RVWord[] registers = new RVWord[32];


    public ProcessorState(int xlen, int endian) {
        this.xlen = xlen;
        this.endian = endian; // 1 - little, 2 - big
        RVWord.setXlen(xlen);
        initRegisters();
    }

    private void initRegisters() {
        for (int i = 0; i < 32; ++i) registers[i] = new RVWord(BigInteger.ZERO);
    }

    public RVWord getRegister(int index) {
        return registers[index];
    }

    public void setRegister(int index, RVWord result) {
        if (index == 0) return;
        registers[index] = result;
    }
}
