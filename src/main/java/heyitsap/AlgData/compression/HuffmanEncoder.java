package heyitsap.AlgData.compression;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

class Node {
    int symbol;
    int frequency;
    Node left;
    Node right;

    // Non-leaf
    public Node (Node left, Node right){
        this.frequency = left.frequency + right.frequency;
        this.left = left;
        this.right = right;
        this.symbol = -1;
    }

    public Node (int symbol, int frequency){
        this.symbol = symbol;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public void setSymbol(char character) {
        this.symbol = character;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }
}

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
        // Making the tree, the root
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
            Node smallest = heap.peek();
            heap.remove(smallest);
            Node secondSmallets = heap.peek();
            heap.remove(secondSmallets);

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
}
