import instruction_formats.RTypeInstruction;
import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public class Sltu extends RTypeInstruction {

    public Sltu(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        RVWord res = new RVWord(rs1.getValue().abs().compareTo(rs2.getValue()) < 0 ? BigInteger.ONE : BigInteger.ZERO);
        state.setRegister(rd, res);
    }
}
