package elfville.server;

import java.net.*;
import java.security.GeneralSecurityException;

import java.io.*;
import javax.crypto.*;

import elfville.protocol.*;

public class Session implements Runnable {
	private Socket clientSocket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private CurrentUserProfile currentUser;

	private int consecutive_failures = 0;

	private static int TIMEOUT_IN_MS = 15 * 60 * 1000;
	private static int CONSECUTIVE_FAILURE_LIMIT = 5;

	private SharedKeyCipher sks = null;

	public Session(Socket client) throws IOException {
		clientSocket = client;
		clientSocket.setSoTimeout(TIMEOUT_IN_MS);
		currentUser = new CurrentUserProfile(); // a new user, not logged in
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream());
			oos = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e1) {
			clientSocket.close();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (sks == null && false) { // TODO: enable
					SealedObject encrypted = (SealedObject) ois.readObject();
					sks = PKcipher.instance.decrypt(encrypted);
				}
				Request request = (Request) ois.readObject();
				Response response = Routes.processRequest(request, currentUser);

				if (response.isOK()) {
					consecutive_failures = 0;
				} else {
					consecutive_failures++;
					if (consecutive_failures >= CONSECUTIVE_FAILURE_LIMIT) {
						break;
					}
				}

				// set session authentication
				if ((request instanceof SignUpRequest)
						|| (request instanceof SignInRequest)) {
					if (response.isOK())
						System.out.printf("the current user's id is: %d\n",
								currentUser.getCurrentUserId());
				}

				oos.writeObject(response);
				oos.flush();
			} catch (EOFException e) {
				System.out.println("Client disconnected.");
				break;
			} catch (IOException e) {
				System.out.println("Client connection broke.");
				break;
			} catch (ClassNotFoundException e) {
				System.out.println("Client sent malformed request.");
				break;
			} catch (GeneralSecurityException e) {
				System.out.println("Client sent bad key.");
				e.printStackTrace();
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
