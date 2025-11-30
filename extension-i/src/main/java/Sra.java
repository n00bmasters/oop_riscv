import instruction_formats.RTypeInstruction;
import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

public class Sra extends RTypeInstruction {

    public Sra(RVWord instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        int shift = rs2.getBits(0, 6);
        RVWord res = new RVWord(rs1.getSignedValue()).shr(shift);
        state.setRegister(rd, res);
    }
}
