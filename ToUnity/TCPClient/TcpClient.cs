using System;
using System.Net;
using System.Net.Sockets;

namespace DSWhiteMagic.Networking
{
	public class TcpClient : AbstractClient
	{
		protected IPEndPoint remoteEndPoint;

		public TcpClient(IPAddress addtess, int port, Pack receivingPack): base(receivingPack)
		{
			if (addtess != null) {
				remoteEndPoint = new IPEndPoint(addtess, port);
			}
		}

		protected override Socket CreateSocket()
		{
			return new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
		}

		protected override bool TrySocketConnect(Socket socket) 
		{
			try {
				socket.Connect(remoteEndPoint);
				return true;
			} catch {
				return false;
			}
		}

		private int GetExpectedBytes(Pack readPack)
		{
			int headerSize = GetHeaderSize();
			if (readPack.ReadableBytes >= headerSize) {
				return GetNextMessageSize(readPack);
			}
			return headerSize;
		}

		protected override bool TryReceiveFull(Socket socket, Pack readPack)
		{
			int expectedBytes = 0;
			do {
				expectedBytes = GetExpectedBytes(readPack);
				readPack.CheckSize(expectedBytes);

				int receivedBytes = socket.Receive (readPack.ToArray(),
				                                readPack.WriteIndex,
				                                readPack.WritableBytes,
				                                SocketFlags.None);

				if (receivedBytes == 0) {
					return false;
				}
				readPack._BytesMoreReaded (receivedBytes);
				expectedBytes = GetExpectedBytes(readPack);
			}
			while(readPack.ReadableBytes < expectedBytes);
			
			return true;
		}
	}
}