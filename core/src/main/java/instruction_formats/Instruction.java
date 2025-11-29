package instruction_formats;
import types.ProcessorState;

public interface Instruction {
    void execute (ProcessorState state);
}