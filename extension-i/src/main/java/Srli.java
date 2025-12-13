import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Srli extends ITypeInstruction {

    public Srli(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord mask = new RVWord(BigInteger.ONE.shiftLeft(RVWord.getXlen()).subtract(BigInteger.ONE));
        rs1 = rs1.and(mask);
        state.setRegister(rd, rs1.srl(imm.getBits(0, 6)));
    }
}
