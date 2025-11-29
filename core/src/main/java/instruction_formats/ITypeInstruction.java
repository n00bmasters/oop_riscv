package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public abstract class ITypeInstruction implements Instruction{
    private int rd, rs1;
    private RVWord imm;
    public ITypeInstruction(RVWord instructionWord) {
        rd = instructionWord.getBits(7, 11);
        rs1 = instructionWord.getBits(15, 19);
        imm = new RVWord(BigInteger.valueOf(instructionWord.getBits(20, 24))).signExtend(12);
    }
    public abstract void execute(ProcessorState state);
}
