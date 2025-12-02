package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public abstract class BTypeInstruction implements Instruction{
    protected int rs1, rs2;
    protected RVWord label;
    public BTypeInstruction(RVWord instructionWord) {
        rs1 = instructionWord.getBits(15, 19);
        rs2 = instructionWord.getBits(20, 24);
        int imm12 = instructionWord.getBits(31, 31);
        int imm11 = instructionWord.getBits(7, 7);
        int imm10_5 = instructionWord.getBits(25, 30);
        int imm4_1 = instructionWord.getBits(8, 11);
        int rawImm = (imm12 << 12) | (imm11 << 11) | (imm10_5 << 5) | (imm4_1 << 1);
        label = new RVWord(BigInteger.valueOf(rawImm)).signExtend(13);
    }
    public abstract void execute(ProcessorState state);
}
