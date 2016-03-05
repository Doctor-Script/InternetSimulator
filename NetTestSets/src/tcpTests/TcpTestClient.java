package tcpTests;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

import tcpTests.senders.*;

public class TcpTestClient {

	static String host = "localhost";
	static int port = 8080;
	
	static void processConnection(TestMessageSender testMessageSender)
	{
		Scanner in = new Scanner(System.in);
		try {
			while (true)
			{
				System.out.println("press to send");
				in.nextLine();
				System.out.println("sending");
				
				try(Socket socket = new Socket(host, port)) {
					DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
					testMessageSender.setStream(stream);
					testMessageSender.sendTestMessages();
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			in.hasNext();
		}
		finally {
			in.close();
		}
	}
	
	public static void main(String[] args) {
//		processConnection(new SequencedSender());
		processConnection(new RandomSender(50));
	}
}
