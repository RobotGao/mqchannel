package com.robot.channel.worker.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Encodes {
	/**
	 * <p>
	 * 属性描述: [默认编码格式]
	 * </p>
	 */
	private static final String DEFAULT_URL_ENCODING = "UTF-8";

	/**
	 * <p>
	 * 属性描述: [自定义key]
	 * </p>
	 */
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * <p>
	 * 类名称: [MD5 入参为String]
	 * <p>
	 */
	public static String encodeMD5Hex(String data) {
		return DigestUtils.md5Hex(data);
	}

	/**
	 * <p>
	 * 类名称: [MD5 入参为byte]
	 * <p>
	 */
	public static String encodeMD5Hex(byte[] data) {
		return DigestUtils.md5Hex(data);
	}

	/**
	 * <p>
	 * 类名称: [Hex编码.]
	 * <p>
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * <p>
	 * 类名称: [Hex解码.]
	 * <p>
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <p>
	 * 类名称: [Base64编码.]
	 * <p>
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	/**
	 * <p>
	 * 类名称: [Base64编码, URL安全(将Base64中的URL非法字符'+'�?/'转为'-'�?_', 见RFC3548).]
	 * <p>
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * <p>
	 * 类名称: [Base64解码.]
	 * <p>
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * <p>
	 * 类名称: [Base62编码]
	 * <p>
	 */
	public static String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];

		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}

		return new String(chars);
	}

	/**
	 * <p>
	 * 类名称: [Html 转码.]
	 * <p>
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * <p>
	 * 类名称: [Html 解码.]
	 * <p>
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * <p>
	 * 类名称: [Xml 转码.]
	 * <p>
	 */
	@SuppressWarnings("deprecation")
  public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * <p>
	 * 类名称: [Xml 解码.]
	 * <p>
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * <p>
	 * 类名称: [URL 编码, Encode默认为UTF-8.]
	 * <p>
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * <p>
	 * 类名称: [URL 解码, Encode默认为UTF-8.]
	 * <p>
	 */
	public static String urlDecode(String part) {
		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
}
