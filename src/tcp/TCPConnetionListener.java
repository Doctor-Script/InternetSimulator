package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class TCPConnetionListener
{
	private ServerSocket serverSocket;
	private int listenedPort;
	private List<IConnectionHandler> handlers = new LinkedList<>();
	
	private IConnectionHandler handlerPrototype;
	
	public TCPConnetionListener(int listenedPort, IConnectionHandler handlerPrototype) {
		this.handlerPrototype = handlerPrototype;
		this.listenedPort = listenedPort;
	}
	
	public void run()
	{
		try {
			serverSocket = new ServerSocket(listenedPort);
			System.out.println("Waiting connections...");
			
			while (true)
			{
				System.out.println("Wait new...");
				Socket socket = serverSocket.accept();
				
				IConnectionHandler handler = handlerPrototype.clone();
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
}