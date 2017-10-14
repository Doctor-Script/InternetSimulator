package tcp.mediator.generators;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import tcp.TCPConnetionListener;
import tcp.config.Config;
import tcp.mediator.MediatorHandler;
import tcp.mediator.SideListener;

public class FixedPingGenerator extends Thread implements IPingGenerator
{
	private static final long SLEEP = 1000;
	
	protected long baseTimeWait;
	private ConcurrentLinkedQueue<FixedPingGenerator.Message> queue;
	
	public FixedPingGenerator(long baseTimeWait)
	{
		queue = new ConcurrentLinkedQueue<FixedPingGenerator.Message>();
		this.baseTimeWait = baseTimeWait;
		setName("NetworkEventQueue");
		this.setDaemon(true);
		this.start();
	}
	
	@Override
	public void setPingFor(SideListener target, byte[] buffer, int size) throws IOException {
		pushMessage(target, buffer, size, baseTimeWait);
	}
	
	protected void pushMessage(SideListener target, byte[] buffer, int size, long timeWait) {
		Message message = new Message();
		message.receiveDate = new Date();
		message.buffer = buffer;
		message.size = size;
		message.target = target;
		message.timeWait = timeWait;
		queue.add(message);
	}
	
	@Override
	public void run()
	{
		try {
			while(true)
			{
				Date now = new Date();
				Message currentMessage = queue.peek();
				while (currentMessage != null && currentMessage.isReady(now.getTime()))
				{
					try {
						currentMessage.send();
					} catch (IOException e) {
						e.printStackTrace();
					}
					queue.poll();
					currentMessage = queue.peek();
				}
				
				Thread.sleep(SLEEP);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onConnectionEnd(SideListener handler) {
	}
	
	private static class Message
	{
		public Date receiveDate;
		public byte[] buffer;
		public int size;
		public SideListener target;
		public long timeWait;
		
		public boolean isReady(long nowTime) {
			return nowTime - receiveDate.getTime() > timeWait;
		}
		
		public void send() throws IOException
		{
			if (!target.getSocket().isClosed()) {
				target.getSocket().getOutputStream().write(buffer, 0, size);
			}
		}
	}
	
	public static void main(String[] args)
	{
		MediatorHandler handler = new MediatorHandler(new FixedPingGenerator(Config.PING), 
				Config.SERVER_ADDRESS, Config.SERVER_PORT);
		
		new TCPConnetionListener(Config.LISTEN_PORT, handler).run();
	}
}