package heyitsap.AlgData.compression;

public class LZToken {
    int distance; // How many steps it needs to back
    int length; // How many steps forward after it has gone back it needs to copy
    byte character;// The actual
    boolean isMatch;

    // Literal, just character
    public LZToken(byte character){
        this.isMatch = false;
        this.character = character;
        this.distance= 0;
        this.length = 0;
    }
    // Match
    public LZToken(int distance, int length){
        this.character = (byte) 0;
        this.distance = distance;
        this.length = length;
        this.isMatch = true;
    }

}
