# GitHub Link
[GitHub Link](https://github.com/HeyItsAp/CompressionAlgData)

# Little Disclaimer
This ReadMe is for those who needs more information about the source code/are interested in the implemention of the compression algorithm. Though i can say that this is more of a learning project and is for my own self-interest. 
> **Note:** AI has been invovled writting this ReadME.md.

# How to run it
> It is recommended to create a dedicated folder for which way to use

## **Option A: Git clone and Maven**
  
Requires:
- Atleast Java 25

Navigate to the safe directory and run the following code to get the code, and to navigate into the project:

```
git clone https://github.com/HeyItsAp/CompressionAlgData
cd assignment4ALGDATA
```

Run the following command to run at test the current codebase:

```
java -jar out/artifacts/
```
## **Option B (for LA probably): Just run the .jar**

**Recommended to make a file nanmed 'output'**. Run the jar associated with the thing in your own folder. 
# LZ with referances (LZ77) + Huffman Compression (Java)

This loss-less compression algorithm combines LZ with referances (**LZ77**) and **Huffman coding** to compress and decompress files.

## LZ with references (LZZ)
From the lecture, it was possible to store LZ tokens as references to previous occurrences. After some research this method already has a name; **LZ77**. 

It works by having two sliding windows; a back-window and input-window. Consistently, the input-window checks if a symbol or a combination of symbols has already occurred in the back-window. If there is, it replaces the symbols with a reference and if not, just saves it and moves on. The symbols are saved as tokens; the most common format is `(distance, offset, character)`. Non-matches just store `character`, with the others being 0. Matches contain how long it is between the match and the location of the match and how many other characters are involved.

This article explains it somewhat better:
The compressor uses two techniques: [LS77 Sliding Algorithm Explained](https://imaarov.ir/blog/lz77-sliding-window-compression-explained)

## Huffman Coding/Tree
Its just a way to certain symbols based on frequency using a binary tree setup. Recommended to read this setup: [Huffman Coding](https://www.geeksforgeeks.org/dsa/huffman-coding-greedy-algo-3/)
## How it works


- **LZ77** replaces repeated sequences with a reference to earlier data: `(distance, length)`.
- **Huffman coding** replaces commonly occurring symbols with shorter bit codes.

```text
Original file
    |
    v
LZ77 match finding
    |
    v
Literals + (distance, length) tokens
    |
    v
Convert tokens to symbols; count frequencies
    |
    v
Build Huffman tree and codes
    |
    v
BitWriter packs symbols and distances into bytes
    |
    v
Compressed file (.bin)
```

## Compression steps

1. **Read the input.** Load the file as a `byte[]` so the compressor handles raw bytes, not just text.
2. **Find LZ77 matches (`LZ.compress`).** At each position, search up to **4,096 bytes** behind the current position for the longest match (up to **258 bytes**). If the match is at least **3 bytes**, emit a match token containing its distance and length; otherwise, emit a literal byte.
3. **Translate tokens to Huffman symbols.** Use symbols `0–255` for literal byte values. For a match of length `L`, use symbol `256 + (L - 3)` (symbols `256–511`). A match's distance is stored separately.
4. **Count symbol frequencies.** Count how often each literal/length symbol occurs in the LZ77 tokens.
5. **Construct the Huffman tree (`HuffmanEncoder`).** Insert the symbols with nonzero frequencies into a priority queue, repeatedly combine the two least-frequent nodes, then walk the tree to assign bit codes (`0` for left, `1` for right).
6. **Write the metadata.** Store the original uncompressed byte length and enough information to reconstruct the Huffman tree.
7. **Encode the tokens.** Write the Huffman code for every literal or match-length symbol. After a match-length symbol, write its distance as a **12-bit value** using `distance - 1` (which represents distances 1–4,096).
8. **Pack bits and save.** `BitWriter` combines individual bits into bytes. Pad the final partial byte with zeros and write the result to a binary file, such as `output/compressed.bin`.

## Decompression steps

1. **Read the compressed file** as a `byte[]`.
2. **Read the original size** from the file header. This tells the decoder when it has reconstructed all the original bytes.
3. **Reconstruct the Huffman tree** from the serialized tree data.
4. **Read bits (`BitReader`).** Read one bit at a time and follow the Huffman tree (`0` = left, `1` = right) until reaching a leaf symbol.
5. **Handle the decoded symbol:**
    - For a symbol in `0–255`, append that byte to the output.
    - For a symbol in `256–511`, calculate `length = (symbol - 256) + 3`, then read the next 12 bits to obtain `distance = encodedDistance + 1`. Copy `length` bytes from `distance` bytes behind the current output position. Copy one byte at a time so overlapping matches work.
6. **Stop after `originalSize` bytes.** Ignore any padding bits at the end and save the restored bytes (for example, to `output/decompressed.txt`).

```text
Compressed file (.bin)
    |
    v
Read original size + Huffman tree
    |
    v
BitReader + Huffman tree decode symbols
    |
    v
Literal bytes or (distance, length) matches
    |
    v
LZ77 back-reference copying
    |
    v
Original bytes restored
```

## Example compressed-file format

The following layout is a **custom format for this project**, not the standard DEFLATE/ZIP format:

| Field | Encoding | Purpose |
| --- | --- | --- |
| Original size | First 4 bytes, signed big-endian Java `int` | Number of bytes to restore |
| Huffman tree | Preorder bit serialization | Rebuild the same tree during decompression |
| Compressed tokens | Variable-length Huffman codes plus fixed 12-bit match distances | Compressed data |
| Padding | 0–7 trailing bits | Completes the final byte |

**Tree serialization:** write `1` followed by a **9-bit symbol** for a leaf; write `0` followed by the left and right subtrees for an internal node. Empty input can be represented by a zero original size and no tree or token data. The decoder reads the tree from the bitstream before decoding the tokens.

If the Huffman tree is saved in a **separate file** instead, decompression must receive *both* the compressed data and the tree metadata. Both file sizes count when measuring total compression.

## Main classes

| Class | Responsibility                                                                                                                                                       |
| --- |----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `LZ` | The Compiler; combines LZ77 and Huffman and reverse. Has LZ77 spesifics; `compress(byte[])` produces LZ77 tokens; `decompress(...)` restores bytes from tokens.      |
| `LZToken` | Represents either a literal byte or a `(distance, length)` match                                                                                                     |
| `HuffmanEncoder` | Counts symbol frequencies, constructs the tree, and assigns codes                                                                                                    |
| `Node` | Holds Huffman tree nodes (symbol, frequency, left/right children)                                                                                                    |
| `BitWriter` | Packs individual bits into whole bytes for saving                                                                                                                    |
| `BitReader` | Extracts individual bits from stored bytes                                                                                                                           |


## Output files

A practical layout is:

```text
src/main/resources/original.txt    # Example input bundled with the project
output/compressed.bin              # Compressed binary data
output/decompressed.txt            # Reconstructed output, readable for text inputs
```

The compressed `.bin` file contains raw bytes, so it may not display as readable text. A text editor is useful for checking the **decompressed** `.txt` file, but `Arrays.equals(...)` is a more reliable correctness test.
