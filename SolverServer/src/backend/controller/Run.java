package backend.controller;

import java.io.IOException;


public class Run {

	public static void main(String[] args) throws IOException {


		ThreadPooledServer server = new ThreadPooledServer(9000);
		new Thread(server).start();

	}

}
