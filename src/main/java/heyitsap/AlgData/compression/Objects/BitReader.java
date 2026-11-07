package heyitsap.AlgData.compression.Objects;

import java.io.EOFException;

public class BitReader {
    private final byte[] data;
    private int bytePos = 0;
    private int bitPos = 0;

    public BitReader(byte[] data) {
        this.data = data;
    }

    public int readBit() throws EOFException {
        if (bytePos >= data.length) {
            throw new EOFException("Unexpected end of data");
        }

        int bit = ((data[bytePos] & 0xFF)
                >>> (7 - bitPos)) & 1;

        bitPos++;

        if (bitPos == 8) {
            bitPos = 0;
            bytePos++;
        }

        return bit;
    }

    public int readBits(int count) throws EOFException {
        int value = 0;

        for (int i = 0; i < count; i++) {
            value = (value << 1) | readBit();
        }

        return value;
    }
}