package tcp.mediator.generators;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import tcp.TCPConnetionListener;
import tcp.config.Config;
import tcp.mediator.MediatorHandler;

public class SleepPingGenerator implements IPingGenerator
{
	private long sleepDelayMS;

	public SleepPingGenerator(long sleepDelayMS) {
		this.sleepDelayMS = sleepDelayMS;
	}

	public void setPingFor(OutputStream output, byte[] buffer, int size, Socket socket) throws IOException
	{
		try {
			Thread.sleep(sleepDelayMS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		output.write(buffer, 0, size);
	}
	
	public static void main(String[] args)
	{
		MediatorHandler handler = new MediatorHandler(new SleepPingGenerator(Config.PING), 
				Config.SERVER_ADDRESS, Config.SERVER_PORT);
		
		new TCPConnetionListener(Config.LISTEN_PORT, handler).run();
	}
}