package tcp.mediator.policy;

import java.io.IOException;
import java.io.OutputStream;

public class SleepPolicy implements IWaitPolicy
{
	private long sleepMS;

	public SleepPolicy(long sleepMS) {
		this.sleepMS = sleepMS;
	}

	public void pushMessage(OutputStream output, byte[] buffer, int size) throws IOException
	{
		try {
			Thread.sleep(sleepMS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("after ping");
		
		output.write(buffer, 0, size);
	}
}
