using System;
using System.Text;
using UnityEngine;

namespace DSWhiteMagic.Networking
{
	//запретить запсить болье чем есть места
	//не читать если индекс чтения достиг индекс записи
	public class Pack
	{
		byte[] data;
		int writeIndex = 0;
		int readIndex = 0;
		
		public Pack(int capacity) 
		{
			data = new byte[capacity];
		}

		public Pack(byte[] data, int writeIndex) {
			this.data = data;
			this.writeIndex = writeIndex;
		}
		
		public byte[] ToArray() 
		{
			return data;
		}

		public int Capacity {
			get { return data.Length; }
		}

		public int ReadableBytes {
			get { return writeIndex - readIndex; }
		}

		public int WritableBytes {
			get { return data.Length - writeIndex; }
		}

		public int WriteIndex {
			get { return writeIndex; }
		}

		public int ReadIndex {
			get { return readIndex; }
		}

		public void Clear() 
		{
			writeIndex = 0;
			readIndex = 0;
		}


		private void SaveToData(byte[] bytes) 
		{
			for (int i = bytes.Length-1; i >= 0; i--) {
				data[writeIndex] = bytes[i];
				writeIndex++;
			}
		}

		public Pack Push(short val)
		{
			SaveToData(BitConverter.GetBytes(val));
			return this;
		}
		
		public Pack Push(int val)
		{
			SaveToData(BitConverter.GetBytes(val));
			return this;
		}

		public Pack Push(float val)
		{
			SaveToData(BitConverter.GetBytes(val));
			return this;
		}

		public Pack Push(string val)
		{
			SaveToData (Encoding.ASCII.GetBytes (val));
			return this;
		}

		public Pack Push(byte val)
		{
			data [writeIndex] = val;
			writeIndex += 1;
			return this;
		}







		private byte[] Flip(byte[] bytes, int index, int size)
		{
			byte[] bs = new byte[size];
			for (int i = bs.Length-1; i >= 0; i--) {
				bs[i] = bytes[index];
				index++;
			}
			return bs;
		}

		public short ReadSort()
		{
			short val = BitConverter.ToInt16 (Flip(data, readIndex, sizeof(short)), 0);
			readIndex += sizeof(short);
			return val;
		}

		public int ReadInt()
		{
//			if (ReadableBytes - sizeof(int) < 0) {
//				throw new IndexOutOfRangeException();
//			}
			int val = BitConverter.ToInt32 (Flip(data, readIndex, sizeof(int)), 0);
			readIndex += sizeof(int);
			return val;
		}

		public float ReadFloat()
		{
			float val = BitConverter.ToSingle (Flip(data, readIndex, sizeof(float)), 0);
			readIndex += sizeof(float);
			return val;
		}
		
		public byte ReadByte()
		{
			byte b = data [readIndex];
			readIndex += 1;
			return b;
		}

		public void DiscardReadedBytes()
		{
			int i;
			int j = 0;//убрать переменную
			for (i = readIndex; i < writeIndex; i++) {
				data[j++] = data[i];
			}
			readIndex = 0;
			writeIndex = j;
		}

		public byte GetByte()
		{
			byte b = data [readIndex];
			return b;
		}

		public short GetShort()
		{
			short val = BitConverter.ToInt16(Flip(data, readIndex, sizeof(short)), 0);
			return val;
		}

		public int ReadLength {
			get {
				return readIndex;
			}

		}

		public void _BytesMoreReaded(int i)
		{
			if (i > WritableBytes) {
				throw new IndexOutOfRangeException();
			}
			writeIndex += i;
		}

		public void BitesMoreSended(int numBytes) {
			readIndex += numBytes;
		}

		public void PrintData(int from, int size)
		{
			for (int i = from ; i < size; i++)
			{
				Debug.Log(data[i]);
			}
		}

		//Not Tested
		public void CheckSize(int numExpectedBytes)
		{
			if (WritableBytes < numExpectedBytes) {
				Debug.Log("upgrade size");
				byte[] newData = new byte[data.Length * 2];
				for (int i = 0; i < data.Length; i++) {
					newData[i] = data[i];
				}
			}
		}
	}
}