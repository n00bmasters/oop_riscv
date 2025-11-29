package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public abstract class STypeInstruction implements Instruction{
    private int rs1, rs2;
    private RVWord imm;
    public STypeInstruction(RVWord instructionWord) {
        rs1 = instructionWord.getBits(15, 19);
        rs2 = instructionWord.getBits(20, 24);
        int imm4_0 = instructionWord.getBits(7, 11);
        int imm11_5 = instructionWord.getBits(25, 31);
        int rawImm = (imm11_5 << 5) | imm4_0;
        imm = new RVWord(BigInteger.valueOf(rawImm)).signExtend(12);
    }
    public abstract void execute(ProcessorState state);
}
