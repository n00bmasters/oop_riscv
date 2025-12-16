import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import decoder.InstructionDecoder;
import types.*;
import instruction_formats.Instruction;

public class Main {

    private static void printState(ProcessorState state) {
        System.out.println("bits: " + state.getXlen());
        System.out.println("endianness: " + (state.getEndian() == 1 ? "Little Endian" : "Big Endian"));
        System.out.println("sections: " + state.getSectionCount());
        System.out.println("segments: " + state.getSegmentCount());
        System.out.println("registers:");
        for (int i = 0; i < 32; i++) {
            System.out.printf("x%-2d: 0x%s\n", i, state.getRegister(i).getValue().toString(16).toUpperCase());
        }
    }
    private static ProcessorState elfHeaderParse(RandomAccessFile sc) throws IOException{
        byte[] head = new byte[16];
        sc.seek(0);
        sc.readFully(head);
        if (head[0] != 0x7F || head[1] != 'E' || head[2] != 'L' || head[3] != 'F') {
            System.err.println("Not an ELF file");
            System.exit(1);
        }
        int bits = head[4] & 0xFF;
        int endiannes = head[5] & 0xFF;
        System.out.println("Bits: " + bits + ", Endian: " + endiannes);
        ByteOrder order = (endiannes == 1) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;  // ball knowledge
        ProcessorState state = new ProcessorState(32 * bits, endiannes);

        sc.seek(16);
        byte[] rest = new byte[36];
        sc.readFully(rest);
        ByteBuffer b = ByteBuffer.wrap(rest).order(order);
        short type = b.getShort(); // supporting only exec
        short machine = b.getShort(); // riscv arch
        int ver = b.getInt();
        long entr, head_offs, sect_offs;
        if (bits == 1) {
            entr = Integer.toUnsignedLong(b.getInt()); // long in 64
            head_offs = Integer.toUnsignedLong(b.getInt()); // long in 64
            sect_offs = Integer.toUnsignedLong(b.getInt()); // long in 64
        } else {
            entr = b.getLong();
            head_offs = b.getLong();
            sect_offs = b.getLong();
        }
        int flags = b.getInt(); // should be checked for unsupported bs
        short headSize = b.getShort(); // 32 bit elf header
        short segHeadSize = b.getShort() ; // 32 bit program header
        short head_num = b.getShort();
        short sectionHeadSize = b.getShort();
        short sect_num = b.getShort();
        short string_table = b.getShort();
        
        for (short i = 0; i < sect_num; i++) {
            long sect_offs_real = sect_offs + (long) i * sectionHeadSize;
            sc.seek(sect_offs_real);
            byte[] sh = new byte[sectionHeadSize];
            sc.readFully(sh);
            ByteBuffer sb = ByteBuffer.wrap(sh).order(order);
            int sh_name   = sb.getInt();
            int sh_type   = sb.getInt();
            long sh_flags, sh_addr, sh_offset, sh_size;
            if (bits == 1) {
                sh_flags  = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                sh_addr   = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                sh_offset = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                sh_size   = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
            } else {
                sh_flags  = sb.getLong();
                sh_addr   = sb.getLong();
                sh_offset = sb.getLong();
                sh_size   = sb.getLong();
            }
            int sh_link   = sb.getInt();
            int sh_info   = sb.getInt();
            long sh_addralign, sh_entsize;
            if (bits == 1) {
                if (sb.remaining() >= 8) {
                    sh_addralign = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                    sh_entsize   = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                } else {
                    sh_addralign = 0;
                    sh_entsize = 0;
                }
            } else {
                sh_addralign = sb.getLong();
                sh_entsize = sb.getLong();
            }
            state.addSection(new Section(sh_name, sh_type, sh_flags, sh_addr, sh_offset, sh_size, sh_link, sh_info, sh_addralign, sh_entsize));
        }
        Section shstr = state.getSection(Short.toUnsignedInt(string_table));
        byte[] strtab = new byte[Math.toIntExact(shstr.sh_size)];
        sc.seek(Integer.toUnsignedLong(Math.toIntExact(shstr.sh_offset)));
        sc.readFully(strtab);
        for (int i = 0; i < state.getSectionCount(); i++) {
            Section tmp_s = state.getSection(i);
            tmp_s.name = readStringAt(strtab, tmp_s.sh_name); //check
        } // section part finished, needed for reading semgents
        
        sc.seek(Integer.toUnsignedLong(Math.toIntExact(head_offs)));
        byte[] phData = new byte[head_num * segHeadSize];
        sc.readFully(phData);
        
        ByteBuffer phBuf = ByteBuffer.wrap(phData).order(order);
        for (short i = 0; i < head_num; i++) {
            Segment ph = new Segment();
            ph.p_type   = phBuf.getInt();
            if (bits == 1){
                ph.p_offset = Integer.toUnsignedLong(phBuf.getInt());
                ph.p_vaddr  = Integer.toUnsignedLong(phBuf.getInt()); // this and lower are 8 in 64
                ph.p_paddr  = Integer.toUnsignedLong(phBuf.getInt());
                ph.p_filesz = Integer.toUnsignedLong(phBuf.getInt());
                ph.p_memsz  = Integer.toUnsignedLong(phBuf.getInt());
                ph.p_flags = phBuf.getInt();
                ph.p_align  = Integer.toUnsignedLong(phBuf.getInt());
            } else {
                ph.p_offset  = phBuf.getLong();
                ph.p_vaddr  = phBuf.getLong();
                ph.p_paddr  = phBuf.getLong();
                ph.p_filesz = phBuf.getLong();
                ph.p_memsz  = phBuf.getLong();
                ph.p_flags = (int) phBuf.getLong();
                ph.p_align  = phBuf.getLong();
            }
            System.out.println("  offset " + ph.p_offset + ", filesz " + ph.p_filesz + ", memsz " + ph.p_memsz + ", flags " + ph.p_flags);
            state.addSegment(ph);
        }
        state.setPC(new RVWord(BigInteger.valueOf(entr)));
        return state;
    }

