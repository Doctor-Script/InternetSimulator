package tcp.mediator.generators;

import java.io.IOException;

import tcp.TCPConnetionListener;
import tcp.config.Config;
import tcp.mediator.MediatorHandler;
import tcp.mediator.SideListener;

@Deprecated
public class SleepPingGenerator implements IPingGenerator
{
	private long sleepDelayMS;

	public SleepPingGenerator(long sleepDelayMS) {
		this.sleepDelayMS = sleepDelayMS;
	}

	@Override
	public void setPingFor(SideListener target, byte[] buffer, int size) throws IOException
	{
		try {
			Thread.sleep(sleepDelayMS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		target.getSocket().getOutputStream().write(buffer, 0, size);
	}
	
	@Override
	public void onConnectionEnd(SideListener source) {
	}
	
	public static void main(String[] args)
	{
		MediatorHandler handler = new MediatorHandler(new SleepPingGenerator(Config.PING), 
				Config.SERVER_ADDRESS, Config.SERVER_PORT);
		
		new TCPConnetionListener(Config.LISTEN_PORT, handler).run();
	}
}
