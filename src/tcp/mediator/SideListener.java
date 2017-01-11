package tcp.mediator;

import java.io.IOException;
import java.io.OutputStream;

import tcp.TCPReceivingThread;

public class SideListener extends TCPReceivingThread
{
	protected SideListener other;
	protected OutputStream output;
	protected final MediatorHandler parent;
	
	public SideListener(MediatorHandler parent, String name) {
		this.parent = parent;
		setName(name);
	}
	
	@Override
	protected void onReceived(byte[] buffer, int size) throws IOException {
		parent.pingGenerator.setPingFor(other.socket, buffer, size);
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