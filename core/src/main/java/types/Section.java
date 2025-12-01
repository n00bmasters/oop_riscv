package types;

import java.utils.String;

public interface Section {}

public class Section64 implements Section{
    private String name;
    int sh_name; int sh_type; long sh_flags; long sh_addr; long sh_offset; long sh_size;
    int sh_link; int sh_info; long sh_addralign; long sh_entsize;
    public Section64(int a,int b,long c,long d,long e,long f,int g,int h,long i,long j){
        sh_name=a;sh_type=b;sh_flags=c;sh_addr=d;sh_offset=e;sh_size=f;sh_link=g;sh_info=h;sh_addralign=i;sh_entsize=j;
    }

}

public class Section32 implements SEction {
    String name;
    int sh_name; int sh_type; int sh_flags; int sh_addr; int sh_offset; int sh_size;
    int sh_link; int sh_info; int sh_addralign; int sh_entsize;
    public Section32(int a,int b,int c,int d,int e,int f,int g,int h,int i,int j){
        sh_name=a;sh_type=b;sh_flags=c;sh_addr=d;sh_offset=e;sh_size=f;sh_link=g;sh_info=h;sh_addralign=i;sh_entsize=j;
    }
}


