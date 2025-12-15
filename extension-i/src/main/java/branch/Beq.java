package branch;

import instruction_formats.BTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Beq extends BTypeInstruction {

    public Beq(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord val1 = state.getRegister(rs1);
        RVWord val2 = state.getRegister(rs2);

        if (val1.equals(val2)) {
            state.setPC(state.getPC().add(label));
        }
    }
}
