package load_store;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Lh extends ITypeInstruction {

    public Lh(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = state.mem.readMemory(rs1.add(imm), 2).signExtend(16);
        state.setRegister(rd, res);
    }
}
