package tcpTests.senders;

import java.io.IOException;

//Need two bytes header
public class SequencedSender extends TestMessageSender
{
	Element[] elements = { new FirstHeaderPart(), new SecondHeaderPart(), new FirstValue(), new SecondValue() };
	int num = 0;

	@Override
	public void sendTestMessages() throws IOException, InterruptedException {
		for (int i = 0; i < 8; i++)
		{
			SendTestMessages(i);
		}
	}

	abstract class Element {
		int value;
		
		abstract void addElement() throws IOException;
		
		void writeByte(int value) throws IOException {
			this.value = value;
			stream.writeByte(value);
		}
		
		void writeInt(int value) throws IOException {
			this.value = value;
			stream.writeInt(value);
		}
		
		@Override
		public String toString() {
			return Integer.toString(value);
		}
	}
	
	class FirstHeaderPart extends Element {
		void addElement() throws IOException {
			writeByte(0);
		}
	}
	
	class SecondHeaderPart extends Element {
		void addElement() throws IOException {
			writeByte(TEST);
		}
	}
	
	class FirstValue extends Element {
		void addElement() throws IOException {
			writeInt(getValue1());
		}
	}
	
	class SecondValue extends Element {
		void addElement() throws IOException {
			writeInt(getValue2());
		}
	}

	void SendTestMessages(int elemenstBefore) throws IOException, InterruptedException {
		
		int numElements = elements.length;
		print("before: " + Integer.toString(elemenstBefore));
		int numMessages = (int)(elemenstBefore / numElements) + 1;
		int size = numMessages * numElements;
		
		for (int i = 0; i < size; i++)
		{
			int id = i % numElements;
			if (id == 0) {
				print("num: " + Integer.toString(++num));
			}
			elements[id].addElement();
			print(elements[id].toString());
			if (elemenstBefore - 1 == i) {
				print("Sleep");
				stream.flush();
				Thread.sleep(SLEEP);
			}
		}
		print("End------------------------------");
		stream.flush();
	}
}