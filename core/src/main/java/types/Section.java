package types;


public class Section {
    public String name;
    public int sh_name; public int sh_type; public long sh_flags; public long sh_addr; public long sh_offset; public long sh_size;
    public int sh_link; public int sh_info; public long sh_addralign; public long sh_entsize;
    public Section64(int a,int b,long c,long d,long e,long f,int g,int h,long i,long j){
        sh_name=a;sh_type=b;sh_flags=c;sh_addr=d;sh_offset=e;sh_size=f;sh_link=g;sh_info=h;sh_addralign=i;sh_entsize=j;
    }
}



