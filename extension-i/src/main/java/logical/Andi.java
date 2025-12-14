package logical;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Andi extends ITypeInstruction {

    public Andi(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        state.setRegister(rd, rs1.and(imm));
    }
}
