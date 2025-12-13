import instruction_formats.ITypeInstruction;
import instruction_formats.RTypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Sllid extends ITypeInstruction {

    public Sllid(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        state.setRegister(rd, rs1.shl(imm.getBits(0, 5)).signExtend(64));
    }
}
