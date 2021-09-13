package chip.sound;

import javax.sound.sampled.*;

// Fonte: StackOverflow
public class Sound {
    
    public static final float sampleRate = 8000f; // 440
    
    public static void tone(int hz, int msecs) throws LineUnavailableException {
        tone(hz, msecs, 1.0);
    }
    
    public static void tone(int hz, int msecs, double vol) throws LineUnavailableException {
        byte[] buf = new byte[1];
        
        AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        
        sdl.open(af);
        sdl.start();
        
        for (int i = 0; i < msecs * 8; i++) {
            double angle = i / (sampleRate/ hz);
            buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
            sdl.write(buf, 0, 1);
        }
        
        sdl.drain();
        sdl.stop();
        sdl.close();
    }
}
