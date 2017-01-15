package tcp.mediator.generators;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tcp.TCPConnetionListener;
import tcp.config.Config;
import tcp.mediator.MediatorHandler;
import tcp.mediator.SideListener;

public class TicTacPingGenerator extends FixedPingGenerator
{
	private Map<MediatorHandler, Integer> messagesCount = new HashMap<>();
	
	public TicTacPingGenerator(long ping) {
		super(ping);
	}

	@Override
	public void setPingFor(SideListener target, byte[] buffer, int size) throws IOException
	{
		int numMessages = updateNumMessgages(target.parent);
		long timeWait = (numMessages % 2 == 0) ? baseTimeWait * 2 : baseTimeWait / 2;
		pushMessage(target, buffer, size, timeWait);
	}
	
	private int updateNumMessgages(MediatorHandler target)
	{
		Integer numMessages;
		synchronized (messagesCount) {
			numMessages = messagesCount.get(target);
			if (numMessages == null) {
				numMessages = 0;
			}
			messagesCount.put(target, numMessages + 1);
		}
		return numMessages;
	}
	
	@Override
	public void onConnectionEnd(SideListener source)
	{
		synchronized (messagesCount) {
			messagesCount.remove(source.getOther().getSocket());
			messagesCount.remove(source.getSocket());
		}
	}
	
	public static void main(String[] args)
	{
		MediatorHandler handler = new MediatorHandler(new TicTacPingGenerator(Config.PING), 
				Config.SERVER_ADDRESS, Config.SERVER_PORT);
		
		new TCPConnetionListener(Config.LISTEN_PORT, handler).run();
	}
}