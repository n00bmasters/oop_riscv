import instruction_formats.ITypeInstruction;
import types.ProcessorState;
import types.RVWord;

public class Xori extends ITypeInstruction {

    public Xori(int instructionWord) {
        super(instructionWord);
    }

    @Override
    public void execute(ProcessorState state) {
        RVWord rs1 = state.getRegister(this.rs1);
        state.setRegister(rd, rs1.xor(imm));
    }
}
