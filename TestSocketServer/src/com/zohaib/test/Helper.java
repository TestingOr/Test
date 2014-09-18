package com.zohaib.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class Helper {

	
	public static void writeRecord(String msg) {
		try {
 
			File file =new File("records.txt");
 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(msg);
			bw.newLine();
			bw.close();
 
			System.out.println("Record written");
 
		} catch (IOException e) {
			System.out.println("Exception, Record was not written");
			e.printStackTrace();
		}
	}
	
	public static String getRegistrationString(String [] tokens) throws Exception {
		
		String userNumber = tokens[1];
		int year = Integer.valueOf(tokens[2]); 
		int month = Integer.valueOf(tokens[3]);
		int day = Integer.valueOf(tokens[4]);
		int hour = Integer.valueOf(tokens[5]);
		int minute = Integer.valueOf(tokens[6]);
		String source = tokens[7];
		String dest = tokens[8];
		
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		
		return day+"-"+month+"-"+year+ "-" + hour + "-" + minute + "-" + source + "-"+dest;
		
		
	}
	
	
	public static String readFile() {
		 
		BufferedReader br = null;
 
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader("records.txt"));
 
			String temp = "";
			while ((sCurrentLine = br.readLine()) != null) {
				temp = temp + sCurrentLine + ";";
			}
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
 
	}
}
