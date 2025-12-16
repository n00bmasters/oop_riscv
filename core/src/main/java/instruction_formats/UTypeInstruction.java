package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

import static types.Utils.getBits;

public abstract class UTypeInstruction implements Instruction{
    protected int rd;
    protected RVWord imm;
    public UTypeInstruction(int instructionWord) {
        rd = getBits(instructionWord, 7, 11);
        imm = new RVWord(BigInteger.valueOf(getBits(instructionWord, 12, 31))).signExtend(20);
    }
    public abstract void execute(ProcessorState state);

    @Override
    public String toString() {
        return this.getClass().toString().substring(this.getClass().toString().indexOf('.') + 1).toLowerCase() + " x" + String.valueOf(rd) + ", " + imm.getSignedValue();
    }

}
