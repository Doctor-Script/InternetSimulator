package tcpTests.senders;

import java.io.DataOutputStream;
import java.io.IOException;

public abstract class TestMessageSender {

	public static final short TEST = 9;
	public static final int SLEEP = 1000;
	
	protected DataOutputStream stream;
	protected int increment = 10;
	protected int value1 = 7;
	protected int value2 = 9;
	
	public abstract void sendTestMessages() throws IOException, InterruptedException;
	
	protected int getValue1() {
		value1 += increment;
		return value1;
	}
	
	protected int getValue2() {
		value2 += increment;
		return value2;
	}
	
	public void setStream(DataOutputStream stream) {
		this.stream = stream;
	}
	
	public static void print(String s) {
		System.out.println(s);
	}
}
