import static org.junit.jupiter.api.Assertions.assertEquals;

import instruction_formats.Instruction;
import java.math.BigInteger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import types.RVWord;

public class SubTest extends TestExtension {

    @ParameterizedTest
    @MethodSource("TestExtension#xlenValues")
    void test(int xlen) {
        setup(xlen);
        int rd = 10;
        int rs1 = 11;
        int rs2 = 12;
        setRegister(rs1, BigInteger.valueOf(100));
        setRegister(rs2, BigInteger.valueOf(50));
        RVWord instructionWord = createRType(rd, rs1, rs2, 0, 0x20, 0b0110011);
        Instruction add = new Sub(instructionWord);
        add.execute(state);
        assertEquals(BigInteger.valueOf(50), getRegister(rd).getValue());
    }

    @ParameterizedTest
    @MethodSource("TestExtension#xlenValues")
    void testNeg(int xlen) {
        setup(xlen);
        int rd = 10;
        int rs1 = 11;
        int rs2 = 12;
        setRegister(rs1, BigInteger.valueOf(50));
        setRegister(rs2, BigInteger.valueOf(100));
        RVWord instructionWord = createRType(rd, rs1, rs2, 0, 0x20, 0b0110011);
        Instruction add = new Sub(instructionWord);
        add.execute(state);
        assertEquals(BigInteger.valueOf(-50), getRegister(rd).getValue());
    }
}
