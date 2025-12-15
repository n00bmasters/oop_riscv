package load_store;

import instruction_formats.STypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sh extends STypeInstruction {

    public Sh(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        state.mem.writeMemory(rs1.add(imm), rs2, 2);
    }
}
