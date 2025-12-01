package types;

public class Section32 implements Section {
    String name;
    int sh_name; int sh_type; int sh_flags; int sh_addr; int sh_offset; int sh_size;
    int sh_link; int sh_info; int sh_addralign; int sh_entsize;
    public Section32(int a,int b,int c,int d,int e,int f,int g,int h,int i,int j){
        sh_name=a;sh_type=b;sh_flags=c;sh_addr=d;sh_offset=e;sh_size=f;sh_link=g;sh_info=h;sh_addralign=i;sh_entsize=j;
    }
}
