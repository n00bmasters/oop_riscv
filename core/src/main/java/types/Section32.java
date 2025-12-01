package types;

public class Section32 implements Section {
    public String name;
    public int sh_name; public int sh_type; public int sh_flags; public int sh_addr; public int sh_offset; public int sh_size;
    public int sh_link; public int sh_info; public int sh_addralign; public int sh_entsize;
    public Section32(int a,int b,int c,int d,int e,int f,int g,int h,int i,int j){
        sh_name=a;sh_type=b;sh_flags=c;sh_addr=d;sh_offset=e;sh_size=f;sh_link=g;sh_info=h;sh_addralign=i;sh_entsize=j;
    }
}
