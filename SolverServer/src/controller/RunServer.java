package controller;

import java.io.IOException;

/**
 * Main function of the server - start the server on port 9000.
 * 
 * @author Tzelon Machluf and Jasmine Nouriel
 *
 */
public class RunServer {

	public static void main(String[] args) throws IOException {


		ThreadPooledServer server = new ThreadPooledServer(9000);
		new Thread(server).start();

	}

}
