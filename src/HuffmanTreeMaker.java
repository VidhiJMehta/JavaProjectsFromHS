import java.util.*;
import java.io.*;

public class HuffmanTreeMaker {

	int[] count;
	
	public HuffmanTreeMaker(int[] arr) {
		this.count = new int[256];
		for(int i = 0; i < arr.length; i++) {
			count[i] = arr[i];
		}
	}
	
	public HuffmanTreeMaker(String fileName) throws IOException {
		this.count = new int[256];
		this.createTableWFreq(fileName);
		HuffmanTree h = new HuffmanTree(this.count, fileName);
	}
	
	public int[] createTableWFreq(String fileName) {
		// standard ASCII characters go from index 0 to index 255
	count = new int[256];
		try {
			Scanner console = new Scanner(new File(fileName));
			while(console.hasNextLine()) {
				String line = console.nextLine();
				for(int i = 0; i < line.length(); i++) {
					count[line.charAt(i)] ++;
				}
				
				//increasing the frequency of the nextLine character after each word in the file
				count[10]++;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return this.count;
	}

	public String toString() {
		String output = ""; 
		for(int i =  0; i < this.count.length; i++) {
			if(count[i] > 0)
			output += ((char)i  + ": "+  count[i] + " " );
		}
		return output;
	}

}
