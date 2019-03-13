import java.util.*;
import java.io.*;



class Node implements Comparable<Node>{
	char character;
	int weight;
	Node left;
	Node right;

	public Node(char i, int freq) {
		this.character = i;
		this.weight = freq;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	@Override
	public int compareTo(Node o) {
		return (this.weight - o.weight);
	}

	public String toString() {
		return this.character + "";
	}


}
public class HuffmanTree {
	private Queue<Node> charList;
	private HashMap<Character, String> codes;
	private HashMap<String, Character> chars;
	private Node overallRoot;
	private String originalFile;
	private String writeToThisFile;
	private String writeBitsToThisFile;
	private String eof;

	/*This method will construct theHuffman tree using the given array of character 
	 * frequencies,where count[i]is the number of occurrences of the character with 
	 * integer(decimal)value i.For example, count[32]represents the number of spaces. */
	public HuffmanTree(int[] count, String fileName) throws IOException{
		
		this.charList = new PriorityQueue<>();
		this.codes = new HashMap<>();
		this.chars = new HashMap<>();
		
		this.originalFile = fileName;
		//converts the fileName from .txt to .code
		String initialName = "";
		for(int i = 0; i < fileName.length(); i++) {
			if(fileName.charAt(i) == '.') {
				this.writeToThisFile = initialName + ".code";
				this.writeBitsToThisFile = initialName + ".short";
			}
			else initialName += fileName.charAt(i);
		}

		//make the leaf nodes with the characters in the priority queue
		for(int i = 0; i < count.length; i++) {	
			// if the frequency is more than zero, add it to the charList
			if(count[i] > 0) {
				Node n = new Node((char)i, count[i]);
				charList.offer(n);
			}
		}
		
		// this adds the EOF to the queue
		charList.offer(new Node((char) 256, 1));
		//System.out.println(charList);
		this.overallRoot = this.compressQueue();
		//TreePrinter.printTree(this.overallRoot);
		this.writeHashMap(this.overallRoot, "");
		//System.out.println(this.codes);
		this.write(this.writeToThisFile);
		this.writeBitsToShortFile();
	}
	
	public HuffmanTree(String codeFile)throws IOException {
		this.codes = new HashMap<>();
		this.chars = new HashMap<>();
		Scanner console = new Scanner(new File(codeFile));
		while(console.hasNext()) {
			char c = (char)console.nextInt();
			String s = (console.next());
		this.codes.put(c,s);
		this.chars.put(s,c);
		}
	}
	
	// Makes the look-up table !!
	public void writeHashMap(Node root, String code) {
		//if we have reached a valid character, then add the character and it's code to the hashmap
		if(root.character != '\0') {
			codes.put(root.character, code);
			chars.put(code, root.character);
			return;
		}
		this.writeHashMap(root.left, code + "0");
		this.writeHashMap(root.right, code + "1");

	}


	// compresses the queue to one element. It takes the lowest two elements and combines them
	// until only one elements is left in the charList and it returns this last element.
	public Node compressQueue() {
		while(this.charList.size() > 1) {
			Node left = charList.poll();
			Node right = charList.poll();

			Node newNode = new Node('\0', left.weight+right.weight);
			newNode.setLeft(left);
			newNode.setRight(right);

			charList.add(newNode);

		}
		return this.charList.peek();
	}

	/* This method should write your encoding tree to the given
	 * file in a standard format, using the naming conventions provided later. */
	public void write(String fileName) {
		try {
			PrintWriter p = new PrintWriter(fileName);
			Set<Character> s = codes.keySet();

			for(char c : s) {
				if((int)c == 256) this.eof = codes.get(c);
				p.println((int) c);
				p.println(codes.get(c));
			}

			p.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void writeBitsToShortFile() throws IOException{
		
		BitOutputStream output = new BitOutputStream(writeBitsToThisFile);
		Scanner console = new Scanner(new File(originalFile));
		
		while(console.hasNextLine()) {
			for(char c : console.nextLine().toCharArray()) {
			//System.out.println("letter: " + c + " code: " + codes.get(c));
				for(char bit: codes.get(c).toCharArray()) {
					//System.out.print(bit + " ");
					if(bit == '1') output.writeBit(1);
					else if(bit == '0') output.writeBit(0);
			}
			}
			for(char bit: codes.get((char)10).toCharArray()) {
				//System.out.print(bit + " ");
				if(bit == '1') output.writeBit(1);
				else if(bit == '0') output.writeBit(0);
			}
		}
		for(char bit: codes.get((char)256).toCharArray()) {
			//System.out.print(bit + " ");
			if(bit == '1') output.writeBit(1);
			else if(bit == '0') output.writeBit(0);
		}
		//System.out.println();
		output.close();
	}

	public void decode(BitInputStream in, String outFile) throws IOException{
		PrintWriter out = new PrintWriter(outFile);
		String bits = new String();
		
		while(!bits.equals(codes.get((char)256))) {
			bits += in.readBit() + "";
			if(bits.equals(codes.get((char)256))) break;
			if(bits.contains("-1")) break;
			
			if(chars.containsKey(bits)) {
				//System.out.println(chars.get(bits));
				out.print(chars.get(bits));
				bits = "";
			}
		}
		out.close();
		in.close();
	}
	
}
