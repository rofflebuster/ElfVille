package elfville.server;


import java.net.*;

import java.io.*;

import elfville.protocol.Message;

public class ServerThread extends Thread {
	private Socket clientSocket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public ServerThread(Socket client) {
		clientSocket = client;
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream());
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch(Exception e1) {
			try {
				clientSocket.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
			return;
		}
		this.start();
	}

	public void run() {
		while (true) {
			try {
				Message inMessage = (Message) ois.readObject();
				Message outMessage = Routes.processRequest(inMessage);
				oos.writeObject(outMessage);
				oos.flush();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		
		try {
			// close connections
			ois.close();
			oos.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
