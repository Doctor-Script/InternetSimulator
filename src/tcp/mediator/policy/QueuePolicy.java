package tcp.mediator.policy;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueuePolicy extends Thread implements IWaitPolicy
{
	private static final long DELAY = 5000;
	private static final long SLEEP = 1000;
	private ConcurrentLinkedQueue<QueuePolicy.Message> queue = new ConcurrentLinkedQueue<QueuePolicy.Message>();
	
	public QueuePolicy() {
		this.setDaemon(true);
		this.start();
	}
	
	@Override
	public void pushMessage(OutputStream output, byte[] buffer, int size) throws IOException {
		Message m = new Message();
		m.start = new Date();
		m.output = output;
		m.buffer = buffer;
		m.size = size;
		queue.add(m);
		System.out.println("added");
	}

	@Override
	public void run()
	{
		while(true) {
			Date now = new Date();
			Message m = queue.peek();
			while (m != null && m.isReady(now.getTime()))
			{
				try {
					m.send();
				} catch (IOException e) {
					e.printStackTrace();
				}
				queue.poll();
				m = queue.peek();
			}
			
			sleep();
		}
	}
	
	private void sleep() {
		try {
			Thread.sleep(SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static class Message
	{
		public Date start;
		public OutputStream output;
		public byte[] buffer;
		public int size;
		
		public boolean isReady(long nowTime) {
			return nowTime - start.getTime() > DELAY;
		}
		
		public void send() throws IOException {
			output.write(buffer, 0, size);
		}
	}
}