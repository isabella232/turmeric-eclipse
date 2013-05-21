package org.ebayopensource.turmeric.eclipse.repositorysystem.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class LogDebug {
	static File myLog = new File("C:\\myDebug.txt");
public static void writeMessage(String m){
	Writer output;
	try {
		output = new BufferedWriter(new FileWriter("C:\\myDebug.txt", true));
		output.write(m);
		output.write(System.getProperty("line.separator") );
		output.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
