package helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import world.World;

public class Recorder {
	private static final String FILENAME = "C:\\Users\\matth\\eclipse-workspace\\GravitySimulation1.4\\src\\config\\record.txt";
	
	
	public static void recordTurn(World world) {	
		FileWriter fw = null;
		try {
			fw = new FileWriter(FILENAME, true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);

		try {

			String content = world.toString();

			bw.append(content);

		} catch (IOException e) { System.out.println("Something went wrong"); } 
		finally {
			try {

				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();

			} catch (IOException ex) {	ex.printStackTrace();	}
		}
	}
}
