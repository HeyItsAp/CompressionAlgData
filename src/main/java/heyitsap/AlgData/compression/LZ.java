package heyitsap.AlgData.compression;

import java.util.ArrayList;
import java.util.List;


class Token {
    int distance; // How many steps it needs to back
    int length; // How many steps forward after it has gone back it needs to copy
    byte character; // The actual

    // Literal, just charcter
    public Token(byte character){
        this.character = character;
        this.distance= 0;
        this.length = 0;
    }
    // Match
    public Token(int distance, int length, byte next){
        this.character = next;
        this.distance = distance;
        this.length = length;
    }

}

public class LZ {
    public static void compress(Byte[] data){
        // Modifiyal bits:
        int WindowSize = 4096;
        int MAX_LEN = 258;
        int MIN_MATCH = 3;


        List<Token> tokens = new ArrayList<>();
        int pos = 0;

        while (pos < data.length) {
            int windowStart = Math.max(0, pos - WindowSize);
            int bestOffset = 0, bestLength = 0;
            for (int j = windowStart; j < pos; j++) {
                int length = 0;
                while (
                        pos + length < data.length
                                && length < MAX_LEN
                                && data[j + length] == data[pos + length]
                ) length++;
                if (length > bestLength) {
                    bestLength = length;
                    bestOffset = pos - j;
                }
            }

            if(bestLength >= MIN_MATCH){
                tokens.add(new Token(bestOffset, bestLength, data[pos]));
                pos += bestLength;
            } else {
                tokens.add(new Token(data[pos]));
                pos++;
            }
        }
    }
}
