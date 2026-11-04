package heyitsap.AlgData.compression.Objects;

public class LZToken {
    public int distance; // How many steps it needs to back
    public int length; // How many steps forward after it has gone back it needs to copy
    public byte character;// The actual
    public boolean isMatch;

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
