package types;

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
