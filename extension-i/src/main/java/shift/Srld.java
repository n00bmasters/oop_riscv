package shift;

import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Srld extends RTypeInstruction {

    public Srld(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        BigInteger mask = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
        RVWord res = rs1.and(new RVWord(mask));
        res = res.srl(rs2.getBits(0, 5));
        state.setRegister(rd, res.signExtend(64));
    }
}
