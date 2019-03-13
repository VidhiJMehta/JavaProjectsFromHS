import java.util.*;
import java.io.*;


public class HuffmanCompressor {
	
	public static void main(String args[]) throws IOException{
		HuffmanCompressor hc = new HuffmanCompressor();
		hc.compress("Hamlet.txt");
		hc.expand("Hamlet.code", "Hamlet.short");
	}

	public void compress(String fileName) throws IOException{
		HuffmanTreeMaker maker = new HuffmanTreeMaker(fileName);
	}
	
	public void expand(String codeFile, String fileName) throws IOException{
		HuffmanTree tree = new HuffmanTree(codeFile);
		tree.decode(new BitInputStream(fileName), "Hamlet.new");
	}
	
}
