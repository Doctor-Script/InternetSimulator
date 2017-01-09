package inetTCPTesting.mediator.policy;

import java.io.IOException;
import java.io.OutputStream;

public interface IWaitPolicy {
	void pushMessage(OutputStream output, byte[] buffer, int size) throws IOException;
}
