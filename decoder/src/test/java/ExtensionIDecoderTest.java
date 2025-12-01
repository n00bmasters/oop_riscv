import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import instruction_formats.Instruction;
import java.math.BigInteger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import types.RVWord;

public class ExtensionIDecoderTest {
    
    public static java.util.stream.Stream<Integer> xlenValues() { 
        return java.util.stream.Stream.of(32, 64, 128); 
    }

    private int createRType(int rd, int rs1, int rs2, int funct3, int funct7, int opcode) {
        int word = 0;
        word |= (opcode & 0x7F);
        word |= (rd & 0x1F) << 7;
        word |= (funct3 & 0x07) << 12;
        word |= (rs1 & 0x1F) << 15;
        word |= (rs2 & 0x1F) << 20;
        word |= (funct7 & 0x7F) << 25;
        return word;
    }
    
    private int createIType(int rd, int rs1, int imm, int funct3, int opcode) {
        int word = 0;
        word |= (opcode & 0x7F);
        word |= (rd & 0x1F) << 7;
        word |= (funct3 & 0x07) << 12;
        word |= (rs1 & 0x1F) << 15;
        word |= (imm & 0xFFF) << 20;
        return word;
    }
    
    private int createSType(int rs1, int rs2, int imm, int funct3, int opcode) {
        int word = 0;
        word |= (opcode & 0x7F);
        word |= (imm & 0x1F) << 7;  // imm[4:0]
        word |= (funct3 & 0x07) << 12;
        word |= (rs1 & 0x1F) << 15;
        word |= (rs2 & 0x1F) << 20;
        word |= ((imm >> 5) & 0x7F) << 25;  // imm[11:5]
        return word;
    }
    
    @ParameterizedTest
    @MethodSource("xlenValues")
    void testDecodeAdd(int xlen) {
        RVWord.setXlen(xlen);
        ExtensionIDecoder decoder = new ExtensionIDecoder();
        
        // Create an ADD instruction: add x10, x11, x12
        int instructionWord = createRType(10, 11, 12, 0, 0, 0b0110011);
        
        Instruction instruction = decoder.decode(instructionWord);
        assertNotNull(instruction);
        assertEquals(Add.class, instruction.getClass());
    }
    
    @ParameterizedTest
    @MethodSource("xlenValues")
    void testDecodeSub(int xlen) {
        RVWord.setXlen(xlen);
        ExtensionIDecoder decoder = new ExtensionIDecoder();
        
        // Create a SUB instruction: sub x10, x11, x12
        int instructionWord = createRType(10, 11, 12, 0, 0x20, 0b0110011);
        
        Instruction instruction = decoder.decode(instructionWord);
        assertNotNull(instruction);
        assertEquals(Sub.class, instruction.getClass());
    }
    
    @ParameterizedTest
    @MethodSource("xlenValues")
    void testDecodeSll(int xlen) {
        RVWord.setXlen(xlen);
        ExtensionIDecoder decoder = new ExtensionIDecoder();
        
        // Create a SLL instruction: sll x10, x11, x12
        int instructionWord = createRType(10, 11, 12, 1, 0, 0b0110011);
        
        Instruction instruction = decoder.decode(instructionWord);
        assertNotNull(instruction);
        assertEquals(Sll.class, instruction.getClass());
    }
    
    @ParameterizedTest
    @MethodSource("xlenValues")
    void testDecodeITypeInstruction(int xlen) {
        RVWord.setXlen(xlen);
        ExtensionIDecoder decoder = new ExtensionIDecoder();
        
        // Create an ADDI instruction: addi x10, x11, 5
        int instructionWord = createIType(10, 11, 5, 0, 0b0010011);
        
        // For now, I-type instructions are recognized but not fully decoded
        Instruction instruction = decoder.decode(instructionWord);
        assertNull(instruction);
    }
    
    @ParameterizedTest
    @MethodSource("xlenValues")
    void testDecodeSTypeInstruction(int xlen) {
        RVWord.setXlen(xlen);
        ExtensionIDecoder decoder = new ExtensionIDecoder();
        
        // Create a SW instruction: sw x11, 8(x12)
        int instructionWord = createSType(12, 11, 8, 2, 0b0100011);
        
        // For now, S-type instructions are recognized but not fully decoded
        Instruction instruction = decoder.decode(instructionWord);
        assertNull(instruction);
    }
    
    @ParameterizedTest
    @MethodSource("xlenValues")
    void testDecodeUnsupportedOpcode(int xlen) {
        RVWord.setXlen(xlen);
        ExtensionIDecoder decoder = new ExtensionIDecoder();
        
        // Create an undefined opcode instruction
        int instructionWord = 0xFFFFFFFF;
        
        Instruction instruction = decoder.decode(instructionWord);
        assertNull(instruction);
    }
}