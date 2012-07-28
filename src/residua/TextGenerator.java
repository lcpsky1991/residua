package residua;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TextGenerator {
	
	public static ArrayList<String> readLinesFromFile(String file){
		
		ArrayList<String> lines = new ArrayList<String>();
		
		try{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			
			
			while ((strLine = br.readLine()) != null)   {
				lines.add(strLine);
			}
			//Close the input stream
			in.close();
		
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		
		return lines;
	}
	


}
