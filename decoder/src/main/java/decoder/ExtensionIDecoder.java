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
            case 0b0111011: // R-type instructions (RV64I)
                return decodeRType64(instructionWord);
            case 0b0010011: // I-type arithmetic instructions
                return decodeITypeArithmetic(instructionWord);
            case 0b0011011: // I-type arithmetic instructions (RV64I)
                return decodeITypeArithmetic64(instructionWord);
            case 0b0000011: // I-type load instructions
                return decodeITypeLoad(instructionWord);
            case 0b1100011: // B-type branch instructions
                return decodeBType(instructionWord);
            case 0b1100111: // I-type jump instructions
                return decodeITypeJump(instructionWord);
            case 0b0100011: // S-type store instructions
                return decodeSType(instructionWord);
            case 0b1101111: // J-type jump instructions
                return decodeJType(instructionWord);
            case 0b0110111: // U-type LUI instruction
                return decodeUTypeLUI(instructionWord);
            case 0b0010111: // U-type AUIPC instruction
                return decodeUTypeAUIPC(instructionWord);
            case 0b1110011: // I-type system instructions
                return decodeITypeSystem(instructionWord);
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
    
    private Instruction decodeRType64(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        int funct7 = getBits(instructionWord, 25, 31);
        
        switch (funct3) {
            case 0b000:
                if (funct7 == 0b0000000) {
                    return new Addw(instructionWord);
                } else if (funct7 == 0b0100000) {
                    return new Subw(instructionWord);
                }
                break;
            case 0b001:
                if (funct7 == 0b0000000) {
                    return new Sllw(instructionWord);
                }
                break;
            case 0b101:
                if (funct7 == 0b0000000) {
                    return new Srlw(instructionWord);
                } else if (funct7 == 0b0100000) {
                    return new Sraw(instructionWord);
                }
                break;
        }
        return null;
    }
    
    private Instruction decodeITypeArithmetic(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        int imm = getBits(instructionWord, 20, 24);
        
        switch (funct3) {
            case 0b000: // addi
                return new Addi(instructionWord);
            case 0b010: // slti
                return new Slti(instructionWord);
            case 0b011: // sltiu
                return new Sltiu(instructionWord);
            case 0b100: // xori
                return new Xori(instructionWord);
            case 0b110: // ori
                return new Ori(instructionWord);
            case 0b111: // andi
                return new Andi(instructionWord);
            case 0b001: // slli
                return new Slli(instructionWord);
            case 0b101: // srli, srai
                if (imm == 0b00000) {
                    return new Srli(instructionWord);
                } else if (imm == 0b10000) {
                    return new Srai(instructionWord);
                }
                break;
        }
        return null;
    }
    
    private Instruction decodeITypeArithmetic64(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        int imm = getBits(instructionWord, 20, 24);
        
        switch (funct3) {
            case 0b000: // addiw
                return new Addiw(instructionWord);
            case 0b001: // slliw
                return new Slliw(instructionWord);
            case 0b101: // srliw, sraiw
                if (imm == 0b00000) {
                    return new Srliw(instructionWord);
                } else if (imm == 0b01000) {  // Note: Different imm value for SRAIW
                    return new Sraiw(instructionWord);
                }
                break;
        }
        return null;
    }
    
    private Instruction decodeITypeLoad(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        
        switch (funct3) {
            case 0b000: // lb
                return null;
            case 0b001: // lh
                return null;
            case 0b010: // lw
                return null;
            case 0b100: // lbu
                return null;
            case 0b101: // lhu
                return null;
        }
        return null;
    }
    
    private Instruction decodeBType(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        
        switch (funct3) {
            case 0b000: // beq
                return null;
            case 0b001: // bne
                return null;
            case 0b100: // blt
                return null;
            case 0b101: // bge
                return null;
            case 0b110: // bltu
                return null;
            case 0b111: // bgeu
                return null;
        }
        return null;
    }
    
    private Instruction decodeITypeJump(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        
        if (funct3 == 0b000) {
            return null;
        }
        return null;
    }
    
    private Instruction decodeSType(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        
        switch (funct3) {
            case 0b000: // sb
                return null;
            case 0b001: // sh
                return null;
            case 0b010: // sw
                return null;
        }
        return null;
    }
    
    private Instruction decodeJType(int instructionWord) {
        return null;
    }
    
    private Instruction decodeUTypeLUI(int instructionWord) {
        return null;
    }
    
    private Instruction decodeUTypeAUIPC(int instructionWord) {
        return null;
    }
    
    private Instruction decodeITypeSystem(int instructionWord) {
        int funct3 = getBits(instructionWord, 12, 14);
        int imm = getBits(instructionWord, 20, 31);
        
        if (funct3 == 0b000) {
            if (imm == 0b000000000000) {
                return null;
            } else if (imm == 0b000000000001) {
                return null;
            }
        }
        return null;
    }
}
