package chip.system;

public class CPU {
    
    public int[] sprites = {
        0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
        0x20, 0x60, 0x20, 0x20, 0x70, // 1
        0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
        0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
        0x90, 0x90, 0xF0, 0x10, 0x10, // 4
        0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
        0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
        0xF0, 0x10, 0x20, 0x40, 0x40, // 7
        0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
        0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
        0xF0, 0x90, 0xF0, 0x90, 0x90, // A
        0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
        0xF0, 0x80, 0x80, 0x80, 0xF0, // C
        0xE0, 0x90, 0x90, 0x90, 0xE0, // D
        0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
        0xF0, 0x80, 0xF0, 0x80, 0x80  // F
    };
    
    public byte[] memory;
    public byte[] regs;
    
    public short[] stack;
    
    public int dt;
    public int st;
    
    public short i;
    
    public short pc;
    public byte  sp;
    
    public boolean paused;
    
    public CPU() {
        memory = new byte[4096];
        regs = new byte[16];
        
        stack = new short[16];
        
        dt = 0;
        st = 0;
        
        i = 0;
        
        pc = 0x200;
        sp = 0;
        
        paused = false;
        
        // Load sprites into memory
        
        for (int i = 0; i < sprites.length; i++) {
            memory[i] = (byte) sprites[i];
        }
    }
    
    public synchronized void load(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            memory[0x200 + i] = buffer[i];
        }
    }
    
    // Esse método performa o clock do Chip-8.
    public void clock() {
        if (!paused) {
            
        }
    }
}
