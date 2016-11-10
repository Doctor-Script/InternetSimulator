package internetSimulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPSocketHandler extends Thread {

	private Socket socket;
	
	protected byte[] dataBuffer;
	
	public TCPSocketHandler(Socket socket) {
		this.socket = socket;
		dataBuffer = new byte[256];
	}
	
	@Override
	public void run() {
		try {
			InputStream sin = socket.getInputStream();
			OutputStream sout = socket.getOutputStream();
			
			int size;
			do {
				size = sin.read(dataBuffer);
				if (size > 0) {
					onReceived(dataBuffer, size, sout);
				}
			} while (size > 0);
			
//			while (size > 0) {
//				size = sin.read(dataBuffer);
//				onReceived(dataBuffer, size, sout);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {}
		}
	}
	
	protected boolean onReceived(byte[] dataBuffer, int size, OutputStream output) throws IOException {
		return true;
	}
}
