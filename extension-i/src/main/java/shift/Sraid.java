package shift;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sraid extends ITypeInstruction {

    public Sraid(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = rs1.signExtend(64);
        res = res.sra(imm.getBits(0, 5));
        state.setRegister(rd, res.signExtend(64));
    }
}