    private static void initStack(ProcessorState state, RandomAccessFile raf) throws IOException {
        RVWord stackPointer = new RVWord(java.math.BigInteger.valueOf(0x80000000));
        int stackSize = 0x10000;
        RVWord pageSizeWord = new RVWord(java.math.BigInteger.valueOf(4096));
        RVWord currentAddr = stackPointer.sub(new RVWord(java.math.BigInteger.valueOf(stackSize)));
        while (!currentAddr.equals(stackPointer)) {
            state.mapPage(currentAddr, 6); 
            currentAddr = currentAddr.add(pageSizeWord);
        }
        state.setRegister(2, stackPointer);
        System.out.println("SP: 0x" + stackPointer.getValue().toString(16));
    } 

    private static void loadSegments(ProcessorState state, RandomAccessFile raf) throws IOException {
        int count = state.getSegmentCount(); 
        System.out.println(count);
        for (int i = 0; i < count; i++) {
            Segment seg = state.getSegment(i);
            if (seg.p_type == 1) {
                long startPage = seg.p_vaddr & ~0xFFFL;
                long endAddr = seg.p_vaddr + seg.p_memsz;

                for (long a = startPage; a < endAddr; a += 4096) {
                    RVWord pageAddr = new RVWord(BigInteger.valueOf(a));
                    state.mapPage(pageAddr, seg.p_flags);
                }

                raf.seek(seg.p_offset);
                byte[] data = new byte[(int) seg.p_filesz];
                raf.readFully(data);

                for (int j = 0; j < data.length; j++) {
                    RVWord addr = new RVWord(BigInteger.valueOf(seg.p_vaddr + j));
                    state.initByte(addr, data[j]);
                }

                for (long j = seg.p_filesz; j < seg.p_memsz; j++) {
                    RVWord addr = new RVWord(BigInteger.valueOf(seg.p_vaddr + j));
                    state.initByte(addr, (byte) 0);
                }
                
            }
        }
    }

    private static String readStringAt(byte[] table, int offset) {
        if (offset < 0 || offset >= table.length) return "";
        int i = offset;
        while (i < table.length && table[i] != 0) i++;
        return new String(table, offset, i - offset, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: java Main <program_filename>");
            System.exit(1);
        }
        File file = new File(args[0]);
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            RVWord.setXlen(32);
            ProcessorState state = elfHeaderParse(raf);
            loadSegments(state, raf);
            state.dumpMemory("dump.txt");
            initStack(state, raf); 
            InstructionDecoder decoder = new InstructionDecoder();
            int instrCount = 0;
            for (;;) {
                int instrWord = state.fetchInstruction();
                System.out.printf("PC: 0x%08X, Instr: 0x%08X\n", state.getPC().getValue().intValue(), instrWord);
                Instruction instr = decoder.decode(instrWord);
                if (instr == null || instrWord == 0x00000073 || instrWord == 0x00008067) {
                    printState(state);
                    System.err.println("Total instructions executed: " + instrCount);
                    System.err.print("Program ended: ");
                    if (instr == null) System.err.print("unknown instruction");
                    else if (instrWord == 0x00000073) System.err.print("ECALL");
                    else System.err.print("RET");
                    System.err.println();
                    System.exit(0);
                }
                instrCount++;
                instr.execute(state);
                state.setPC(state.getPC().add(new RVWord(BigInteger.valueOf(4))));
            }
        }
        // RVWord testWord = new RVWord(BigInteger.valueOf(32));
        // System.out.printf("types.RVWord xlen = %d\n", testWord.getXlen());
    }
}
