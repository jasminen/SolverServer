package backend.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import common.Message;
import backend.model.minimaxAB.MinimaxAB;

public class ClientRunnable implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;
	protected ObjectOutputStream output;
	protected ObjectInputStream input;
		

	public ClientRunnable(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
	}

	public void run() {
		try {
			System.out.println("Client "+clientSocket.getRemoteSocketAddress()+" is connected");
			output = new ObjectOutputStream(this.clientSocket.getOutputStream());
			input = new ObjectInputStream(this.clientSocket.getInputStream());
			output.writeObject(new Message(null, "You are connected to " + this.serverText, 0, null, 0));
			output.flush();
			while (true) {
				Message messageIn = (Message) input.readObject();
				if (messageIn.getMsg().equals("exit")) {
					System.out.println("Client "+clientSocket.getRemoteSocketAddress()+" closed the connection");
					break;
				} else if (messageIn.getMsg().equals("getHint") && messageIn.getGame().equals("2048")) {
					int direction = MinimaxAB.findBestMove(messageIn.getState(), messageIn.getDepth());
					output.writeObject(new Message(null, "This is the best next move", direction, messageIn.getGame(), 0));
				}
			}
			output.close();
			input.close();
		} catch (SocketException e) {
			System.out.println("Client "+clientSocket.getRemoteSocketAddress()+" closed the connection");

		}
		catch (IOException e) {
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
}