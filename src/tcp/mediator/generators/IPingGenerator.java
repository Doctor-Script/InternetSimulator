package tcp.mediator.generators;

import java.io.IOException;

import tcp.mediator.SideListener;

public interface IPingGenerator {
	void setPingFor(SideListener target, byte[] buffer, int size) throws IOException;
	void onConnectionEnd(SideListener source);
}