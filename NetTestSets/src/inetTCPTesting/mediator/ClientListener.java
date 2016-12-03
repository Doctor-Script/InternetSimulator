package inetTCPTesting.mediator;

public class ClientListener extends SideListener
{
	public ClientListener(MediatorHandler parent) {
		super(parent);
		setName("Client  " + Integer.toString(parent.getConnectionId()));
	}
}