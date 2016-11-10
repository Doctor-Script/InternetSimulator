package internetSimulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class EchoTester extends TCPSocketHandler
{
	private static final int NUM_MESSAGES_IN_SERIA = 3;
	private static final int NUM_PARTS_IN_SERIA = 2;
	
	private int respondNumber = 1;
	
	public EchoTester(Socket socket) {
		super(socket);
	}
	
	@Override
	protected boolean onReceived(byte[] buffer, int size, OutputStream output) throws IOException
	{
			
		System.out.println(size);
		System.out.println(Arrays.toString(Arrays.copyOf(buffer, size)));
	
		int discard = 0;
		byte[] stageBuffer = multiplyData(buffer, discard, size - discard, NUM_MESSAGES_IN_SERIA);
//		System.out.println(Arrays.toString(stageBuffer));
		
//		output.write(stageBuffer, 0, stageBuffer.length);
//		output.flush();
//		sleep();
//		output.write(stageBuffer, 0, stageBuffer.length);
//		output.flush();
		
		System.out.println("================");
		for (int i = 1; i < size * NUM_MESSAGES_IN_SERIA + 1; i++) {
//		for (int i = 4; i < size * NUM_PARTS_IN_SERIA + 1; i++) {
			sendChunck(stageBuffer, i, output, 2);
		}
		System.out.println("============== END ==============");
		
		return true;
	}
	
	private void sendChunck(byte[] buffer, int size, OutputStream output, int numMessages) throws IOException
	{
		output.write(buffer, 0, size);
		System.out.println(respondNumber++ + Arrays.toString(Arrays.copyOf(buffer, size)));
		sleep();
		if (buffer.length - size > 0) {
//			System.out.println(buffer.length - size);
			output.write(buffer, size, buffer.length - size);
			System.out.println("+" + respondNumber++ + Arrays.toString(Arrays.copyOf(buffer, size)));
		}
		sleep();
	}

	private void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private byte[] multiplyData(byte[] arr, int off, int aLen, int mult) {
		byte[] result = new byte[aLen * mult];
		
		for (int copyId = 0; copyId < mult; copyId++) {
			System.arraycopy(arr, off, result, copyId * aLen, aLen);
		}
		return result;
	}
	
	private byte[] concatByteArrays(byte[] a, byte[] b, int aLen, int bLen) {
		byte[] c= new byte[aLen+bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}
}
