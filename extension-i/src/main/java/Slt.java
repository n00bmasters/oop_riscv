import instruction_formats.RTypeInstruction;
import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public class Slt extends RTypeInstruction {

    public Slt(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        BigInteger s1 = rs1.getValue();
        BigInteger s2 = rs2.getValue();
        RVWord res = new RVWord(s1.compareTo(s2) < 0 ? BigInteger.ONE : BigInteger.ZERO);
        state.setRegister(rd, res);
    }
}
