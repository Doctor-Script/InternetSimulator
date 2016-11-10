package internetSimulator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPTestServer {

	private ServerSocket serverSocket;
	private int port = 8080;
	
	private void run() {
		try {
			serverSocket = new ServerSocket(port);
			while (true)
			{
				System.out.println("wait for connections");
				Socket socket = serverSocket.accept();
				TCPSocketHandler handler = generateNewHandler(socket);
				handler.setDaemon(true);
				handler.start();
			}
		} catch(Exception e) {
			System.out.println(e.getStackTrace().toString());
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {}
		}
	}
	
	private TCPSocketHandler generateNewHandler(Socket socket) {
		return new EchoTester(socket);
	}
	
	private static TCPTestServer instance;
	
	public static void main(String[] args) {
		instance = new TCPTestServer();
		instance.run();
	}
}
