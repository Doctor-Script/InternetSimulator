package inetTCPTesting.factory;

import inetTCPTesting.IConnectionHandler;
import inetTCPTesting.echo.RaggedEchoHandler;
import inetTCPTesting.mediator.MediatorHandler;
import inetTCPTesting.mediator.policy.IWaitPolicy;
import inetTCPTesting.mediator.policy.QueuePolicy;
import inetTCPTesting.mediator.policy.SleepPolicy;

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
