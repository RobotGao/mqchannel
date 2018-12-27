package com.robot.channel.proxy.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.DecimalFormat;

public class DataConvertor {

	public static byte[] shortToBytes(Short s) {
		if ((s > Short.MAX_VALUE) || (s < Short.MIN_VALUE)) {
			return null;
		}
		byte[] bs = new byte[2];
		Short temp = s;
		for (int i = 0; i < 2; i++) {
			int offset = (bs.length - 1 - i) * 8;
			bs[i] = (byte) ((temp >>> offset) & 0xff);
		}
		return bs;
	}

	public static byte[] intToByteArray(int n) {
		if (n > Integer.MAX_VALUE || n < Integer.MIN_VALUE) {
			return null;
		}
		byte[] bs = new byte[4];
		int temp = n;
		for (int i = 0; i < 4; i++) {
			int offset = (bs.length - 1 - i) * 8;

			bs[i] = (byte) ((temp >>> offset) & 0xff);
		}
		return bs;
	}

	public static byte[] longToBytes(long l) {
		if ((l > Long.MAX_VALUE) || (l < Long.MIN_VALUE)) {
			return null;
		}
		byte[] bs = new byte[8];
		long temp = l;
		for (int i = 0; i < 8; i++) {
			int offset = (bs.length - 1 - i) * 8;
			bs[i] = (byte) ((temp >>> offset) & 0xff);
		}
		return bs;
	}

	public static long byteToUnsignInt(byte[] bs) {
		int mask = 0xff;
		int temp = 0;
		long res = 0;
		int byteslen = bs.length;

		if (byteslen > 8) {
			return Long.valueOf(0L);
		}

		for (int i = 0; i < byteslen; i++) {
			res <<= 8;
			temp = bs[i] & mask;
			res |= temp;
		}
		return res;
	}

	public static long byteToSignInt(byte[] bs) {
		int mask = 0xff;
		int temp = 0;
		long res = 0L;
		int byteslen = bs.length;

		if (byteslen > 8) {
			return Long.valueOf(0L);
		}

		for (int i = 0; i < byteslen; i++) {
			res <<= 8;
			if (i == 0 && bs[i] < 0) {
				temp = bs[i];
			} else {
				temp = bs[i] & mask;
			}
			res |= temp;
		}

		return res;
	}

	public static int getUnsignedByte(byte data) {

		return data & 0x0FF;
	}

	public static int getUnsignedByte(Short data) {

		return data & 0x0FFFF;
	}

	public static long getUnsignedInt(int data) {

		return data & 0x0FFFFFFFFL;
	}

	public static Short hBytesToShort(byte[] b) {
		int s = 0;
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		s = s * 256;
		if (b[1] >= 0) {
			s = s + b[1];
		} else {
			s = s + 256 + b[1];
		}
		short result = (short) s;
		return result;
	}

