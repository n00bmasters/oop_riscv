package types;

import java.math.BigInteger;

public class ProcessorState {

    private int xlen;
    private int endian;
    private Memory mem;
    RVWord[] registers = new RVWord[32];


    public ProcessorState(int xlen, int endian) {
        this.xlen = xlen;
        this.endian = endian; // 1 - little, 2 - big
        this.mem = Memory(xlen);
        RVWord.setXlen(xlen);
        initRegisters();
    }

    public void addSection(Section section){
        mem.addSection(section);
    }

    public void addSegment(Segment segment){
        mem.addSegment(segment);
    }

    public Section getSection(int ind){
        return mem.getSection(ind);
    }

    public Segment addSegment(int ind){
        return mem.getSegment(ind);
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
