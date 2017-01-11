package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import tcp.config.Config;

public class TCPConnetionListener
{
	private static final Logger logger = Config.getLoggerFor(TCPConnetionListener.class);
	
	private ServerSocket serverSocket;
	private int listenedPort;
	
	private IConnectionHandler handlerPrototype;
	
	public TCPConnetionListener(int listenedPort, IConnectionHandler handlerPrototype) {
		this.handlerPrototype = handlerPrototype;
		this.listenedPort = listenedPort;
	}
	
	public void run()
	{
		try {
			serverSocket = new ServerSocket(listenedPort);
			logger.info("Start listening...");
			
			while (true)
			{
				Socket socket = serverSocket.accept();
				IConnectionHandler handler = handlerPrototype.clone();
				handler.setSocket(socket);
				handler.start();
				logger.info("Accepted...");
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