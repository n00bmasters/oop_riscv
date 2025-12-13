import static org.junit.jupiter.api.Assertions.assertEquals;

import instruction_formats.Instruction;
import java.math.BigInteger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import types.RVWord;

public class SltuTest extends TestExtension {

    @ParameterizedTest
    @MethodSource("TestExtension#xlenValues")
    void testLargeShift(int xlen) {
        BigInteger rs1Value = BigInteger.valueOf(-100L);
        BigInteger rs2Value = BigInteger.valueOf(5);
        setup(xlen);
        int rd = 10;
        int rs1 = 11;
        int rs2 = 12;
        setRegister(rs1, rs1Value);
        setRegister(rs2, rs2Value);
        int instructionWord = createRType(rd, rs1, rs2, 0x6, 0x20, 0b0110011);
        Instruction add = new Sltu(instructionWord);
        add.execute(state);
        BigInteger expected = (rs1Value.compareTo(rs2Value) < 0) ? BigInteger.ONE : BigInteger.ZERO;
        BigInteger actual = getRegister(rd).getValue();
        assertEquals(expected, actual);
    }
}
