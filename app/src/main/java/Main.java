import java.math.BigInteger;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import types.*;


public class Main {

    private static void printState(ProcessorState) {
        // TODO
    }
    private static ProcessorState elfHeaderParse(RandomAccessFile sc) {
        byte[] head = new byte[16];
        sc.seek(0);
        raf.readFully(head);
        if (head[0] != 0x7F || head[1] != 'E' || head[2] != 'L' || head[3] != 'F') {
            System.err.println("Not an ELF file");
            System.exit(1);
        }
        int bits = head[4] & 0xFF;
        int endiannes = head[5] & 0xFF;
        ByteOrder order = (ei_data == 1) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;  // ball knowledge
        ProcessorState state = new ProcessState(32 * bits, endiannes);
        if (bits == 1) {
            sc.seek(16);
            byte rest[] = new byte[36];
            sc.readFully(rest);
            ByteBuffer bb = ByteBuffer.wrap(rest).order(order);
            assert (b.getShort() == 2); // supporting only exec
            assert (b.getShort() == 0xf3); // riscv arch
            int ver = b.getInt();
            int entr = b.getInt();
            int head_offs = b.getInt();
            int sect_offs = b.getInt();
            int flags = b.getInt(); // should be checked for unsupported bs
            assert (b.getShort() == 52); // 32 bit elf header
            assert (b.getShort() == 32); // 32 bit program header
            int head_num = b.getShort();
            assert(b.getShort() == 40); // section header entry size 32 bit
            int sect_num = b.getShort();
            int string_table = b.getShort();


        }
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

        RVWord testWord = new RVWord(BigInteger.valueOf(32));
        System.out.printf("types.RVWord xlen = %d\n", testWord.getXlen());
    }
}
