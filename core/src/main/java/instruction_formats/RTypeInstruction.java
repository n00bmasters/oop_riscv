package instruction_formats;

import types.ProcessorState;
import types.RVWord;

import static types.Utils.getBits;

public abstract class RTypeInstruction implements Instruction {
    protected final int rd, rs1, rs2;
    public RTypeInstruction(int instructionWord) {
        rd = getBits(instructionWord, 7, 11);
        rs1 = getBits(instructionWord, 15, 19);
        rs2 = getBits(instructionWord, 20, 24);
    }
    public abstract void execute(ProcessorState state);
}
