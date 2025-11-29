package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public abstract class UTypeInstruction implements Instruction{
    private int rd;
    private RVWord imm;
    public UTypeInstruction(RVWord instructionWord) {
        rd = instructionWord.getBits(7, 11);
        imm = new RVWord(BigInteger.valueOf(instructionWord.getBits(12, 31))).signExtend(20);
    }
    public abstract void execute(ProcessorState state);
}
