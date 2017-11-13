package ca.ucalgary.seng300.VendingMachineLogic;

import java.io.BufferedWriter;
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
	private static Date date;
	
	void log(String event) {
		date = new Date();
		try(FileWriter fw = new FileWriter(fileName, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw)){
			out.println(String.format("%s - %s\n", dateFormat.format(date), event));
		} catch (IOException e) {
			System.out.println("Error writing to file!");
		}
	}
	
}
