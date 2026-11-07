package heyitsap.AlgData.compression;

import heyitsap.AlgData.compression.Objects.BitReader;
import heyitsap.AlgData.compression.Objects.BitWriter;
import heyitsap.AlgData.compression.Objects.LZToken;
import heyitsap.AlgData.compression.Objects.Node;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class HuffmanEncoder {
    public static HashMap<Integer, String> Codes(
            Node root,
            HashMap<Integer, String> ans,
            String code) {

        if (root == null) return ans;

        System.out.println(root.symbol + ": " +root.frequency );
        // Leaf node
        if (root.left == null && root.right == null) {
            if (code.equals("")) code = "0";

            ans.put(root.symbol, code);
            return ans;
        }

        Codes(root.left, ans, code + "0");
        Codes(root.right, ans, code + "1");

        return ans;
    }
    public static Node ConstructTree(int[] frequncies){
        PriorityQueue<Node> heap =
                new PriorityQueue<>(
                        Comparator.comparingInt(node -> node.frequency)
                );
        // ==== ======================
        //
        // ==== ======================
        for (int i = 0; i < frequncies.length; i++){
            if(frequncies[i] > 0){
                heap.add(new Node(i, frequncies[i]));
            }
        }

        if (heap.isEmpty()){
            return null;
        }

        System.out.println(heap.peek().symbol + ": " + heap.peek().frequency);
        // ==== ======================
        // Making the tree, the root
        // ==== ======================
        while (heap.size() != 1){
            // Get the two smallest frequency
            Node smallest = heap.poll();
            Node secondSmallets = heap.poll();

            // Create a new node with frequency is the addition of both, left children is the smallest and right is the second smallest
            Node BigBoyNode = new Node(smallest, secondSmallets);
            heap.add(BigBoyNode);
        }
        System.out.println(heap.peek().symbol + ": " + heap.peek().frequency);
        return heap.peek();
    }
    public static int[] countFrequncies(List<LZToken> tokens){
        int[] frequencies = new int[512];

        for (LZToken token : tokens) {
            int symbol;

            if (token.isMatch) {
                symbol = 256 + (token.length - 3);
            } else {
                symbol = token.character & 0xFF;
            }

            frequencies[symbol]++;
        }

        return frequencies;
    }

    public static void SerializeTree(Node node, BitWriter writer) {
        if (node.left == null && node.right == null) {
            writer.writeBit(1);
            writer.writeBits(node.symbol, 9);
            return;
        }

        writer.writeBit(0);

        SerializeTree(node.left, writer);
        SerializeTree(node.right, writer);
    }

    public static void WriteTree(Node node) throws IOException {
        BitWriter writer = new BitWriter();
        SerializeTree(node, writer);
        Files.write(Path.of("src/main/resources/compressed.txt"), writer.toByteArray());
    }

    public static Node ReadTree(BitReader reader) throws EOFException {
        int isLeaf = reader.readBit();

        if (isLeaf == 1){
            int symbol = reader.readBits(9);
            return new Node(symbol,0);
        }

        Node left = ReadTree(reader);
        Node right = ReadTree(reader);

        return new Node(left, right);
    }

    public static int DecodeSymbolBits(BitReader reader, Node current) throws IOException {
        Node node = current;

        if (current.left==null && current.right == null){
            if(reader.readBit() != 0){
                throw new IOException("Invalid Huffman code");
            }
            return current.symbol;
        }

        while (current.left != null || current.right != null) {

            int bit = reader.readBit();

            current = (bit == 0)
                    ? current.left
                    : current.right;
        }

        return current.symbol;
    }
}
