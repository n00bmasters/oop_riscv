package decoder;
import instruction_formats.Instruction;
import types.ProcessorState;

public class InstructionDecoder {
    ProcessorState processorState;

    public int fetch() {
        return 0x0;
    }

    public Instruction decode(int instructionWord) {
        ExtensionIDecoder decoder = new ExtensionIDecoder();
        return decoder.decode(instructionWord);
    }

    public void fetchAndDecode() {
        // TODO: Implement fetchAndDecode
    }
}
