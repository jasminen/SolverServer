package controller;


import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * The server class - using Fixed Thread Pool the server support up to 10 concurrent connection.
 * Default port 9000
 * 
 * @author Tzelon Machluf and Jasmine Nouriel
 *
 */
public class ThreadPooledServer implements Runnable {

	
    protected int          serverPort   = 9000;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
    
/**
 * Constructor with port parameter. 
 * @param port 
 */
    public ThreadPooledServer(int port) {
        this.serverPort = port;
    }

    public void run() {
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            this.threadPool.execute(new ClientRunnable(clientSocket, "Solver server"));
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    /**
     * Open Server Socket function
     */
    private void openServerSocket() {
        try {
        	InetAddress localaddr = InetAddress.getLocalHost();
            this.serverSocket = new ServerSocket(this.serverPort);
            this.serverSocket.setSoTimeout(0);
            System.out.println("Starting server " + localaddr.getHostAddress() + ", on port: " + this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort + " ", e);
        }
    }
}