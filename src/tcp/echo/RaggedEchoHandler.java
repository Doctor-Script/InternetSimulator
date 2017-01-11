package tcp.echo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import tcp.IConnectionHandler;
import tcp.TCPReceivingThread;
import tcp.TCPConnetionListener;
import tcp.config.Config;
import utils.ByteArrayUtils;

public class RaggedEchoHandler extends TCPReceivingThread implements IConnectionHandler
{
	private static final int NUM_MESSAGES_PER_ROUND = 3;
	private static final int SLEEP = 100;
	
	private int respondNumber;
	
	@Override
	protected void onReceived(byte[] buffer, int size) throws IOException
	{
		System.out.println(bytesToString(buffer, size));
	
		respondNumber = 1;
		int discard = 0;
		byte[] stageBuffer = ByteArrayUtils.multiply(buffer, discard, size - discard, NUM_MESSAGES_PER_ROUND);
		
		for (int i = 1; i < size * NUM_MESSAGES_PER_ROUND + 1; i++) {
			OutputStream output = socket.getOutputStream();
			writeData(stageBuffer, 0, i, output);
			writeData(stageBuffer, i, stageBuffer.length - i, output);
		}
		System.out.println("============== END ==============");
	}
	
	private void writeData(byte[] buffer, int offset, int length, OutputStream output) throws IOException
	{
		output.write(buffer, offset, length);
		System.out.println(respondNumber++ + ByteArrayUtils.toString(buffer, offset, length));
		
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String bytesToString(byte[] buffer, int size) {
		return Arrays.toString(Arrays.copyOf(buffer, size)) + " = " + Integer.toString(size) + " bytes";
	}
	
	@Override
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public IConnectionHandler clone() {
		return new RaggedEchoHandler();
	}
	
	public static void main(String[] args)
	{
		RaggedEchoHandler handler = new RaggedEchoHandler();
		new TCPConnetionListener(Config.LISTEN_PORT, handler).run();
	}
}