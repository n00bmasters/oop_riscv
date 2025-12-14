package comparison;

import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Slt extends RTypeInstruction {

    public Slt(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        RVWord res = new RVWord(rs1.getSignedValue().compareTo(rs2.getSignedValue()) < 0 ? BigInteger.ONE : BigInteger.ZERO);
        state.setRegister(rd, res);
    }
}
