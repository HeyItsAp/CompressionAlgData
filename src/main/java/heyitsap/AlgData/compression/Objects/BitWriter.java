package heyitsap.AlgData.compression.Objects;

import java.io.ByteArrayOutputStream;

public class BitWriter {

    private final ByteArrayOutputStream out =
            new ByteArrayOutputStream();

    private int currentByte = 0;
    private int bitCount = 0;


    public void writeBit(int bit) {
        currentByte = (currentByte << 1) | (bit & 1);
        bitCount++;

        if (bitCount == 8) {
            out.write(currentByte);
            currentByte = 0;
            bitCount = 0;
        }
    }

    public void writeCode(String code) {
        for (char c : code.toCharArray()) {
            writeBit(c == '1' ? 1 : 0);
        }
    }

    public byte[] toByteArray() {
        if (bitCount > 0) {
            currentByte <<= (8 - bitCount);
            out.write(currentByte);

            currentByte = 0;
            bitCount = 0;
        }

        return out.toByteArray();
    }
}