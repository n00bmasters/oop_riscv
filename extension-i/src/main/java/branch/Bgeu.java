package branch;

import instruction_formats.BTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Bgeu extends BTypeInstruction {

    public Bgeu(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord val1 = state.getRegister(rs1);
        RVWord val2 = state.getRegister(rs2);

        if (val1.getValue().compareTo(val2.getValue()) >= 0) {
            state.setPC(state.getPC().add(label));
        }
    }
}

