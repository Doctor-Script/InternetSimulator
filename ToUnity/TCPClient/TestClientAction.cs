using UnityEngine;
using DSWhiteMagic.Networking;

// For test DSWhiteMagic.Networking.TcpClient.
// Uses server on Java
// Requires HeaderSize 2 bytes
public class TestClientAction : RemoteAction
{
	private int num = 0;
	
	public override int GetIncomingSize() {
		return 8;
	}
	
	public override void OnResponce(Pack responceBuffer)
	{
		Debug.Log("num: " + ++num);
		Debug.Log("1 : " + responceBuffer.ReadInt());
		Debug.Log("2 : " + responceBuffer.ReadInt());
		Debug.Log("ReadableBytes = " + responceBuffer.ReadableBytes);
		Debug.Log("-----------------------------------");
	}
}