package types;


public interface Segment {}

public class Segment32 implements Segment {
    int p_type;
    int p_offset;
    int p_vaddr;
    int p_paddr;
    int p_filesz;
    int p_memsz;
    int p_flags;
    int p_align;
}

public class Segment64 implements Segment {
    int p_type;
    int p_offset;
    long p_vaddr;
    long p_paddr;
    long p_filesz;
    long p_memsz;
    long p_flags;
    long p_align;
}
