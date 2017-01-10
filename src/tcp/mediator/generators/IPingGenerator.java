package tcp.mediator.generators;

import java.io.IOException;
import java.io.OutputStream;

public interface IPingGenerator {
	void setPingFor(OutputStream targetStream, byte[] buffer, int size) throws IOException;
}