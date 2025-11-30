import static org.junit.jupiter.api.Assertions.assertEquals;

import instruction_formats.Instruction;
import java.math.BigInteger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import types.RVWord;

public class SrlTest extends TestExtension {

    @ParameterizedTest
    @MethodSource("TestExtension#xlenValues")
    void testLargeShift(int xlen) {
       setup(xlen);
        int rd = 10;
        int rs1 = 11;
        int rs2 = 12;
        RVWord rs1Value = new RVWord(BigInteger.valueOf(10042L << 28));
        RVWord rs2Value = new RVWord(BigInteger.valueOf(28));
        state.setRegister(rs1, rs1Value);
        state.setRegister(rs2, rs2Value);
        RVWord instructionWord = createRType(rd, rs1, rs2, 0x6, 0, 0b0110011);
        Instruction add = new Srl(instructionWord);
        add.execute(state);
        BigInteger mask = BigInteger.ONE.shiftLeft(xlen).subtract(BigInteger.ONE);
        BigInteger expected = rs1Value.getValue().shiftRight(rs2Value.getValue().intValue());
        BigInteger actual = state.getRegister(rd).getValue();
        assertEquals(expected, actual);
    }
}
