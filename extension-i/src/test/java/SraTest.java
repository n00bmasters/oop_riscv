import static org.junit.jupiter.api.Assertions.assertEquals;

import instruction_formats.Instruction;
import java.math.BigInteger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import types.RVWord;

public class SraTest extends TestExtension {

    @ParameterizedTest
    @MethodSource("TestExtension#xlenValues")
    void testLargeShift(int xlen) {
        BigInteger mask = BigInteger.ONE.shiftLeft(xlen).subtract(BigInteger.ONE);
        BigInteger rs1Value = BigInteger.valueOf(-1042L << 28).and(mask);
        BigInteger rs2Value = BigInteger.valueOf(28);
        setup(xlen);
        int rd = 10;
        int rs1 = 11;
        int rs2 = 12;
        setRegister(rs1, rs1Value);
        setRegister(rs2, rs2Value);
        RVWord instructionWord = createRType(rd, rs1, rs2, 0x6, 0x20, 0b0110011);
        Instruction add = new Sra(instructionWord);
        add.execute(state);
        BigInteger expected = rs1Value.shiftRight(rs2Value.intValue());
        BigInteger actual = getRegister(rd).getValue();
        assertEquals(expected, actual);
    }
}
