package shift;

import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sllw extends RTypeInstruction {

    public Sllw(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        state.setRegister(rd, rs1.shl(rs2.getBits(0, 4)).signExtend(32));
    }
}
