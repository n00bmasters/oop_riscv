import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Srlw extends RTypeInstruction {

    public Srlw(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        BigInteger mask = BigInteger.ONE.shiftLeft(32).subtract(BigInteger.ONE);
        RVWord res = rs1.and(new RVWord(mask));
        res = res.srl(rs2.getBits(0, 4));
        state.setRegister(rd, res.signExtend(32));
    }
}
