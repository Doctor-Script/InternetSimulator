package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class TCPReceiver extends Thread
{
	protected Socket socket;
	
	public TCPReceiver() {
		setDaemon(true);
	}
	
	@Override
	public void run()
	{
		try {
			InputStream sin = socket.getInputStream();
			OutputStream sout = socket.getOutputStream();
			
			onAccepted(sin, sout);
			
			int size;
			do {
				byte[] dataBuffer = new byte[256];
				size = sin.read(dataBuffer);
				if (size > 0) {
					onReceived(dataBuffer, size, sout);
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
	
	protected void onReceived(byte[] dataBuffer, int size, OutputStream output) throws IOException {
	}
	
	protected void onAccepted(InputStream sin, OutputStream output) throws IOException {
	}
	
	protected void onClosed() throws IOException {
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}