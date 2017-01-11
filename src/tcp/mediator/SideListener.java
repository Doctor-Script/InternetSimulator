package tcp.mediator;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import tcp.TCPReceivingThread;
import tcp.config.Config;
import utils.ByteArrayUtils;

public class SideListener extends TCPReceivingThread
{
	private static final Logger log = Config.getLoggerFor(SideListener.class);
	
	protected SideListener other;
	protected OutputStream output;
	protected final MediatorHandler parent;
	
	public SideListener(MediatorHandler parent, String name)
	{
		super(log);
		this.parent = parent;
		setName(name);
	}
	
	@Override
	protected void onReceived(byte[] buffer, int size) throws IOException
	{
		logger.info(ByteArrayUtils.toString(buffer, 0, size));
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