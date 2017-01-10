package tcp.mediator;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import tcp.IConnectionHandler;
import tcp.mediator.generators.IPingGenerator;

public class MediatorHandler implements IConnectionHandler
{
	private static int counter = 0;
	private final int id;
	
	private SideListener clientListener;
	private SideListener serverListener;
	public final IPingGenerator pingGenerator;
	
	private final String serverAddress;
	private final int serverPort;
	
	public MediatorHandler(IPingGenerator pingGenerator, String serverAddress, int serverPort)
	{
		synchronized (this) {
			id = counter++;
		}
		
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.pingGenerator = pingGenerator;
		
		clientListener = new SideListener(this, "Client " + Integer.toString(id));
		serverListener = new SideListener(this, "Server " + Integer.toString(id));
		
		clientListener.setOther(serverListener);
		serverListener.setOther(clientListener);
	}
	
	@Override
	public void start() {
		serverListener.start();
		clientListener.start();
	}

	@Override
	public void setSocket(Socket socket)
	{
		clientListener.setSocket(socket);
		
		Socket toServerSocket;
		try {
			toServerSocket = new Socket(serverAddress, serverPort);
			serverListener.setSocket(toServerSocket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public IConnectionHandler clone() {
		return new MediatorHandler(pingGenerator, serverAddress, serverPort);
	}
}