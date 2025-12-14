package types;

import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileWriter;


public class Memory{
    List<Section> sections = new ArrayList<>();
    List<Segment> segments = new ArrayList<>();
    private final Map<RVWord, Page> pages = new HashMap<>();
    private final int xlen;
    private static final RVWord pageSize = new RVWord(new BigInteger("4096"));
    private static final RVWord offsetMask = pageSize.sub(new RVWord(BigInteger.ONE));
    private static final RVWord pageMask = offsetMask.not();

    public Memory(int bits) {
        this.xlen = bits;
    }

    public Page mapPage(RVWord vaddr, int flags){
        RVWord pageKey = vaddr.and(pageMask);
        Page page = this.pages.computeIfAbsent(pageKey, k -> new Page(4096, flags));
        return page;
    }

    public void addSection(Section section){
        sections.add(section);
    }

    public void addSegment(Segment segment){
        segments.add(segment);
    }

    public Section getSection(int ind){
        return sections.get(ind);
    }

    public Segment getSegment(int ind){
        return segments.get(ind);
    }

    public void writeMemory(RVWord address, RVWord value, int size) {
        byte[] bytes = ByteBuffer.allocate(8)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putLong(value.getValue().longValue())
            .array();

        for (int i = 0; i < size; i++) {
            RVWord offset = new RVWord(BigInteger.valueOf(i));
            RVWord byteAddr = address.add(offset);
            directWrite(byteAddr, bytes[i]);
        }
    }

    public RVWord readMemory(RVWord address, int size) {
        long value = 0;
        for (int i = 0; i < size; i++) {
            RVWord offset = new RVWord(BigInteger.valueOf(i));
            RVWord byteAddr = address.add(offset);
            byte b = directRead(byteAddr);
            long unsignedByte = Byte.toUnsignedLong(b);
            value |= (unsignedByte << (i * 8));
        }

        return new RVWord(BigInteger.valueOf(value));
    }

    private void directWrite(RVWord vaddr, byte val) {
        RVWord pageKey = vaddr.and(pageMask);
        int offset = vaddr.and(offsetMask).getValue().intValue();
        Page p = checkPerms(pageKey, 2, vaddr);
        p.rawWrite(offset, val);
    }

    private byte directRead(RVWord vaddr) {
        RVWord pageKey = vaddr.and(pageMask);
        int offset = vaddr.and(offsetMask).getValue().intValue();
        Page p = checkPerms(pageKey, 4, vaddr);
        return p.rawRead(offset);
    }

    private Page checkPerms(RVWord pageKey, int requiredFlag, RVWord vaddr) {
        Page p = this.pages.get(pageKey);
        
        if (p == null) {
            throw new RuntimeException("Segfault: Unmapped address " + vaddr);
        }
        
        if (!p.hasPermission(requiredFlag)) {
            String op = "'";
            if (requiredFlag == 4) op = "R";
            if (requiredFlag == 2) op = "W";
            if (requiredFlag == 1) op = "X";
            
            throw new RuntimeException("Segfault: " + op + " permission denied at " + vaddr);
        }
        return p;
    } 

    public void initBytes(RVWord vaddr, byte val) {
        RVWord pageKey = vaddr.and(pageMask);
        int offset = vaddr.and(offsetMask).getValue().intValue();
        Page page = this.pages.get(pageKey);
        if (page == null) {
            page = mapPage(vaddr, 6); 
        }
        page.rawWrite(offset, val);
    }

    public byte readByte(RVWord vaddr) {
        RVWord pageKey = vaddr.and(pageMask);
        int offset = vaddr.and(offsetMask).getValue().intValue();
        Page page = this.pages.get(pageKey);
        if (page == null) {
            return 0; // or throw exception
        }
        return page.rawRead(offset);
    }

    public void dumpToFile(String filename) throws IOException {
    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
        for (var entry : pages.entrySet()) {
            RVWord addr = entry.getKey();
            Page page = entry.getValue();

            writer.printf("0x%x (Flags: %s)\n", addr.getValue(), "zaglushka.");

            for (int i = 0; i < 4096; i++) {
                byte b = page.rawRead(i);
                
                writer.printf("%02X ", b);
                
                if ((i + 1) % 16 == 0) {
                    writer.println();
                }
            }
            writer.println("\n");
        }
    }
}

}
