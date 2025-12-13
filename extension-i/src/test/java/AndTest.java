import static org.junit.jupiter.api.Assertions.assertEquals;

import instruction_formats.Instruction;
import java.math.BigInteger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import types.RVWord;

public class AndTest extends TestExtension {

    @ParameterizedTest
    @MethodSource("TestExtension#xlenValues")
    void test(int xlen) {
        setup(xlen);
        int rd = 10;
        int rs1 = 11;
        int rs2 = 12;
        setRegister(rs1, BigInteger.valueOf(100));
        setRegister(rs2, BigInteger.valueOf(50));
        int instructionWord = createRType(rd, rs1, rs2, 0x6, 0, 0b0110011);
        Instruction add = new And(instructionWord);
        add.execute(state);
        assertEquals(BigInteger.valueOf(32), getRegister(rd).getValue());
    }
}
