import instruction_formats.ITypeInstruction;
import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Slti extends ITypeInstruction {

    public Slti(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = new RVWord(rs1.getValue().compareTo(imm.getValue()) < 0 ? BigInteger.ONE : BigInteger.ZERO);
        state.setRegister(rd, res);
    }
}
