package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;

public class TCPReceivingThread extends Thread
{
	protected Socket socket;
	
	public TCPReceivingThread() {
		setDaemon(true);
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
			System.out.println(getName() + ": force closed!");
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