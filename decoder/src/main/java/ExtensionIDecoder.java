import instruction_formats.Instruction;
import types.RVWord;


public class ExtensionIDecoder implements ExtensionDecoder {
    @Override
    public Instruction decode(int instructionWord) {
        RVWord word = new RVWord(java.math.BigInteger.valueOf(instructionWord));
        int opcode = word.getBits(0, 6);
        
        switch (opcode) {
            case 0b0110011: // R-type instructions
                return decodeRType(word);
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
    
    private Instruction decodeRType(RVWord word) {
        int funct3 = word.getBits(12, 14);
        int funct7 = word.getBits(25, 31);
        
        switch (funct3) {
            case 0b000:
                if (funct7 == 0b0000000) {
                    return new Add(word);
                } else if (funct7 == 0b0100000) {
                    return new Sub(word);
                }
                break;
            case 0b001:
                if (funct7 == 0b0000000) {
                    return new Sll(word);
                }
                break;
            case 0b010:
                if (funct7 == 0b0000000) {
                    return new Slt(word);
                }
                break;
            case 0b011:
                if (funct7 == 0b0000000) {
                    return new Sltu(word);
                }
                break;
            case 0b100:
                if (funct7 == 0b0000000) {
                    return new Xor(word);
                }
                break;
            case 0b101:
                if (funct7 == 0b0000000) {
                    return new Srl(word);
                } else if (funct7 == 0b0100000) {
                    return new Sra(word);
                }
                break;
            case 0b110:
                if (funct7 == 0b0000000) {
                    return new Or(word);
                }
                break;
            case 0b111:
                if (funct7 == 0b0000000) {
                    return new And(word);
                }
                break;
        }
        return null;
    }
}