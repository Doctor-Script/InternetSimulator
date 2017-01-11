package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

public class TCPReceivingThread extends Thread
{
	protected Socket socket;
	protected final Logger logger;
	
	public TCPReceivingThread(Logger logger) {
		setDaemon(true);
		this.logger = logger;
	}
	
	@Override
	public void run()
	{
		try {
			InputStream socketInputStream = socket.getInputStream();
			
			onAccepted();
			
			int size;
			do {
				byte[] dataBuffer = new byte[256];
				size = socketInputStream.read(dataBuffer);
				if (size > 0) {
					onReceived(dataBuffer, size);
				}
			} while (size > 0);
			
		}
		catch (SocketException e) {
			logger.info(getName() + ": force closed!");
		}
		catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				onClosed();
			} catch (IOException e) {}
		}
	}
	
	protected void onReceived(byte[] dataBuffer, int size) throws IOException {
	}
	
	protected void onAccepted() throws IOException {
	}
	
	protected void onClosed() throws IOException {
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}