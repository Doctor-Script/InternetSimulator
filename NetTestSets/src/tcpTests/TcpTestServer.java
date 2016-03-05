package tcpTests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tcpTests.senders.*;

public class TcpTestServer {

	static ServerSocket serverSocket;
	static int port = 8080;
	
	static void processConnection(TestMessageSender testMessageSender)
	{
		try {
			serverSocket = new ServerSocket(port);
			while (true)
			{
				System.out.println("wait for connections");
				
				try (Socket socket = serverSocket.accept()) {
					DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
					testMessageSender.setStream(stream);
					testMessageSender.sendTestMessages();
				}
			}
		} catch(Exception e) {
			System.out.println(e.getStackTrace().toString());
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {}
		}
	}
	
	public static void main(String[] args) {
		processConnection(new SequencedSender());
//		processConnection(new RandomSender());
	}
}
