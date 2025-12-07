package types;

import java.math.BigInteger;
import java.io.IOException;

public class ProcessorState {

    private int xlen;
    private int endian;
    private Memory mem;
    RVWord[] registers = new RVWord[32];

    public ProcessorState(int xlen) {
        this.xlen = xlen;
        this.mem = new Memory(xlen);
        RVWord.setXlen(xlen);
        initRegisters();
    }

    public ProcessorState(int xlen, int endian) {
        this.xlen = xlen;
        this.endian = endian; // 1 - little, 2 - big
        this.mem = new Memory(xlen);
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

    public int getSectionCount(){
        return mem.sections.size();
    }

    public int getSegmentCount(){
        return mem.segments.size();
    }

    public Segment getSegment(int ind){
        return mem.getSegment(ind);
    }

    private void initRegisters() {
        for (int i = 0; i < 32; ++i) registers[i] = new RVWord(BigInteger.ZERO);
    }

    public RVWord getRegister(int index) {
        return registers[index];
    }

    public Page mapPage(RVWord vaddr, int flags) {
        return mem.mapPage(vaddr, flags);
    }

    public void initByte(RVWord startAddr, byte data) {
        mem.initBytes(startAddr, data);
    }

    public void setRegister(int index, RVWord result) {
        if (index == 0) return;
        registers[index] = result;
    }

    public void dumpMemory(String filename) {
        try { mem.dumpToFile(filename); } catch (IOException e) {}
    }
}
