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

    private static ProcessorState elfHeaderParse(RandomAccessFile sc) {
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
        if (bits == 1) {
            sc.seek(16);
            byte[] rest = new byte[36];
            sc.readFully(rest);
            ByteBuffer b = ByteBuffer.wrap(rest).order(order);
            assert (b.getShort() == 2); // supporting only exec
            assert (b.getShort() == 0xf3); // riscv arch
            int ver = b.getInt();
            int entr = b.getInt();
            int head_offs = b.getInt();
            int sect_offs = b.getInt();
            int flags = b.getInt(); // should be checked for unsupported bs
            assert (b.getShort() == 52); // 32 bit elf header
            assert (b.getShort() == 32); // 32 bit program header
            short head_num = b.getShort();
            assert(b.getShort() == 40); // section header entry size 32 bit
            short sect_num = b.getShort();
            short string_table = b.getShort();

            for (short i = 0; i < head_num; i++) {
                long sect_offs_real = Integer.toUnsignedLong(sect_offs) + (long) i * 40;
                sc.seek(sect_offs_real);
                byte[] sh = new byte[40];
                sc.readFully(sh);
                ByteBuffer sb = ByteBuffer.wrap(sh).order(order);
                int sh_name   = sb.getInt();
                int sh_type   = sb.getInt();
                int sh_flags  = sb.getInt();
                int sh_addr   = sb.getInt();
                int sh_offset = sb.getInt();
                int sh_size   = sb.getInt();
                int sh_link   = sb.getInt();
                int sh_info   = sb.getInt();
                int sh_addralign = sb.getInt();
                int sh_entsize   = sb.getInt();
                state.addSection(new Section32(sh_name, sh_type, sh_flags, sh_addr, sh_offset, sh_size, sh_link, sh_info, sh_addralign, sh_entsize));
            }
            Section32 shstr = (Section32) state.getSection(Short.toUnsignedInt(string_table));
            byte[] strtab = new byte[shstr.sh_size];
            sc.seek(Integer.toUnsignedLong(shstr.sh_offset));
            sc.readFully(strtab);
            for (int i = 0; i < state.getSectionCount(); i++) {
                Section32 tmp_s = (Section32) state.getSection(i);
                String name = (strtab == null) ? "" : readStringAt(strtab, tmp_s.sh_name); //check
                tmp_s.name = name;
            } // section part finished, not sure for what purpose but whtvr

            sc.seek(Integer.toUnsignedLong(head_offs));
            for (short i = 0; i < head_num; i++) {
                Segment32 ph = new Segment32();
                ph.p_type   = sc.readInt();
                ph.p_offset = sc.readInt();
                ph.p_vaddr  = sc.readInt();
                ph.p_paddr  = sc.readInt();
                ph.p_filesz = sc.readInt();
                ph.p_memsz  = sc.readInt();
                ph.p_flags  = sc.readInt();
                ph.p_align  = sc.readInt();
                state.addSegment(ph);
            }
        }
        return state;
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
            elfHeaderParse(raf);
        }

        // RVWord testWord = new RVWord(BigInteger.valueOf(32));
        // System.out.printf("types.RVWord xlen = %d\n", testWord.getXlen());
    }
}
