package chip.input;

import java.awt.event.*;

/*import java.util.List;
import java.util.ArrayList;*/

public class Keyboard extends KeyAdapter {
    
    //public static List<Key> pressed = new ArrayList<>();
    
    private static boolean isKeyPressed;
    private static Key keyPressed;
    
    public static boolean isKeyPressed() {
        return isKeyPressed;
    }
    
    public static Key keyPressed() {
        return keyPressed;
    }
    
    public static int getKeyPressedEmulated() {
        return keyPressed.getEmulatedKey();
    }
    
    public static Key[] keys = {
        new Key(KeyEvent.VK_1, 0x1),
        new Key(KeyEvent.VK_2, 0x2),
        new Key(KeyEvent.VK_3, 0x3),
        new Key(KeyEvent.VK_4, 0xC),
        new Key(KeyEvent.VK_Q, 0x4),
        new Key(KeyEvent.VK_W, 0x5),
        new Key(KeyEvent.VK_E, 0x6),
        new Key(KeyEvent.VK_R, 0xD),
        new Key(KeyEvent.VK_A, 0x7),
        new Key(KeyEvent.VK_S, 0x8),
        new Key(KeyEvent.VK_D, 0x9),
        new Key(KeyEvent.VK_F, 0xE),
        new Key(KeyEvent.VK_Z, 0xA),
        new Key(KeyEvent.VK_X, 0x0),
        new Key(KeyEvent.VK_C, 0xB),
        new Key(KeyEvent.VK_V, 0xF),
    };
    
    public Key onNextKeyPress() {
        return keyPressed;
    }
    
    @Override 
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();
        
        isKeyPressed = true;
        
        for (Key k : keys) {
            if (k.getRealKey() == kc) keyPressed = k;
        }
    }
    
    @Override 
    public void keyReleased(KeyEvent e) {
        int kc = e.getKeyCode();
        
        isKeyPressed = false;
        
        for (Key k : keys) {
            if (k.getRealKey() == kc) keyPressed = k;
        }
    }
}
