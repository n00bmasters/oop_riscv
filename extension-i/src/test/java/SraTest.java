import static org.junit.jupiter.api.Assertions.assertEquals;
import static types.RVWord.getMask;

import instruction_formats.Instruction;
import java.math.BigInteger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import types.RVWord;

public class SraTest extends TestExtension {

    @ParameterizedTest
    @MethodSource("TestExtension#xlenValues")
    void testLargeShift(int xlen) {
        setup(xlen);
        BigInteger rs1Value = BigInteger.valueOf(-1042L << 28).and(getMask());
        BigInteger rs2Value = BigInteger.valueOf(28);
        int rd = 10;
        int rs1 = 11;
        int rs2 = 12;
        setRegister(rs1, rs1Value);
        setRegister(rs2, rs2Value);
        int instructionWord = createRType(rd, rs1, rs2, 0x6, 0x20, 0b0110011);
        Instruction add = new Sra(instructionWord);
        add.execute(state);
        BigInteger signedRs1 = rs1Value;
        if (rs1Value.testBit(xlen - 1)) {
            signedRs1 = rs1Value.subtract(BigInteger.ONE.shiftLeft(xlen));
        }
        BigInteger expected = signedRs1.shiftRight(rs2Value.intValue());
        BigInteger actual = getRegister(rd).getSignedValue();
        assertEquals(expected, actual);
    }
}
