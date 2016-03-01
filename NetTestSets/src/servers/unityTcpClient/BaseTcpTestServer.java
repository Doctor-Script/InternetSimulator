package servers.unityTcpClient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BaseTcpTestServer {

	public static final byte TEST = 9;
	public static final int SLEEP = 1000;
	
	protected ServerSocket serverSocket;
	protected Socket socket;
	protected DataOutputStream stream;
	
	protected int increment = 10;
	protected int value1 = 7;
	protected int value2 = 9;
	
	public void run() throws IOException, InterruptedException
	{		
		serverSocket = new ServerSocket(8080);
		print(getClass().getSimpleName());
		print("wait for connections");
		socket = serverSocket.accept();
		print("accepted");
		
		stream = new DataOutputStream(socket.getOutputStream());

		processConnection();
		
		print("close");
		socket.close();
		serverSocket.close();
	}
	
	protected void processConnection() throws IOException, InterruptedException {}
	
	int getValue1() {
		value1 += increment;
		return value1;
	}
	
	int getValue2() {
		value2 += increment;
		return value2;
	}
	
	
	protected static void runServer(BaseTcpTestServer server)
	{
		try {
			while (true) {
				server.run();
			}
		} catch(Exception e) {
			print(e.getStackTrace().toString());
		}
	}
	
	public static void print(String s) {
		System.out.println(s);
	}
}
