package heyitsap.AlgData;

import heyitsap.AlgData.compression.HuffmanEncoder;

import java.util.HashMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        System.out.println(HuffmanEncoder.Codes(HuffmanEncoder.ConstructTree(), new HashMap<Character, String>(), ""));
    }
}
