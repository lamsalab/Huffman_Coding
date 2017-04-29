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

	public static void decode(String infile, String outfile) throws IOException {
		BitInputStream in = new BitInputStream(infile);
		BitOutputStream out = new BitOutputStream(outfile);
		if(in.readBits(32) != 1846) {
			throw new IllegalArgumentException();
		}
		HuffmanTree tree = new HuffmanTree(in);
		tree.decode(in, out);
		in.close();
		out.close();
	}
	
	public static void encode(String infile, String outfile) throws IOException {
		Map<Short, Integer> frequencyMap = createFrequencyMap(infile);
		BitInputStream in = new BitInputStream(infile);
		BitOutputStream out = new BitOutputStream(outfile);
		HuffmanTree tree = new HuffmanTree(frequencyMap);
		
	}
	
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
