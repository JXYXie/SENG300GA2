package ca.ucalgary.seng300.VendingMachineLogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/***********************************************
 * Logs each action of the user and machine events that are visible to the user to a text file
 * Utilizes Java's internal clock for timestamps
 ***********************************************/
public class EventLogger {
	
	private String fileName = "eventlog.txt";
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS"); //Date Format is in Hour:minute:second.miliseconds
	private static Date date = new Date();
	private PrintWriter pw;
	private FileWriter fw;
	
	void log(String event) {
		try {
			fw = new FileWriter(fileName);
			pw = new PrintWriter(fw);
			pw.append(String.format("%s - %s\n", dateFormat.format(date), event));
			pw.close();
		} catch (IOException e) {
			System.out.println("Error writing to file!");
		}
	}
	
}
