package inetTCPTesting;

import inetTCPTesting.factory.HandlerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class TCPServer
{
	private ServerSocket serverSocket;
	private int port = 8081;
	private List<IConnectionHandler> handlers = new LinkedList<>();
	
	private void run()
	{
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Waiting connections...");
			
			while (true)
			{
				System.out.println("Wait new...");
				Socket socket = serverSocket.accept();
				IConnectionHandler handler = HandlerFactory.generateNewHandler();
				handler.setSocket(socket);
				handlers.add(handler);//is no garbage
				handler.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {}
		}
	}
	
	private static TCPServer instance;
	
	public static void main(String[] args) {
		instance = new TCPServer();
		instance.run();
	}
}