package chip.system;

import java.util.Random;

import javax.sound.sampled.LineUnavailableException;

import chip.main.*;
import chip.input.*;
import chip.sound.Sound;

public class CPU {
    
    public static Random random;
    
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
    
    public int speed;
    
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
        speed = 10;
        
        random = new Random();
        
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
        for (int i = 0; i < speed; i++) {
            //System.out.println(pc);
            if (pc >= memory.length) pc = (byte) (memory.length);
            
            if (!paused) {
                byte opcode = (byte) (memory[pc] << 8 | memory[pc + 1]);
                
                try {
                    executeInstruction(opcode);
                } catch (Exception e) {}
            }
        }
        
        if (!paused) {
            updateTimers();
        }
        
        try {
            playSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public void executeInstruction(byte opcode) throws Exception {
        pc += 2;
        
        byte x = (byte) ((opcode & 0x0F00) >> 8);
        byte y = (byte) ((opcode & 0x00F0) >> 4);
        
        switch (opcode & 0xF000) {
            case 0x0000:
                switch (opcode) {
            case (byte) 0x00E0: // CLS - Clear Screen
                Main.screen.clear();
                break;
            case (byte) 0x00EE: // RET - Pop stack
                short[] oldStack = stack;
                
                stack = new short[oldStack.length - 1];
                sp--;
                
                for (int i = 0; i < stack.length; i++) {
                    stack[i] = oldStack[i];
                }
                
                break;
        }

            break;
        case 0x1000: // JP - Jump to nnn address
            pc = (byte) (opcode & 0xFFF);
            break;
        case 0x2000: // CALL - Call addr and push to stack pc value
            short[] oldStack = stack;
                
            stack = new short[oldStack.length + 1];
            sp++;
            
            for (int i = 0; i < stack.length; i++) {
                stack[i] = oldStack[i];
            }
            
            stack[stack.length - 1] = pc;
            break;
        case 0x3000: // SE Vx, byte
            if (regs[x] == (opcode & 0xFF))
                pc += 2;
            break;
        case 0x4000: // SNE Vx, byte
            if (regs[x] != (opcode & 0xFF))
                pc += 2;
            break;
        case 0x5000: // SE Vx, Vy
            if (regs[x] == regs[y])
                pc += 2;
            break;
        case 0x6000: // LD Vx, byte
            regs[x] = (byte) (opcode & 0xFF);
            break;
        case 0x7000: // ADD Vx, byte
            regs[x] += (opcode & 0xFF);
            break;
        case 0x8000:
            switch (opcode & 0xF) {
                case 0x0: // LD Vx, Vy
                    regs[x] = regs[y];
                    break;
                case 0x1: // OR Vx, Vy
                    regs[x] |= regs[y];
                    break;
                case 0x2: // AND Vx, Vy
                    regs[x] &= regs[y];
                    break;
                case 0x3: // XOR Vx, Vy
                    regs[x] ^= regs[y];
                    break;
                case 0x4: // ADD Vx, Vy
                    byte sum = (byte) (regs[x] += regs[y]);
                    
                    regs[0xF] = 0;
                    
                    if (sum > 0xFF) // Greater than 1 byte
                        regs[0xF] = 1;
                    
                    regs[x] = sum;
                    break;
                case 0x5: // SUB Vx, Vy
                    regs[0xF] = 0;
                    
                    if (regs[x] > regs[y])
                        regs[0xF] = 1;
                    
                    regs[x] -= regs[y];
                    
                    if (regs[x] < 0) { // Handle negative
                        byte off = regs[x] *= -1;
                        
                        regs[x] = (byte) (255 - off);
                    }
                    break;
                case 0x6: // SHR Vx, Vy
                    regs[0xF] = (byte) (regs[x] & 0x1);
                    
                    regs[x] >>= 1;
                    break;
                case 0x7: // SUBN Vx, Vy
                    regs[0xF] = 0;
                    
                    if (regs[y] > regs[x])
                        regs[0xF] = 1;
                    
                    regs[x] = (byte) (regs[y] - regs[x]);
                    break;
                case 0xE: // SHL Vx, Vy
                    regs[0xF] = (byte) (regs[x] & 0x80);
                    
                    regs[x] <<= 1;
                    break;
            }

            break;
        case 0x9000: // SNE Vx, Vy
            if (regs[x] != regs[y])
                pc += 2;
            break;
        case 0xA000: // LD I, addr
            i = (byte) (opcode & 0xFFF);
            break;
        case 0xB000: // JP V0, addr
            pc = (byte) ((opcode & 0xFFF) + regs[0]);
            break;
        case 0xC000: // RND Vx, byte
            regs[x] = (byte) (random.nextInt(255) & (opcode & 0xFF));
            break;
        case 0xD000: // DRW Vx, Vy, nibble
            byte width = 8;
            byte height = (byte) (opcode & 0xF);
            
            regs[0xF] = 0;
            
            for (int row = 0; row < height; row++) {
                byte sprite = memory[i + row];
                
                for (int col = 0; col < width; col++) {
                    if ((sprite & 0x80) > 0) {
                        if (Main.screen.setPixel(regs[x] + col, regs[y] + row))
                            regs[0xF] = 1;
                    }
                    
                    sprite <<= 1;
                }
            }
            break;
        case 0xE000:
            switch (opcode & 0xFF) {
                case 0x9E: // SKP Vx
                    if (Keyboard.getKeyPressedEmulated() == regs[x])
                        pc += 2;
                    break;
                case 0xA1: // SKNP Vx
                    if (Keyboard.getKeyPressedEmulated() =!= regs[x])
                        pc += 2;
                    break;
            }

            break;
        case 0xF000:
            switch (opcode & 0xFF) {
                case 0x07:
                    break;
                case 0x0A:
                    break;
                case 0x15:
                    break;
                case 0x18:
                    break;
                case 0x1E:
                    break;
                case 0x29:
                    break;
                case 0x33:
                    break;
                case 0x55:
                    break;
                case 0x65:
                    break;
            }

            break;

        default:
            throw new Exception("Unknown Opcode" + opcode);
        }
    }
    
    public void updateTimers() {
        if (dt > 0) dt--;
        if (st > 0) st--;
    }
    
    public void playSound() throws LineUnavailableException {
        if (st > 0) Sound.tone(4000, 500);
    }
}
