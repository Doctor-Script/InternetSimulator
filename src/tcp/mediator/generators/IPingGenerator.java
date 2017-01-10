package tcp.mediator.generators;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public interface IPingGenerator {
	void setPingFor(OutputStream targetStream, byte[] buffer, int size, Socket socket) throws IOException;
}