package heyitsap.AlgData.compression;

import java.util.ArrayList;

class Token {
    int distance; // How many steps it needs to back
    int length; // How many steps forward after it has gone back it needs to copy
    char character; // The actual

    // Literal, just charcter
    public Token(char character){
        this.character = character;
        this.distance= 0;
        this.length = 0;
    }
    // Match
    public Token(int distance, int length, char next){
        this.character = next;
        this.distance = distance;
        this.length = length;
    }

}

public class LZ {
    public static void compress(Byte[] data){
        List<Token> tokens = new ArrayList<>();

    }
}
