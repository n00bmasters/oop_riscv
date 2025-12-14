package shift;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sraiw extends ITypeInstruction {

    public Sraiw(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = rs1.signExtend(32);
        res = res.sra(imm.getBits(0, 4));
        state.setRegister(rd, res.signExtend(32));
    }
}
