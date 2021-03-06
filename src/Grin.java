import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Grin {

	public static void main(String[] args) throws IOException {
		if(args[0].compareTo("decode") == 0) {
			decode(args[1], args[2]);
		} else if(args[0].compareTo("encode") == 0){
			encode(args[1], args[2]);
		} else {
			throw new IllegalArgumentException();
		}

	}

	/**
	 * Decodes a file via Huffman Coding
	 * @param infile the file being decoded
	 * @param outfile the new, uncompressed file
	 * @throws IOException
	 */
	public static void decode(String infile, String outfile) throws IOException {
		BitInputStream in = new BitInputStream(infile);
		BitOutputStream out = new BitOutputStream(outfile);
		if (in.readBits(32) != 1846) {
			throw new IllegalArgumentException();
		}
		HuffmanTree tree = new HuffmanTree(in);
		tree.decode(in, out);
		in.close();
		out.close();
	}

	/**
	 * Compresses a file via Huffman Coding
	 * @param infile the file being encoded
	 * @param outfile the new, compressed file
	 * @throws IOException
	 */
	public static void encode(String infile, String outfile) throws IOException {
		Map<Short, Integer> frequencyMap = createFrequencyMap(infile);
		BitInputStream in = new BitInputStream(infile);
		BitOutputStream out = new BitOutputStream(outfile);
		HuffmanTree tree = new HuffmanTree(frequencyMap);
		Map<Short, String> HuffmanCodes = HuffmanTree.createHuffmanCodes(tree);
		out.writeBits(1846, 32);
		tree.serialize(out);
		tree.encode(in, out, HuffmanCodes);
		out.close();
		in.close();
	}

	
	/**
	 * Creates a map of characters to their frequency in a file
	 * @param file the file being parsed
	 * @return Map of characters to their frequency in a file
	 * @throws IOException
	 */
	public static Map<Short, Integer> createFrequencyMap(String file) throws IOException {
		BitInputStream in = new BitInputStream(file);
		short temp = (short) in.readBits(8);
		Map<Short, Integer> frequencyMap = new HashMap<>();
		while(temp != -1) {
			frequencyMap.putIfAbsent(temp, 0);
			frequencyMap.put(temp, frequencyMap.get(temp) + 1);
			temp = (short) in.readBits(8);
		}
		
		frequencyMap.put((short) 256, 1);
		return frequencyMap;
	}
}
