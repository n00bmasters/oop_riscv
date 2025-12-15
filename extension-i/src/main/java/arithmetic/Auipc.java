package arithmetic;

import instruction_formats.UTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Auipc extends UTypeInstruction {

    public Auipc(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord offset = imm.shl(12);
        RVWord pc = state.getPC();
        state.setRegister(rd, pc.add(offset));
    }
}
