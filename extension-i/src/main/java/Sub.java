import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sub extends RTypeInstruction {

    public Sub(RVWord instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        state.setRegister(rd, rs1.sub(rs2));
    }
}
