package inetTCPTesting.mediator;

public class ServerListener extends SideListener
{
	public ServerListener(MediatorHandler parent) {
		super(parent);
		setName("Server " + Integer.toString(parent.getConnectionId()));
	}
}