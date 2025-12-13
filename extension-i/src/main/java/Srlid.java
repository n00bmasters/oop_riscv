import instruction_formats.ITypeInstruction;
import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Srlid extends ITypeInstruction {

    public Srlid(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        BigInteger mask = BigInteger.ONE.shiftLeft(64).subtract(BigInteger.ONE);
        RVWord res = rs1.and(new RVWord(mask));
        res = res.srl(imm.getBits(0, 5));
        state.setRegister(rd, res.signExtend(64));
    }
}
