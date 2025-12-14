package comparison;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Sltiu extends ITypeInstruction {

    public Sltiu(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = new RVWord(rs1.getValue().abs().compareTo(imm.getValue()) < 0 ? BigInteger.ONE : BigInteger.ZERO);
        state.setRegister(rd, res);
    }
}
