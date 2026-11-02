package heyitsap.AlgData.compression;

import java.util.Comparator;
import java.util.PriorityQueue;

class Node {
    char character;
    int frequency;
    Node left;
    Node right;

    public Node (){
        this.character = 'a';
        this.frequency = 0;
        this.left = null;
        this.right = null;
    }

    public Node (char character, int frequency){
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public void setCharacter(char character) {
        this.character = character;
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
    public static void test(){
        PriorityQueue<Node> heap =
                new PriorityQueue<>(
                        Comparator.comparingInt(node -> node.frequency)
                );
        Node CharacterB = new Node('B', 9);
        Node CharacterD = new Node('D', 13);
        Node CharacterA = new Node('A', 5);
        Node CharacterC = new Node('C', 12);

        heap.add(CharacterA);
        heap.add(CharacterB);
        heap.add(CharacterC);
        heap.add(CharacterD);

        System.out.println(heap.peek().character + ": " + heap.peek().frequency);


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
            Node BigBoyNode = new Node();
            BigBoyNode.setFrequency(smallest.frequency + secondSmallets.frequency);
            BigBoyNode.setLeft(smallest);
            BigBoyNode.setRight(secondSmallets);
            heap.add(BigBoyNode);
        }



    }
}
