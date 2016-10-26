using System;
using System.Threading;
using System.Net.Sockets;

namespace DSWhiteMagic.Networking
{
	public abstract class AbstractClient : IDisposable
	{
		private Socket socket;
		private Thread receivingThread;

		private ManualResetEvent bufferUnlockWaitEvent = new ManualResetEvent (true);
		private volatile bool hasNewData = false;
		private volatile Action eventHandler;
		private Pack receivingPack;

		protected AbstractClient(Pack receivingPack) {
			this.receivingPack = receivingPack;
		}

		public void Dispose()
		{
			Disconnect ();
			if (socket != null) {
				if (socket.Connected) {//To prevent client-side exeption on server closed
					socket.Shutdown(SocketShutdown.Both);//To prevent server-side exeption on client closed
				}
				socket.Close();
				socket = null;
			}
		}

		public bool Connect() 
		{
			if (socket == null) {
				socket = CreateSocket();
			}

			if (receivingThread == null && socket != null) {
				receivingThread = new Thread(ReceiveThread);
				receivingThread.Start();
				return true;
			}
			return false;
		}

		public bool Send(Pack data)
		{
			if (receivingThread == null) {
				return false;
			}

			int totalSended = 0;
			int toSend = data.ReadableBytes;
			do {
				int sended = 0;
				try {
					sended = socket.Send (data.ToArray (), 
				                          data.ReadIndex + totalSended, 
										  toSend, 
			                         	  SocketFlags.None);
				} catch (SocketException) {
					PushHandler(CoreOnConnectionLost);
					return false;

				}
				if (sended == 0) {
					PushHandler(CoreOnConnectionLost);
					return false;
				}
				data.BitesMoreSended(sended);
				totalSended += sended;
			} while (totalSended < toSend);

			return true;
		}

		public void Disconnect()
		{
			// not uses because uses socket.Close() on connection lost
//			if (socket != null && socket.Connected) {
//				socket.Disconnect(true);
//			}

			if (receivingThread != null) {
				if(receivingThread.IsAlive) {
					receivingThread.Abort();
				}
				receivingThread = null;
			}
		}

		private void CloseConnection() {
			if (socket != null && socket.Connected) {
				socket.Disconnect(true);
			}
		}

		public Pack GetBuffer()
		{
			if (eventHandler != null) {
				eventHandler();
				eventHandler = null;
			}

			return hasNewData ? receivingPack : null;
		}
		
		public void ReleaseBuffer()
		{
			if (hasNewData) {
				
				receivingPack.DiscardReadedBytes();

				int readableBytes = receivingPack.ReadableBytes;
				if (readableBytes < GetHeaderSize()) {
					AllowReceive();
				} else if (readableBytes < GetNextMessageSize(receivingPack)) {
					AllowReceive();
				}
			}
		}

		private void AllowReceive() {
			hasNewData = false;
			bufferUnlockWaitEvent.Set();
		}

		/// Receiving Thread
		protected virtual bool TrySocketConnect(Socket socket) { return true; } 
		protected abstract bool TryReceiveFull (Socket socket, Pack readPack);
		protected abstract Socket CreateSocket();

		/// Uses in receiving Thread
		private void ReceiveThread()
		{
			if (TrySocketConnect(socket)) 
			{
				PushHandler(OnConnected);

				while (socket != null)
				{
					bufferUnlockWaitEvent.WaitOne();
					if(!TryReceiveFull(socket, receivingPack)) {
						break;
					}
					hasNewData = true;
					bufferUnlockWaitEvent.Reset();
				}
			}
			PushHandler(CoreOnConnectionLost);
		}

		/// Both Threads
		protected virtual int GetNextMessageSize(Pack pack) {
			return 1;//at least one byte
		}

		/// Both Threads
		protected virtual int GetHeaderSize() {
			return 1;
		}

		/// Both Threads
		protected void PushHandler(Action handler) {
			eventHandler += handler;
		}

		protected virtual void OnConnected() {}
		protected virtual void OnConnectionLost() {}
		protected void CoreOnConnectionLost() {
			Dispose();
			OnConnectionLost();
		}
	}
}