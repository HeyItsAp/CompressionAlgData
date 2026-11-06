package heyitsap.AlgData;

import heyitsap.AlgData.compression.HuffmanEncoder;
import heyitsap.AlgData.compression.LZ;
import heyitsap.AlgData.compression.Objects.BitWriter;
import heyitsap.AlgData.compression.Objects.LZToken;
import heyitsap.AlgData.compression.Objects.Node;

import java.util.HashMap;
import java.util.List;

import static heyitsap.AlgData.compression.LZ.compressWithHuffman;


public class Main {
    public static void main() {
        System.out.println("Yo");

        String sample = "\\begin{equation} x = \\frac{a}{b} \\end{equation} \\begin{equation} y = \\frac{c}{d} \\end{equation}";

        compressWithHuffman(sample.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

}

