package com.rezzcomm.reservation.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import android.util.Log;

public class MD5TimePass {

	public String[] getMD5 () {
		String[] md5 = new String[2];
		md5[0] = String.valueOf(new Date().getTime());

		String timePass = md5[0] + "rezzcomm.com";
		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance("MD5");
	        byte utf8_bytes[] = timePass.getBytes();
			m.update(utf8_bytes, 0, utf8_bytes.length);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
//		md5[1] = new BigInteger(1, m.digest()).toString(16);
		md5[1] = toHex(m.digest());

		Log.e("MD5", md5[0] + "__" + md5[1]);
		return md5;
	}
	
	private String toHex(byte[] data) {
		final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
		
		char[] chars = new char[data.length*2];
		for (int i=0; i < data.length; i++) {
			chars[i*2] = HEX_DIGITS[(data[i] >> 4) & 0xf];
			chars[i*2+1] = HEX_DIGITS[data[i] & 0xf];
		}
		
		return new String(chars);
	}
}
