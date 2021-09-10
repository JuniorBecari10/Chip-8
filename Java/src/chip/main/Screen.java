package chip.main;

import chip.input.*;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.util.Arrays;

import javax.swing.JFrame;

public class Screen extends Canvas {
    
    public BufferedImage image;
    
    public JFrame frame;
    
    public static final int width = 64;
    public static final int height = 32;
    public static final int scale = 10;
    
    public static final Color offColor = Color.black; // não pretendo deixar como final
    public static final Color onColor = Color.white;
    
    public static final String title = "Chip-8";
    
    public boolean[] scr;
    
    public Screen() {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        initScreen(new Dimension(width * scale, height * scale));
        
        scr = new boolean[width * height];
        
        Arrays.fill(scr, false);
        
        addKeyListener(new Keyboard());
        
        /*setPixel(0, 0);
        setPixel(5, 2);*/
    }
    
    public void initScreen(Dimension d) {
        setPreferredSize(d);
        
        frame = new JFrame(title);
        
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    public boolean setPixel(int x, int y) {
        if (x < 0 || x > width) throw new IndexOutOfBoundsException("O x não pode ser menor que 0 nem maior que a width!");
        if (y < 0 || y > height) throw new IndexOutOfBoundsException("O y não pode ser menor que 0 nem maior que a height!");
        
        scr[x + (y * width)] ^= true;
        
        return !scr[x + (y * width)];
    }
    
    public void clear() {
        Arrays.fill(scr, false);
    }
}
