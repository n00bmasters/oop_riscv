package branch;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;
import java.math.BigInteger;

public class Jalr extends ITypeInstruction {

    public Jalr(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord currentPC = state.getPC();
        RVWord rdval = state.getRegister(rs1);
        RVWord returnPC = currentPC.add(new RVWord(BigInteger.valueOf(4)));
        state.setRegister(rd, returnPC);
        RVWord targetAddress = rdval.add(imm);
        BigInteger maskedValue = targetAddress.getValue().clearBit(0);
        state.setPC(new RVWord(maskedValue));
    }
}