	public static int hBytesToInt(byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[i] >= 0) {
				s = s + b[i];
			} else {
				s = s + 256 + b[i];
			}
			s = s * 256;
		}
		if (b[3] >= 0) {
			s = s + b[3];
		} else {
			s = s + 256 + b[3];
		}

		return s;
	}

	public static int byte2int(byte[] res) {
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

	public static String getBit(byte[] b) {
		StringBuffer sbf = new StringBuffer();
		int[] bit = new int[8];

		for (int j = 0; j < b.length; j++) {
			for (int i = 0; i < bit.length; i++) {
				bit[8 - i - 1] = (b[j] >> i) & 1;
			}
			for (int i : bit) {
				sbf.append(i);
			}
		}
		return sbf.toString();
	}

	public static String binaryToDate(byte[] b) {

		String binary = getBit(b);
		if (binary == null || "".equals(binary) || binary.length() < 32) {
			return null;
		}
		String date = "";
		String yearBin = binary.substring(0, 6);
		String monthBin = binary.substring(6, 10);
		String dayBin = binary.substring(10, 15);
		String hourBin = binary.substring(15, 20);
		String minBin = binary.substring(20, 26);
		String secBin = binary.substring(26, 32);

		String year = new DecimalFormat("00").format(Integer.valueOf(yearBin, 2));
		String month = new DecimalFormat("00").format(Integer.valueOf(monthBin, 2));
		String day = new DecimalFormat("00").format(Integer.valueOf(dayBin, 2));
		String hour = new DecimalFormat("00").format(Integer.valueOf(hourBin, 2));
		String min = new DecimalFormat("00").format(Integer.valueOf(minBin, 2));
		String second = new DecimalFormat("00").format(Integer.valueOf(secBin, 2));
		date = "20" + year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + second;
		return date;
	}

	public static byte[] dateToBitArray(String date) throws Exception {

		String temp = DataTimeFormat.dataOrTimeFormat(date, "yyyy-MM-dd HH:mm:ss", "yyMMddHHmmss");
		byte[] bDate = new byte[4];
		String bins = "";

		int year = Integer.parseInt(temp.substring(0, 2));
		int mouth = Integer.parseInt(temp.substring(2, 4));
		int day = Integer.parseInt(temp.substring(4, 6));

		int hour = Integer.parseInt(temp.substring(6, 8));
		int min = Integer.parseInt(temp.substring(8, 10));
		int sec = Integer.parseInt(temp.substring(10, 12));

		bins = addZeroForString(Integer.toBinaryString(year), 6) + addZeroForString(Integer.toBinaryString(mouth), 4)
				+ addZeroForString(Integer.toBinaryString(day), 5) + addZeroForString(Integer.toBinaryString(hour), 5)
				+ addZeroForString(Integer.toBinaryString(min), 6) + addZeroForString(Integer.toBinaryString(sec), 6);

		for (int i = 0; i < 4; i++) {
			String bin = bins.substring(i * 8, (i + 1) * 8);
			int tmpInt = Integer.valueOf(bin, 2);
			bDate[i] = (byte) tmpInt;
		}
		return bDate;
	}

	public static String addZeroForString(String str, int strLength) {
		int strLen = str.length();
		StringBuffer sb = new StringBuffer();
		if (strLen < strLength) {
			while (strLen < strLength) {
				sb.append("0");
				strLen++;
			}
		}
		sb.append(str);
		return sb.toString();
	}

	public static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String tmp;
		StringBuffer bString = new StringBuffer("");
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
			bString.append(tmp.substring(tmp.length() - 4));
		}
		return bString.toString();
	}

	public static byte[] hexToBytes(String[] hexStringArrary) {
		StringBuffer strbf = new StringBuffer();
		for (String tmpStr : hexStringArrary) {
			strbf.append(tmpStr);
		}
		String hexString = strbf.toString();
		if (hexString == null || "".equals(hexString)) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	public static byte[] hexToBytes(String hexString) {

		if (hexString == null || "".equals(hexString)) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	public static String binaryStringToHexString(String bString) {
		if (bString == null || bString.equals("") || bString.length() % 8 != 0)
			return null;
		StringBuffer tmp = new StringBuffer();
		int iTmp = 0;
		for (int i = 0; i < bString.length(); i += 4) {
			iTmp = 0;
			for (int j = 0; j < 4; j++) {
				iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
			}
			tmp.append(Integer.toHexString(iTmp));
		}
		return tmp.toString();
	}

	private static byte charToByte(char c) {
		int index = "0123456789ABCDEF".indexOf(c);
		if (index > -1) {
			return (byte) index;
		} else {
			// throw new Exception("数据转换出现错误。传入的16进制数不合法！c" + c);
			return 0;
		}
	}

	public static String byteToHexString(byte[] b) {
		return Encodes.encodeHex(b);
	}

	public static String strRightFill(String srcStr, String fillStr, int tarLen) {
		if (StringUtils.isNotBlank(fillStr) && tarLen > 0) {
			StringBuffer strBf = new StringBuffer();
			if (StringUtils.isNotBlank(srcStr)) {
				int srcStrLen = srcStr.length();
				strBf.append(srcStr);
				while (srcStrLen < tarLen) {
					strBf.append(fillStr);
					srcStrLen = fillStr.length() + srcStrLen;
				}
			} else {
				int srcStrLen = 0;
				while (srcStrLen < tarLen) {
					strBf.append(fillStr);
					srcStrLen = fillStr.length() + srcStrLen;
				}
			}
			return strBf.toString();
		} else {
			return srcStr;
		}
	}

	public static Object bytesToObject(byte[] bytes) {
		Object o = null;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);

			ObjectInputStream oi = new ObjectInputStream(in);

			o = oi.readObject();

			oi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;

	}

	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
				+ (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
				+ (byte) ((b >> 0) & 0x1);
	}

	public static byte[] objectToBytes(Serializable s) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ObjectOutputStream ot = new ObjectOutputStream(out);

			ot.writeObject(s);

			ot.flush();

			ot.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	public static byte returnByte(String a) {
		char[] arr = a.toCharArray();
		int one = Integer.parseInt(String.valueOf(arr[0]));
		int two = Integer.parseInt(String.valueOf(arr[1]));
		int result = one * 16 + two;
		return (byte) result;
	}

	public static String[] split(String a) {
		String[] strs = new String[a.length() / 2];
		for (int i = 0; i < strs.length; i++) {
			if (i == 0) {
				strs[i] = a.substring(i, i + 2);
			} else {
				strs[i] = a.substring(i * 2, i * 2 + 2);
			}

		}
		return strs;
	}
}
