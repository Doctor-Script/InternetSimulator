package inetTCPTesting.mediator;

import inetTCPTesting.TCPReceiver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SideListener extends TCPReceiver
{
	protected SideListener other;
	protected OutputStream output;
	protected final MediatorHandler parent;
	
	public SideListener(MediatorHandler parent) {
		this.parent = parent;
		
	}
	
	@Override
	protected void onAccepted(InputStream sin, OutputStream output) throws IOException {
		this.output = output;
	}
	
	@Override
	protected void onReceived(byte[] buffer, int size, OutputStream output) throws IOException
	{
		System.out.println("Mediator in");
		other.output.write(buffer, 0, size);
	}
	
	void forceClose() throws IOException {
		socket.close();
	}
	
	@Override
	protected void onClosed() throws IOException {
		other.forceClose();
	}
	
	public void setOther(SideListener other) {
		this.other = other;
	}
}