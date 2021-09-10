package chip.input;

public class Key {
    private int realKey;
    private int emulatedKey;
    
    public Key(int realKey, int emulatedKey) {
        this.realKey = realKey;
        this.emulatedKey = emulatedKey;
    }
    
    @Override 
    public String toString() {
        return "Key - realKey: " + realKey + ", emulatedKey: " + emulatedKey;
    }
    
    public int getRealKey() {
        return realKey;
    }
    
    public int getEmulatedKey() {
        return emulatedKey;
    }
    
    public void setRealKey(int realKey) {
        this.realKey = realKey;
    }
    
    public void setEmulatedKey(int emulatedKey) {
        this.emulatedKey = emulatedKey;
    }
}
