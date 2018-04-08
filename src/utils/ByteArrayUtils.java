package utils;

public class ByteArrayUtils
{
	public static String toString(byte[] array, int start, int length)
	{
		start = (start < 0 || start > array.length) ? 0 : start;
		length = (length < 0 || length > array.length) ? array.length : length;
		int end = length + start;
		
		StringBuilder builder = new StringBuilder("[");
		for(int i = start; i < end; i++)
		{
			byte b = array[i];
			builder.append(b);
			if (i < end - 1) {
				builder.append(", ");
			}
		}
		
		builder.append("]");
		return builder.toString();
	}
	
	public static byte[] multiply(byte[] array, int offset, int length, int multiplier)
	{
		offset = (offset < 0 || offset > array.length) ? 0 : offset;
		length = (length < 0 || length > array.length) ? array.length : length;
		
		byte[] result = new byte[length * multiplier];
		
		for (int copyId = 0; copyId < multiplier; copyId++) {
			System.arraycopy(array, offset, result, copyId * length, length);
		}
		return result;
	}
}
