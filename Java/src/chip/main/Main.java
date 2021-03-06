package chip.main;

import java.awt.*;
import java.awt.image.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.*;
import java.nio.file.*;

import chip.system.*;
import chip.input.*;
import chip.sound.*;

public class Main implements Runnable {
    
    private boolean running = false;
    private Thread thread;
    
    // -------------------------------
    
    public static CPU cpu;
    public static Screen screen;
    
    public static String[] args;
    
    //public static Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    public Main() {
        cpu = new CPU();
        screen = new Screen();
        
        if (args == null || args.length == 0) {
            System.err.println("Cannot load ROM. Args is null or empty.");
            
            System.exit(1);
        }
        
        try {
            byte[] buffer = loadFileBuffer(new File(args[0]));
            
            cpu.load(buffer);
        } catch (IOException e) {
            System.err.println("Cannot load ROM.");
            
            System.exit(1);
        }
    }
    
    public byte[] loadFileBuffer(File file) throws IOException {
        Path p = file.toPath();
        
        return Files.readAllBytes(p);
    }
    
    public static void main(String[] args) {
        Main.args = args;
        
        new Main().start();
    }
    
    public synchronized void start() {
        thread = new Thread(this);
        running = true;
        
        thread.start();
    }
    
    public synchronized void stop() {
        running = false;
        
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void tick() {
        cpu.clock();
    }
    
    public void render() {
        BufferStrategy bs = screen.getBufferStrategy();
        
        if (bs == null) {
            screen.createBufferStrategy(3);
            
            return;
        }
        
        Graphics g = screen.image.getGraphics();
        
        g.setColor(Screen.offColor);
        g.fillRect(0, 0, Screen.width, Screen.height);
        
        ///
        
        for (int i = 0; i < Screen.width * Screen.height; i++) {
            int x = i % Screen.width;
            int y = (int) Math.floor(i / Screen.width);
            
            boolean b = screen.scr[i];
            
            g.setColor(Screen.onColor);
            
            if (b) g.fillRect(x, y, 1, 1);
        }
        
        ///
        
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(screen.image, 0, 0, Screen.width * Screen.scale, Screen.height * Screen.scale, null);
        
        bs.show();
    }
    
    @Override 
    public void run() {
        screen.requestFocus();
        
        long lastTime = System.nanoTime();
        final double targetFPS = 60.0;
        
        double ns = 1E9 / targetFPS;
        double delta = 0;
        
        int count = 0;
        double timer = System.currentTimeMillis();
        
        while (running) {
            long now = System.nanoTime();
            
            delta += (now - lastTime) / ns;
            lastTime = now;
            
            if (delta >= 1) {
                tick();
                render();
                
                delta--;
                count++;
            }
            
            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + count);
                
                count = 0;
                timer += 1000;
            }
        }
        
        stop();
    }
}
