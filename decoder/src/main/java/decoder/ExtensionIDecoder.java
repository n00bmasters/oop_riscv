package decoder;

import instruction_formats.Instruction;
import arithmetic.*;
import shift.*;
import comparison.*;
import logical.*;
import static types.Utils.getBits;


public class ExtensionIDecoder implements ExtensionDecoder {
    @Override
    public Instruction decode(int instructionWord) {
        int opcode = getBits(instructionWord, 0, 6);
        
        switch (opcode) {
            case 0b0110011: // R-type instructions
                return decodeRType(instructionWord);
            case 0b0010011: // I-type arithmetic instructions
                return null;
            case 0b0000011: // I-type load instructions
                return null;
            case 0b1100011: // B-type branch instructions
                return null;
            case 0b1100111: // I-type jump instructions
                return null;
            case 0b0100011: // S-type store instructions
                return null;
            case 0b1101111: // J-type jump instructions
                return null;
            case 0b0110111: // U-type LUI instruction
                return null;
            case 0b0010111: // U-type AUIPC instruction
                return null;
            case 0b1110011: // I-type system instructions
                return null;
            default:
                return null;
        }
    }
    
    private Instruction decodeRType(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        int funct7 = getBits(instructionWord, 25, 31);
        
        switch (funct3) {
            case 0b000:
                if (funct7 == 0b0000000) {
                    return new Add(instructionWord);
                } else if (funct7 == 0b0100000) {
                    return new Sub(instructionWord);
                }
                break;
            case 0b001:
                if (funct7 == 0b0000000) {
                    return new Sll(instructionWord);
                }
                break;
            case 0b010:
                if (funct7 == 0b0000000) {
                    return new Slt(instructionWord);
                }
                break;
            case 0b011:
                if (funct7 == 0b0000000) {
                    return new Sltu(instructionWord);
                }
                break;
            case 0b100:
                if (funct7 == 0b0000000) {
                    return new Xor(instructionWord);
                }
                break;
            case 0b101:
                if (funct7 == 0b0000000) {
                    return new Srl(instructionWord);
                } else if (funct7 == 0b0100000) {
                    return new Sra(instructionWord);
                }
                break;
            case 0b110:
                if (funct7 == 0b0000000) {
                    return new Or(instructionWord);
                }
                break;
            case 0b111:
                if (funct7 == 0b0000000) {
                    return new And(instructionWord);
                }
                break;
        }
        return null;
    }
}