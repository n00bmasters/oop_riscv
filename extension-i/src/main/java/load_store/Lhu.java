package load_store;

import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

import java.math.BigInteger;

public class Lhu extends ITypeInstruction {

    public Lhu(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        RVWord res = state.mem.readMemory(rs1.add(imm), 2).signExtend(16);
        res = res.and(new RVWord(BigInteger.ONE.shiftLeft(16).subtract(BigInteger.ONE)));
        state.setRegister(rd, res);
    }
}
