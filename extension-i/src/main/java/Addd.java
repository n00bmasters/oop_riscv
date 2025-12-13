import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Addd extends RTypeInstruction {

    public Addd(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord rs2 = state.getRegister(this.rs2);
        state.setRegister(rd, rs1.add(rs2).signExtend(64));
    }
}
