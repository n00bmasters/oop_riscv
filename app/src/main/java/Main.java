import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import types.*;

public class Main {

    private static void printState(ProcessorState state) {
        // TODO
        return;
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
        ByteOrder order = (endiannes == 1) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;  // ball knowledge
        ProcessorState state = new ProcessorState(32 * bits, endiannes);

        sc.seek(16);
        byte[] rest = new byte[36];
        sc.readFully(rest);
        ByteBuffer b = ByteBuffer.wrap(rest).order(order);
        assert (b.getShort() == 2); // supporting only exec
        assert (b.getShort() == 0xf3); // riscv arch
        int ver = b.getInt();
        if (bits == 1) {
            long entr = Integer.toUnsignedLong(b.getInt()); // long in 64
            long head_offs = Integer.toUnsignedLong(b.getInt()); // long in 64
            long sect_offs = Integer.toUnsignedLong(b.getInt()); // long in 64
        } else {
            long entr = b.getLong();
            long head_offs = b.getLong();
            long sect_offs = b.getLong();
        }
        int flags = b.getInt(); // should be checked for unsupported bs
        short headSize = b.getShort(); // 32 bit elf header
        short segHeadSize = b.getShort() ; // 32 bit program header
        short head_num = b.getShort();
        short sectionHeadSize = b.getShort();
        short sect_num = b.getShort();
        short string_table = b.getShort();
        
        for (short i = 0; i < sect_num; i++) {
            long sect_offs_real = Integer.toUnsignedLong(sect_offs) + (long) i * sectionHeadSize;
            sc.seek(sect_offs_real);
            byte[] sh = new byte[sectionHeadSize];
            sc.readFully(sh);
            ByteBuffer sb = ByteBuffer.wrap(sh).order(order);
            int sh_name   = sb.getInt();
            int sh_type   = sb.getInt(); 
            if (bits == 1) {
                long sh_flags  = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                long sh_addr   = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                long sh_offset = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                long sh_size   = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
            } else {
                long sh_flags  = sb.getLong();
                long sh_addr   = sb.getLong();
                long sh_offset = sb.getLong();
                long sh_size   = sb.getLong();
            }
            int sh_link   = sb.getInt();
            int sh_info   = sb.getInt();
            if (bits == 1) {
                long sh_addralign = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
                long sh_entsize   = Integer.toUnsignedLong(sb.getInt()); // 8 in 64
            } else {
                long sh_addralign = sb.getlong();
                long sh_entsize = sb.getLong();
            }
            state.addSection(new Section(sh_name, sh_type, sh_flags, sh_addr, sh_offset, sh_size, sh_link, sh_info, sh_addralign, sh_entsize));
        }
        Section shstr = state.getSection(Short.toUnsignedInt(string_table));
        byte[] strtab = new byte[shstr.sh_size];
        sc.seek(Integer.toUnsignedLong(shstr.sh_offset));
        sc.readFully(strtab);
        for (int i = 0; i < state.getSectionCount(); i++) {
            Section32 tmp_s = (Section32) state.getSection(i);
            String name = (strtab == null) ? "" : readStringAt(strtab, tmp_s.sh_name); //check
            tmp_s.name = name == 2);
        } // section part finished, needed for reading semgents
        
        sc.seek(Integer.toUnsignedLong(head_offs));
        byte[] phData = new byte[head_num * segHeadSize];
        sc.readFully(phData);
        
        ByteBuffer phBuf = ByteBuffer.wrap(phData).order(order);
        for (short i = 0; i < head_num; i++) {
            Segment32 ph = new Segment();
            ph.p_type   = phBuf.getInt();
            ph.p_offset = phBuf.getInt();
            if (bits == 1){
                ph.p_vaddr  = Integer.toUnsignedLong(phBuf.getInt()); // this and lower are 8 in 64
                ph.p_paddr  = Integer.toUnsignedLong(phBuf.getInt());
                ph.p_filesz = Integer.toUnsignedLong(phBuf.getInt());
                ph.p_memsz  = Integer.toUnsignedLong(phBuf.getInt());
                ph.p_flags  = Integer.toUnsignedLong(phBuf.getInt());
                ph.p_align  = Integer.toUnsignedLong(phBuf.getInt());
            } else {
                ph.p_vaddr  = phBuf.getLong();
                ph.p_paddr  = phBuf.getLong();
                ph.p_filesz = phBuf.getLong();
                ph.p_memsz  = phBuf.getLong();
                ph.p_flags  = phBuf.getLong();
                ph.p_align  = phBuf.getLong();
            }
            state.addSegment(ph);
        }
    
        }
        return state;
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
            
        }
        // RVWord testWord = new RVWord(BigInteger.valueOf(32));
        // System.out.printf("types.RVWord xlen = %d\n", testWord.getXlen());
    }
}
