import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

public class TestCase {
    public String name;
    public String mnemonic;
    public int xlen;
    public String instruction_hex;
    public Map<String, String> regs_in;
    public Map<String, String> regs_out;
    public Map<String, String> mem_in;
    public Map<String, String> mem_out;

    @JsonIgnore
    public String sourceFile;

    @Override
    public String toString() {
        return name + " (" + sourceFile + ")";
    }
}
