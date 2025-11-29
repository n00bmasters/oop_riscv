package instruction_formats;

import types.ProcessorState;
import types.RVWord;

public abstract class RTypeInstruction implements Instruction {
    protected final int rd, rs1, rs2;
    public RTypeInstruction(RVWord instructionWord) {
        rd = instructionWord.getBits(7, 11);
        rs1 = instructionWord.getBits(15, 19);
        rs2 = instructionWord.getBits(20, 24);
    }
    public abstract void execute(ProcessorState state);
}
