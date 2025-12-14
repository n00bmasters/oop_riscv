package arithmetic;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Addi extends ITypeInstruction {

    public Addi(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        state.setRegister(rd, rs1.add(imm));
    }
}
