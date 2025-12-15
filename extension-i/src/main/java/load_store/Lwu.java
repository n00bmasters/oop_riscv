package load_store;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Lwu extends ITypeInstruction {

    public Lwu(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = state.mem.readMemory(rs1.add(imm), 4);
        res = res.and(new RVWord(BigInteger.ONE.shiftLeft(32).subtract(BigInteger.ONE)));
        state.setRegister(rd, res);
    }
}
