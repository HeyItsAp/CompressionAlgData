package heyitsap.AlgData.compression;

import heyitsap.AlgData.compression.Objects.BitReader;
import heyitsap.AlgData.compression.Objects.BitWriter;
import heyitsap.AlgData.compression.Objects.LZToken;
import heyitsap.AlgData.compression.Objects.Node;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LZ {
    public static List<LZToken> compress(byte[] data){
        // Modifiyal bits:
        int WindowSize = 4096;
        int MAX_LEN = 258;
        int MIN_MATCH = 3;


        List<LZToken> LZTokens = new ArrayList<>();
        int pos = 0;

        while (pos < data.length) {
            int windowStart = Math.max(0, pos - WindowSize);
            int bestOffset = 0, bestLength = 0;
            for (int j = windowStart; j < pos; j++) {
                int length = 0;
                while (pos + length < data.length
                                && length < MAX_LEN
                                && data[j + length] == data[pos + length]
                ) length++;
                if (length > bestLength) {
                    bestLength = length;
                    bestOffset = pos - j;
                }
            }

            if(bestLength >= MIN_MATCH){
                LZTokens.add(new LZToken(bestOffset, bestLength));
                pos += bestLength;
            } else {
                LZTokens.add(new LZToken(data[pos]));
                pos++;
            }
        }
        return LZTokens;
    }
    public static byte[] compressWithHuffman(byte[] data) {

        List<LZToken> tokens = LZ.compress(data);

        int[] frequencies = HuffmanEncoder.countFrequncies(tokens);

        Node root = HuffmanEncoder.ConstructTree(frequencies);

        HashMap<Integer, String> codes = new HashMap<>();

        HuffmanEncoder.Codes(root, codes, "");

        BitWriter bitWriter = new BitWriter();

        for (LZToken token : tokens) {
            if (token.isMatch) {
                int symbol = 256 + (token.length - 3);
                bitWriter.writeCode(codes.get(symbol));
                bitWriter.writeBit(token.distance -1);

            } else {
                int symbol = token.character & 0xFF;
                bitWriter.writeCode(codes.get(symbol));
            }
        }
        return bitWriter.toByteArray();
    }
    public static byte[] decompress(List<LZToken> LZTokens, int originalSize) {
        byte[] out = new byte[originalSize];
        int pos = 0;
        for (LZToken t : LZTokens) {
            if (t.isMatch) {
                int src = pos - t.distance;
                for (int i = 0; i < t.length; i++) {
                    out[pos++] = out[src + i];   // byte by byte, so overlap works
                }
            } else {
                out[pos++] = t.character;
            }
        }
        return out;
    }

    public static byte[] DecompressHuffman(byte[] CompressedData) throws IOException {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(CompressedData));

        int originalSize = inputStream.readInt();

        // Cases:
        if (originalSize < 0) {
            throw new IOException("Invalid original size");
        }

        if (originalSize == 0) {
            return new byte[0];
        }

        BitReader reader = new BitReader(inputStream.readAllBytes());

        Node root = HuffmanEncoder.ReadTree(reader);

        byte[] result = new byte[originalSize];
        int pos = 0;

        while (pos < originalSize) {

            int symbol = HuffmanEncoder.DecodeSymbolBits(reader, root);

            if (symbol < 256) {
                // Literal byte
                result[pos++] = (byte) symbol;

            } else {

                // LZ77 match length
                int length = (symbol - 256) + 3;

                // Distance was stored as distance - 1
                int distance = reader.readBits(12) + 1;

                if (distance > pos || length > originalSize - pos) {
                    throw new IOException("Invalid LZ77 match");
                }

                // Copy previous bytes, allowing overlap
                for (int i = 0; i < length; i++) {
                    result[pos] = result[pos - distance];
                    pos++;
                }
            }
        }
        return result;
    }

}
