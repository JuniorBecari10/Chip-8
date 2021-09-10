package chip.system;

public class CPU {
    public byte[] memory;
    public byte[] regs;
    
    public short[] stack;
    
    public int dt;
    public int st;
    
    public short i;
    
    public short pc;
    public byte  sp;
    
    public CPU() {
        memory = new byte[4096];
        regs = new byte[16];
        
        stack = new short[16];
        
        dt = 0;
        st = 0;
        
        i = 0;
        
        pc = 0;
        sp = 0;
    }
    
    // Esse método performa o clock do Chip-8.
    public void clock() {
        
    }
}
