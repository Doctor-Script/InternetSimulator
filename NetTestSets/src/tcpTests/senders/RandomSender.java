package tcpTests.senders;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class RandomSender extends TestMessageSender
{	
	Random rand = new Random();
	int byteNumber;
	int numMessages;
	
	public RandomSender() {
		numMessages = 20;
	}
	public RandomSender(int numMessages) {
		this.numMessages = numMessages;
	}
	
	@Override
	public void sendTestMessages() throws IOException, InterruptedException {
		
		byte[] bytes = getMessage();
		byteNumber = 0;

		while (byteNumber < (bytes.length * numMessages)) 
		{
			int len = bytes.length * 2 + 1;
			int nextMessageLength = rand.nextInt(len);
			sendNextBytes(bytes, nextMessageLength);
			Thread.sleep(SLEEP);
		}

		// Send rest of last message
		int lastBytes = bytes.length - (byteNumber % bytes.length);
		sendNextBytes(bytes, lastBytes);
		
		int totalMessages = byteNumber / bytes.length;
		print("Total messages = " + Integer.toString(totalMessages));
	}
	
	private void sendNextBytes(byte[] originalBytes, int numBytes) throws IOException {
		for (int i = 0; i < numBytes; i++) {
			byte b = originalBytes[byteNumber % originalBytes.length];
			stream.writeByte(b);
			byteNumber++;
		}
		stream.flush();
	}
	
	private byte[] getMessage() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(bos);
		
		stream.writeByte(0);
		stream.writeShort(TEST);
		stream.writeInt(getValue1());
		stream.writeInt(getValue2());
		stream.flush();
		stream.close();
		
		return bos.toByteArray();
	}
}
