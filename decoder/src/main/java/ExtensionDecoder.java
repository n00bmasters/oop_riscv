import instruction_formats.Instruction;

public interface ExtensionDecoder {
    Instruction decode (int instructionWord);
}

