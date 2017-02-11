package com.exam.support.util;

/**
 * Util for simple operation on bytes(byte array).
 * 
 * @author hewen.deng
 */
public class SimpleBytesUtil {

	private static final char[] HEX_CHAR = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F'
	};

	/**
	 * Convert every byte in a byte array to hex present. <br>
	 * One byte would be present as two hex chars.
	 * 
	 * @param bytes
	 * @return
	 * @see #toHexString(byte[], int, int)
	 */
	public static String toHexString(byte[] bytes) {
		return toHexString(bytes, 0, bytes.length);
	}

	/**
	 * Convert {@code length} byte in a byte array, start from {@code offset},
	 * to hex present. <br>
	 * One byte would be present as two hex chars.
	 * 
	 * @param bytes the bytes to be converted to hex chars
	 * @param offset the index of the first byte to convert 
	 * @param length the number of bytes to convert
	 * @return
	 * @throws  IndexOutOfBoundsException
	 * 			If the {@code offset} and {@code length} arguments index
	 *			characters outside the bounds of the {@code bytes} array
	 */
	public static String toHexString(byte[] bytes, int offset, int length) {
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		if (length < 0) {
			throw new IndexOutOfBoundsException("length is negative");
		}
		if (offset < 0) {
			throw new IndexOutOfBoundsException("offset is negative");
		}
		if (offset > bytes.length - length) {
			throw new IndexOutOfBoundsException("outside the bounds of bytes array");
		}
		StringBuilder hs = new StringBuilder(length * 3);
		for (int i = offset, l = offset + length; i < l; i++) {
			int b = bytes[i];
			if (b < 0) {
				b += 256;
			}
			int hex1 = b & 0x0F;
			int hex2 = b >> 4;
			hs.append(HEX_CHAR[hex2]);
			hs.append(HEX_CHAR[hex1]);
			hs.append(' ');
		}
		hs.deleteCharAt(hs.length() - 1);
		return hs.toString();
	}

	/**
	 * To decimal string.
	 * 
	 * @param bytes
	 * @return
	 */
	public static String toDecString(byte[] bytes) {
		return toDecString(bytes, 0, bytes.length);
	}

	/**
	 * To decimal string.
	 * 
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return
	 */
	public static String toDecString(byte[] bytes, int offset, int length) {
		check(bytes, offset, length);
		StringBuilder hs = new StringBuilder(length * 3);
		for (int i = offset, l = offset + length; i < l; i++) {
			hs.append(bytes[i]).append(' ');
		}
		return hs.toString();
	}

	private static void check(byte[] bytes, int offset, int length) {
		if (bytes == null || bytes.length == 0) {
			throw new RuntimeException("empty bytes");
		}
		if (length < 0) {
			throw new IndexOutOfBoundsException("length is negative");
		}
		if (offset < 0) {
			throw new IndexOutOfBoundsException("offset is negative");
		}
		if (offset > bytes.length - length) {
			throw new IndexOutOfBoundsException("outside the bounds of bytes array");
		}	
	}

	/**
	 * 
	 * @param bytes
	 * @return
	 */
	private static String toBinaryString(byte[] bytes) {
		return "";
	}

	/**
	 * @see SimpleBytesUtil#toBEInt(byte[], int)
	 * @param bytes
	 * @return
	 */
	public static int toBEInt(byte[] bytes) {
		return toBEInt(bytes, 0);
	}

	/**
	 * Convert bytes to 32-bits integer by <b>BIG-ENDIAN</b>.
	 *  
	 * @param bytes
	 * @param offset
	 * @return
	 */
	public static int toBEInt(byte[] bytes, int offset) {
		if (bytes == null || bytes.length == 0) {
			throw new RuntimeException("empty bytes");
		}
		if (offset < 0) {
			throw new IndexOutOfBoundsException("offset is negative");
		}
		if (offset + 4 > bytes.length) {
			throw new IndexOutOfBoundsException("outside the bounds of bytes array");
		}
		return (((bytes[offset + 0] & 0xFF) << 24) + ((bytes[offset + 1] & 0xFF) << 16)
				+ ((bytes[offset + 2] & 0xFF) << 8) + ((bytes[offset + 3] & 0xFF) << 0));
	}

}
