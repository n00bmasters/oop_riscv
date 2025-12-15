package arithmetic;

import instruction_formats.UTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Lui extends UTypeInstruction {

    public Lui(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        state.setRegister(rd, imm.shl(12));
    }
}
