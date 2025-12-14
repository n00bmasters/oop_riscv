package shifts;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Srai extends ITypeInstruction {

    public Srai(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = rs1.sra(imm.getBits(0, 4));
        state.setRegister(rd, res);
    }
}
