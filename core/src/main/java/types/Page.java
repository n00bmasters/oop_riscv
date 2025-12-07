package types;

public class Page {
    private final int size;
    private int permissions = 0;
    private final byte[] data;

    public Page(int size, int permissions) {
        this.size = size;
        this.data = new byte[size];
        this.permissions = permissions;
    }

    public boolean hasPermission(int flag) {
        return (permissions & flag) != 0;
    }

    public void rawWrite(int offset, byte val) {
        data[offset] = val;
    }

    public byte rawRead(int offset) {
        return data[offset];
    }
}
