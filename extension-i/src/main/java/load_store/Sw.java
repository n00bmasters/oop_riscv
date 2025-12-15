package load_store;

import instruction_formats.STypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sw extends STypeInstruction {

    public Sw(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        state.mem.writeMemory(rs1.add(imm), rs2, 4);
    }
}
