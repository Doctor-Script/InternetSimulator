package inetTCPTesting.factory;

import inetTCPTesting.IConnectionHandler;
import inetTCPTesting.echo.RaggedEchoHandler;
import inetTCPTesting.mediator.MediatorHandler;

public class HandlerFactory
{
	public static IConnectionHandler generateNewHandler() {
//		return getEcho();
		return getMediator();
	}
	
	public static IConnectionHandler getEcho() {
		return new RaggedEchoHandler();
	}
	
	public static IConnectionHandler getMediator() {
		return new MediatorHandler();
	}
}
