import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sll extends RTypeInstruction {

    public Sll(RVWord instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        state.setRegister(rd, rs1.shl(rs2.getBits(0, 6)));
    }
}
