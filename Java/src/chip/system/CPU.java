package chip.system;

import javax.sound.sampled.LineUnavailableException;

import chip.main.*;
import chip.sound.Sound;

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
            if (!paused) {
                byte opcode = (byte) (memory[pc] << 8 | memory[pc + 1]);
                executeInstruction(opcode);
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
    
    public void executeInstruction(byte opcode) {
        pc += 2;
        
        byte x = (opcode & 0x0F00) >> 8;
        byte y = (opcode & 0x00F0) >> 4;
        
        switch (opcode & 0xF000) {
            case 0x0000:
                switch (opcode) {
            case 0x00E0:
                break;
            case 0x00EE:
                break;
        }

            break;
        case 0x1000:
            break;
        case 0x2000:
            break;
        case 0x3000:
            break;
        case 0x4000:
            break;
        case 0x5000:
            break;
        case 0x6000:
            break;
        case 0x7000:
            break;
        case 0x8000:
            switch (opcode & 0xF) {
                case 0x0:
                    break;
                case 0x1:
                    break;
                case 0x2:
                    break;
                case 0x3:
                    break;
                case 0x4:
                    break;
                case 0x5:
                    break;
                case 0x6:
                    break;
                case 0x7:
                    break;
                case 0xE:
                    break;
            }

            break;
        case 0x9000:
            break;
        case 0xA000:
            break;
        case 0xB000:
            break;
        case 0xC000:
            break;
        case 0xD000:
            break;
        case 0xE000:
            switch (opcode & 0xFF) {
                case 0x9E:
                    break;
                case 0xA1:
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
