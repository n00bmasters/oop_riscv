package load_store;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.Memory;
import types.RVWord;

public class    Lb extends ITypeInstruction {

    public Lb(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = state.mem.readMemory(rs1.add(imm), 1).signExtend(8);
        state.setRegister(rd, res);
    }
}
