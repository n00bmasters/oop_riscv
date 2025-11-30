import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Srl extends RTypeInstruction {

    public Srl(RVWord instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        state.setRegister(rd, rs1.shr(rs2.getBits(0, 6)));
    }
}
