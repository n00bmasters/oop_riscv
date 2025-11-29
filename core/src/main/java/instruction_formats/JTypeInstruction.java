package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public abstract class JTypeInstruction implements Instruction {
    private int rd;
    private RVWord imm;
    public JTypeInstruction(RVWord instructionWord) {
        rd = instructionWord.getBits(7, 11);
        int imm20 = instructionWord.getBits(31, 31);
        int imm19_12 = instructionWord.getBits(12, 19);
        int imm11 = instructionWord.getBits(20, 20);
        int imm10_1 = instructionWord.getBits(21, 30);
        imm = new RVWord(BigInteger.valueOf((imm20 << 20) | (imm19_12 << 12) | (imm11 << 11) | (imm10_1 << 1))).signExtend(21);
    }
    public abstract void execute(ProcessorState state);
}

