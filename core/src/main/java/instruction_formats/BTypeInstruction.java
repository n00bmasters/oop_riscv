package instruction_formats;

import java.math.BigInteger;
import types.ProcessorState;
import types.RVWord;
import static types.Utils.getBits;

public abstract class BTypeInstruction implements Instruction{
    protected int rs1, rs2;
    protected RVWord label;
    public BTypeInstruction(int instructionWord) {
        rs1 = getBits(instructionWord, 15, 19);
        rs2 = getBits(instructionWord, 20, 24);
        int imm12 = getBits(instructionWord, 31, 31);
        int imm11 = getBits(instructionWord, 7, 7);
        int imm10_5 = getBits(instructionWord, 25, 30);
        int imm4_1 = getBits(instructionWord, 8, 11);
        int rawImm = (imm12 << 12) | (imm11 << 11) | (imm10_5 << 5) | (imm4_1 << 1);
        label = new RVWord(BigInteger.valueOf(rawImm)).signExtend(13);
    }

    @Override
    public String toString() {
        return this.getClass().toString().substring(this.getClass().toString().indexOf('.') + 1).toLowerCase() + " x" + String.valueOf(rs1) + ", x" + String.valueOf(rs2) + ", " + label.getSignedValue();
    }

    public abstract void execute(ProcessorState state);
}
