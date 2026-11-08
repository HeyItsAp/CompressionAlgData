package heyitsap.AlgData;

import heyitsap.AlgData.compression.HuffmanEncoder;
import heyitsap.AlgData.compression.LZ;
import heyitsap.AlgData.compression.Objects.BitWriter;
import heyitsap.AlgData.compression.Objects.LZToken;
import heyitsap.AlgData.compression.Objects.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import static heyitsap.AlgData.compression.LZ.compressWithHuffman;


public class Main {
    public static void main() {
        System.out.println("Yo");

        byte[] test;
        byte[] compressedBytes;

        try {
            test = Files.readAllBytes(Path.of("src/main/resources/Twenty_thousand_leagues_under_the_sea.txt"));
            compressedBytes = compressWithHuffman(test);
            Files.write(Path.of("src/main/resources/compressed.txt"), compressedBytes);
            System.out.println("Original: " + test.length + " bytes");
            System.out.println("Compressed: " + compressedBytes.length + " bytes");
        } catch (IOException e){
            e.printStackTrace();
        }

        byte[] restored;

        try {
            test = Files.readAllBytes(Path.of("src/main/resources/compressed.txt"));
            restored = LZ.DecompressHuffman(test);
            Files.write(Path.of("src/main/resources/decompressed.txt"), restored);
            System.out.println("Restored: " + restored.length + " bytes");
        } catch (IOException e){
            e.printStackTrace();
        }




    }

}

