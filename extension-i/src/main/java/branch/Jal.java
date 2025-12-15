package branch;

import instruction_formats.JTypeInstruction;
import types.ProcessorState;
import types.RVWord;
import java.math.BigInteger;

public class Jal extends JTypeInstruction {

    public Jal(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord currentPC = state.getPC();
        RVWord returnPC = currentPC.add(new RVWord(BigInteger.valueOf(4)));
        state.setRegister(rd, returnPC);
        state.setPC(currentPC.add(imm));
    }
}

