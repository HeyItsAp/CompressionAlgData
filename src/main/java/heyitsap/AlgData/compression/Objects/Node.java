package heyitsap.AlgData.compression.Objects;

public class Node {
    public int symbol;
    public int frequency;
    public Node left;
    public Node right;

    // Non-leaf
    public Node(Node left, Node right) {
        this.frequency = left.frequency + right.frequency;
        this.left = left;
        this.right = right;
        this.symbol = -1;
    }

    public Node(int symbol, int frequency) {
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
