package shifts;

import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sraw extends RTypeInstruction {

    public Sraw(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        RVWord res = rs1.signExtend(32);
        res = res.sra(rs2.getBits(0, 4));
        state.setRegister(rd, res.signExtend(32));
    }
}
