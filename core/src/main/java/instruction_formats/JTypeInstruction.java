package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

import static types.Utils.getBits;

public abstract class JTypeInstruction implements Instruction {
    protected int rd;
    protected RVWord imm;
    public JTypeInstruction(int instructionWord) {
        rd = getBits(instructionWord, 7, 11);
        int imm20 = getBits(instructionWord, 31, 31);
        int imm19_12 = getBits(instructionWord, 12, 19);
        int imm11 = getBits(instructionWord, 20, 20);
        int imm10_1 = getBits(instructionWord, 21, 30);
        imm = new RVWord(BigInteger.valueOf((imm20 << 20) | (imm19_12 << 12) | (imm11 << 11) | (imm10_1 << 1))).signExtend(21);
    }
    public abstract void execute(ProcessorState state);
}

