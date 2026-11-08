import heyitsap.AlgData.compression.LZ;
import heyitsap.AlgData.compression.Objects.LZToken;
import org.junit.jupiter.api.Test;

import java.util.List;

import static heyitsap.AlgData.compression.LZ.compress;
import static org.junit.jupiter.api.Assertions.*;

public class TestLZ {
    @Test
    public void testLZCompressionAndDecompression(){
        String[] samples = {
                "",
                "a",
                "abcabcabc",
                "aaaaaaaaaaaaaaaaaaaa",
                "\\begin{equation} x = \\frac{a}{b} \\end{equation} \\begin{equation} y = \\frac{c}{d} \\end{equation}"
        };

        for (String s : samples) {
            byte[] original = s.getBytes(java.nio.charset.StandardCharsets.UTF_8);
            List<LZToken> LZTokens = compress(original);
            byte[] restored = LZ.decompress(LZTokens, original.length);

            assertTrue(java.util.Arrays.equals(original, restored));
        }
    }

}

