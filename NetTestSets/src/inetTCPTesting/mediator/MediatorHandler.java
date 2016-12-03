package inetTCPTesting.mediator;

import inetTCPTesting.IConnectionHandler;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MediatorHandler implements IConnectionHandler
{
	private static int counter = 0;
	private final int id;
	
	private SideListener clientListener;
	private SideListener serverListener;
	
	public MediatorHandler()
	{
		clientListener = new ClientListener(this);
		serverListener = new ServerListener(this);
		
		clientListener.setOther(serverListener);
		serverListener.setOther(clientListener);
		
		synchronized (this) {
			id = counter++;
		}
	}
	
	@Override
	public void start()
	{
		try {
			Socket toServerSocket = new Socket("127.0.0.1", 8080);
			serverListener.setSocket(toServerSocket);
			
			serverListener.start();
			clientListener.start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setSocket(Socket socket) {
		clientListener.setSocket(socket);
	}
	
	public int getConnectionId() {
		return id;
	}
}