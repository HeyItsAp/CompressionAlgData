package heyitsap.AlgData;

import heyitsap.AlgData.compression.HuffmanEncoder;
import heyitsap.AlgData.compression.LZ;

import java.util.HashMap;


public class Main {
    static void main() {
        String sample = "\\begin{equation} x = \\frac{a}{b} \\end{equation} \\begin{equation} y = \\frac{c}{d} \\end{equation}";

        System.out.println(HuffmanEncoder.Codes(HuffmanEncoder.ConstructTree(HuffmanEncoder.countFrequncies(LZ.compress(sample.getBytes(java.nio.charset.StandardCharsets.UTF_8)))), new HashMap<Integer, String>(), ""));
    }
}
