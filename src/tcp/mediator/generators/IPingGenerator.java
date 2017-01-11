package tcp.mediator.generators;

import java.io.IOException;
import java.net.Socket;

public interface IPingGenerator {
	void setPingFor(Socket target, byte[] buffer, int size) throws IOException;
}