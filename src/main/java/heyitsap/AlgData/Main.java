package heyitsap.AlgData;

import heyitsap.AlgData.compression.LZ;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static heyitsap.AlgData.compression.LZ.compressWithHuffman;


public class Main {
    public static void main() {
        Path outputFolder = Path.of("output");
        String pathString = "";
        System.out.println("Yo");
        Scanner scanner = new Scanner(System.in);
        int valg;
        byte[] original;
        byte[] compressedBytes;
        byte[] restored;
        do {
            System.out.println("\n------- Compression Menu -------");
            System.out.println("\n You can view the compressed file and decompressed file in the 'output' folder after first run time");
            System.out.println("1. Forelesning.txt");
            System.out.println("2. Forelesning.lyx");
            System.out.println("3. opg6-kompr-lyx.txt");
            System.out.println("4. Twenty_thousand_leagues_under_the_sea.txt");
            System.out.println("5. Wikipeida (The 100 MB file)");
            System.out.print("Write you choice (1-5):");
            valg = scanner.nextInt();
            scanner.nextLine();


            switch (valg) {
                case 1:
                    pathString = "src/main/resources/diverse.txt";
                    break;
                case 2:
                    pathString = "src/main/resources/diverse-lyx.txt";
                    break;
                case 3:
                    pathString = "src/main/resources/opg6-kompr-lyx.txt";
                    break;
                case 4:
                    pathString = "src/main/resources/Twenty_thousand_leagues_under_the_sea.txt";
                    break;
                case 5:
                    pathString = "src/main/resources/Wikipedia.txt";
                    break;
                default:
                    System.out.println("Invalid");
                    break;
            }
            System.out.println("==== Compressing ====");
            try {
                Files.createDirectories(outputFolder);
                System.out.println("Reading bytes...");
                original = Files.readAllBytes(Path.of(pathString));
                compressedBytes = compressWithHuffman(original);
                System.out.println("Writing data to output/compressed.txt...");
                Files.write(Path.of("output/compressed.txt"), compressedBytes);
                System.out.println("Original: " + original.length + " bytes");
                System.out.println("Compressed: " + compressedBytes.length + " bytes");
            } catch (IOException e){
                e.printStackTrace();
            }

            System.out.println("==== Decompressing ====");
            try {
                System.out.println("Reading bytes from compressed file...");
                original = Files.readAllBytes(Path.of("output/compressed.txt"));

                restored = LZ.DecompressHuffman(original);

                System.out.println("Writing to output/decompressed.txt ...");
                Files.write(Path.of("output/decompressed.txt"), restored);
                System.out.println("Restored: " + restored.length + " bytes");
            } catch (IOException e){
                e.printStackTrace();
            }
        } while (valg != 5);
    }
}

