package types;

public class ProcessorState {
    RVWord[] registers = new RVWord[32];
    public RVWord getRegister(int index) {
        return registers[index];
    }

    public void setRegister(int index, RVWord result) {
        registers[index] = result;
    }
}
