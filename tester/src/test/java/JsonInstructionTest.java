import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import decoder.InstructionDecoder;
import instruction_formats.Instruction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import types.ProcessorState;
import types.RVWord;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonInstructionTest {

    private static final String TESTS_DIR = "tests";

    static Stream<TestCase> loadTestCases() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<TestCase> allTests = new ArrayList<>();
        var url = JsonInstructionTest.class.getClassLoader().getResource(TESTS_DIR);
        if (url == null) {
            throw new IOException("Tests directory not found: " + TESTS_DIR);
        }

        Path startPath = Paths.get(url.getPath());
        try (Stream<Path> paths = Files.walk(startPath)) {
            paths.filter(p -> p.toString().endsWith(".json"))
                    .forEach(path -> {
                        try {
                            List<TestCase> fileTests = mapper.readValue(path.toFile(), new TypeReference<>() {});
                            String fileName = startPath.relativize(path).toString();
                            for (TestCase t : fileTests) {
                                t.sourceFile = fileName;
                                allTests.add(t);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Error parsing JSON: " + path, e);
                        }
                    });
        }
        return allTests.stream();
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("loadTestCases")
    void runTest(TestCase test) {
        System.out.println("Running: " + test.name + " from " + test.sourceFile);

        // --- SETUP ---
        ProcessorState state = new ProcessorState(test.xlen);
        if (test.regs_in != null) {
            test.regs_in.forEach((regStr, valStr) -> {
                int reg = Integer.parseInt(regStr);
                BigInteger val = parseBigInt(valStr);
                state.setRegister(reg, new RVWord(val));
            });
        }

        // --- DECODE ---
        int instructionWord = Integer.decode(test.instruction_hex);
        InstructionDecoder decoder = new InstructionDecoder();
        Instruction instr = decoder.decode(instructionWord);
        String expectedMnemonic = test.mnemonic.toUpperCase();
        String actualClass = instr.getClass().getSimpleName().toUpperCase();
        assertEquals(expectedMnemonic, actualClass,
                String.format("DECODER ERROR in file [%s]. Test: '%s'. Wrong instruction class.", test.sourceFile, test.name));

        // --- EXECUTE ---
        instr.execute(state);
        if (test.regs_out != null) {
            test.regs_out.forEach((regStr, expectedValStr) -> {
                int reg = Integer.parseInt(regStr);
                BigInteger expected = parseBigInt(expectedValStr);
                BigInteger actual = state.getRegister(reg).getSignedValue();

                assertEquals(expected, actual,
                        String.format("EXECUTION ERROR in file [%s]. Test: '%s'. Register x%d mismatch.", test.sourceFile, test.name, reg));
            });
        }
    }

    private BigInteger parseBigInt(String valStr) {
        if (valStr.startsWith("0x") || valStr.startsWith("-0x")) {
            boolean negative = valStr.startsWith("-");
            String cleanHex = valStr.replace("-", "").replace("0x", "");
            BigInteger val = new BigInteger(cleanHex, 16);
            return negative ? val.negate() : val;
        }
        return new BigInteger(valStr);
    }
}
