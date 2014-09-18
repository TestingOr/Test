package com.zohaib.test;

import java.io.*;
import java.net.*;

public class Server {
	ServerSocket socket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;

	DatabaseFunctions db;

	Server() throws Exception {
		try {
			db = new DatabaseFunctions();
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	void run() {
		
		try{
			//1. creating a server socket
			socket = new ServerSocket(2004, 10);
			//2. Wait for connection
			System.out.println("Waiting for connection "+socket.getInetAddress() + " " + socket.getLocalPort() + " "+ socket.getLocalSocketAddress());
			connection = socket.accept();
			System.out.println("Connection received from " + connection.getInetAddress().getHostName());
			//3. get Input and Output streams
			in = new ObjectInputStream(connection.getInputStream());
			//4. The two parts communicate via the input and output streams

			try{
				message = (String)in.readObject();
				String msg = this.processMessage(message);

				System.out.println("message received from client>" + message);
				out = new ObjectOutputStream(connection.getOutputStream());
				out.flush();
				sendMessage(msg);
			}
			catch(ClassNotFoundException classnot){
				System.err.println("Data received in unknown format");
			}

		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				socket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("message being sent to client" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[]) throws Exception
	{
		Server server = new Server();
		while(true){
			server.run();
		}
	}

	public String processMessage(String message)  {

		try {
			String [] tokens = message.split("-");
			if("0".equals(tokens[0])) {

				System.out.println("registration message");
				//String s = Helper.getRegistrationString(tokens);
				long rideID = db.insertOffer(tokens);
				System.out.println("ride saved with id: "+rideID);
				//Helper.writeRecord(s);
				return "Ride Registered";

			} else if("1".equals(tokens[0])) {
				System.out.println("view reports message");
				//String temp = Helper.readFile();
				String temp = db.getOfferResult();
				System.out.println("reports message \n"+temp);
				return temp;
			} else if("2".equals(tokens[0])) {
				System.out.println("update reports message");
				//String temp = Helper.readFile();
				if(db.invalidateOffer(Integer.valueOf(tokens[1]))) {
					return "updated";
				}
				return "error";
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}