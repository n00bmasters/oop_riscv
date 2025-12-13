package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

import static types.Utils.getBits;

public abstract class ITypeInstruction implements Instruction{
    protected int rd, rs1;
    protected RVWord imm;
    public ITypeInstruction(int instructionWord) {
        rd = getBits(instructionWord, 7, 11);
        rs1 = getBits(instructionWord, 15, 19);
        imm = new RVWord(BigInteger.valueOf(getBits(instructionWord, 20, 24))).signExtend(12);
    }
    public abstract void execute(ProcessorState state);
}
