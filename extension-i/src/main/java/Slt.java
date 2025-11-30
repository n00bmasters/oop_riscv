import instruction_formats.RTypeInstruction;
import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public class Slt extends RTypeInstruction {

    public Slt(RVWord instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        BigInteger s1 = rs1.getSignedValue();
        BigInteger s2 = rs2.getSignedValue();
        RVWord res = new RVWord(s1.compareTo(s2) < 0 ? BigInteger.ONE : BigInteger.ZERO);
        state.setRegister(rd, res);
    }
}
