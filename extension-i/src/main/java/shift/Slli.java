package shift;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Slli extends ITypeInstruction {

    public Slli(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        state.setRegister(rd, rs1.shl(imm.getBits(0, 4)));
    }
}
