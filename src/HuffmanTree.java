import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class HuffmanTree {
	
	private Node root;
	
	private class Node implements Comparable<Node> {
		private short leafChar;
		private int frequency;
		private Node leftNode;
		private Node rightNode;
		
		public Node(Node left, Node right) {
			leftNode = left;
			rightNode = right;
			leafChar = -1;
		}
		
		public Node(Node left, Node right, int frequency) {
			leftNode = left;
			rightNode = right;
			leafChar = -1;
			this.frequency = frequency;
		}
		
		public Node(short leafChar, int frequency) {
			this.leafChar = leafChar;
			this.frequency = frequency;
		}
		
		public Node(short leafChar) {
			this.leafChar = leafChar;
		}
		
		public void preOrderPrint() {
			if(leafChar != -1) {
				System.out.println((char) leafChar);
			}
			if(leftNode != null) {
				leftNode.preOrderPrint();
			}
			if(rightNode != null) {
				rightNode.preOrderPrint();
			}
		}
		
		public boolean isLeaf() {
			return leafChar != -1;
		}
		
		public short getBits() {
			return leafChar;
		}
		
		public int getFrequency() {
			return frequency;
		}

		@Override
		public int compareTo(Node o) {
			if(this.frequency > o.frequency) {
				return 1;
			} else if(this.frequency < o.frequency) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	public HuffmanTree(Map<Short, Integer> m) {
		PriorityQueue<Node> queue = new PriorityQueue<>();
		Iterator<Entry<Short, Integer>> it = m.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Short, Integer> pair = (Map.Entry<Short, Integer>)it.next();
	        queue.add(new Node(pair.getKey(), pair.getValue()));
	        it.remove();
	    }
		while(queue.size() >= 2) {
			Node first = queue.poll();
			Node second = queue.poll();
			queue.add(new Node(first, second, first.getFrequency() + second.getFrequency()));
		}
		root = queue.poll();
	}
	
	
	public HuffmanTree(BitInputStream in) {
		root = HuffmanTreeBuilderFromFile(in);
	}
	
	private Node HuffmanTreeBuilderFromFile(BitInputStream in) {
		int temp = in.readBit();
		if(temp == 1) {
			return new Node(HuffmanTreeBuilderFromFile(in), HuffmanTreeBuilderFromFile(in));
		} else if(temp == 0) {
			return new Node((short) in.readBits(9));
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void serialize(BitOutputStream out) {
		serializeHelper(out, root);
	}
	
	public void serializeHelper(BitOutputStream out, Node cur) {
		if(cur.isLeaf()) {
			out.writeBit(0);
			out.writeBits(cur.getBits(), 9);
		} else {
			out.writeBit(1);
			serializeHelper(out, cur.leftNode);
			serializeHelper(out, cur.rightNode);
		}
	}
	
	public void encode(BitInputStream in, BitOutputStream out) {
		
	}
	
	public void decode(BitInputStream in, BitOutputStream out) {
		int temp = in.readBit();
		Node cur = root;
		while(temp != -1) {
			while(!cur.isLeaf()) {
				if(temp == 1) {
					cur = cur.rightNode;
				} else {
					cur = cur.leftNode;
				}
				temp = in.readBit();
			}
			if(cur.getBits() != 256) {
				out.writeBits(cur.getBits(), 8);
			}
			cur = root;
		}
		
	}
	
	
	public static Map<Short, String> createHuffmanCodes(HuffmanTree tree) {
		return createHCHelper(tree.root, new HashMap<Short, String>(), "");
	}
	
	
	public static Map<Short, String> createHCHelper(Node root, HashMap<Short, String> m, String code) {
		Node cur = root;
		if(!cur.isLeaf()) {
			createHCHelper(cur.leftNode, m, code + "0");
			createHCHelper(cur.rightNode, m, code + "1");
		} else {
			m.put(cur.getBits(), code);
		}
		return m;
	}
	
	
	

}
