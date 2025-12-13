import java.math.BigInteger;
import java.util.stream.Stream;
import types.ProcessorState;
import types.RVWord;

public abstract class TestExtension {
    protected ProcessorState state;
    protected int xlen;

    void setup(int xlen) {
        this.xlen = xlen;
        state = new ProcessorState(xlen);
    }

    public static Stream<Integer> xlenValues() { return Stream.of(32, 64, 128); }

    protected void setRegister(int index, BigInteger value) {
        state.setRegister(index, new RVWord(value));
    }

    protected RVWord getRegister(int index) {
        return state.getRegister(index);
    }

    protected int createRType(int rd, int rs1, int rs2, int funct3, int funct7, int opcode) {
        int word = 0;
        word |= (opcode & 0x7F);
        word |= (rd & 0x1F) << 7;
        word |= (funct3 & 0x07) << 12;
        word |= (rs1 & 0x1F) << 15;
        word |= (rs2 & 0x1F) << 20;
        word |= (funct7 & 0x7F) << 25;
        return word;
    }

    protected int createIType(int rd, int rs1, int imm, int funct3, int opcode) {
        int word = 0;
        word |= (opcode & 0x7F);
        word |= (rd & 0x1F) << 7;
        word |= (funct3 & 0x07) << 12;
        word |= (rs1 & 0x1F) << 15;
        word |= (imm & 0xFFF) << 20;
        return word;
    }

    protected int createSType(int rs1, int rs2, int imm, int funct3, int opcode) {
        int word = 0;
        word |= (opcode & 0x7F);
        word |= (imm & 0x1F) << 7;  // imm[4:0]
        word |= (funct3 & 0x07) << 12;
        word |= (rs1 & 0x1F) << 15;
        word |= (rs2 & 0x1F) << 20;
        word |= ((imm >> 5) & 0x7F) << 25;  // imm[11:5]
        return word;
    }
}
