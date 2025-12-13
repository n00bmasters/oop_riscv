import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Srad extends RTypeInstruction {

    public Srad(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        RVWord res = rs1.signExtend(64);
        res = res.sra(rs2.getBits(0, 5));
        state.setRegister(rd, res.signExtend(64));
    }
}
