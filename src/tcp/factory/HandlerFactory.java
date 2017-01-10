package tcp.factory;

import tcp.IConnectionHandler;
import tcp.echo.RaggedEchoHandler;
import tcp.mediator.MediatorHandler;
import tcp.mediator.policy.IWaitPolicy;
import tcp.mediator.policy.QueuePolicy;
import tcp.mediator.policy.SleepPolicy;

@SuppressWarnings("unused")
public class HandlerFactory
{
	private static IWaitPolicy policy = new QueuePolicy();
	
	
	public static IConnectionHandler generateNewHandler() {
//		return getEcho();
		return getMediator();
	}
	
	public static IConnectionHandler getEcho() {
		return new RaggedEchoHandler();
	}
	
	public static IConnectionHandler getMediator()
	{
		return new MediatorHandler(policy);
	}
}
