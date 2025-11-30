package types;

import java.utils.List;
import java.jutils.ArrayList;

public class Memory{
    private List<Section> sections = new ArrayList<>();
    private List<Segment> segments = new ArrayList<>();
    private xlen;

    public Memory(int xlen) {
        this.xlen = xlen;
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

    public Segment addSegment(int ind){
        return segments.get(ind);
    }
}