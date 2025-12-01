package types;

import java.util.List;
import java.util.ArrayList;

public class Memory{
    private List<Section> sections = new ArrayList<>();
    private List<Segment> segments = new ArrayList<>();
    private final int xlen;

    public Memory(int bits) {
        this.xlen = bits;
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

    public void writeMemory(RVWord address, RVWord value){

    }

    public RVWord readMemory(RVWord address) {
        return null;
    }
}