package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;

import static types.Utils.getBits;

public abstract class STypeInstruction implements Instruction{
    protected int rs1, rs2;
    protected RVWord imm;
    public STypeInstruction(int instructionWord) {
        rs1 = getBits(instructionWord, 15, 19);
        rs2 = getBits(instructionWord, 20, 24);
        int imm4_0 = getBits(instructionWord, 7, 11);
        int imm11_5 = getBits(instructionWord, 25, 31);
        int rawImm = (imm11_5 << 5) | imm4_0;
        imm = new RVWord(BigInteger.valueOf(rawImm)).signExtend(12);
    }

    @Override
    public String toString() {
        return this.getClass().toString().substring(this.getClass().toString().indexOf('.') + 1).toLowerCase() + " x" + String.valueOf(rs1) + ", x" + String.valueOf(rs2) + ", " + imm.getSignedValue();
    }

    public abstract void execute(ProcessorState state);
}
