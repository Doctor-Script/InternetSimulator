package tcp.mediator.generators;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import tcp.TCPConnetionListener;
import tcp.config.Config;
import tcp.mediator.MediatorHandler;

public class FixedPingGenerator extends Thread implements IPingGenerator
{
	private static final long SLEEP = 1000;
	
	private long ping;
	private ConcurrentLinkedQueue<FixedPingGenerator.Message> queue;
	
	public FixedPingGenerator(long ping)
	{
		queue = new ConcurrentLinkedQueue<FixedPingGenerator.Message>();
		this.ping = ping;
		this.setDaemon(true);
		this.start();
	}
	
	@Override
	public void setPingFor(OutputStream targetStream, byte[] buffer, int size, Socket socket) throws IOException {
		Message message = new Message();
		message.receiveDate = new Date();
		message.output = targetStream;
		message.buffer = buffer;
		message.size = size;
		message.socket = socket;
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
	
	private class Message
	{
		public Date receiveDate;
		public OutputStream output;
		public byte[] buffer;
		public int size;
		public Socket socket;
		
		public boolean isReady(long nowTime) {
			return nowTime - receiveDate.getTime() > ping;
		}
		
		public void send() throws IOException
		{
			if (!socket.isClosed()) {
				output.write(buffer, 0, size);
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