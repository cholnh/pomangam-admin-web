package com.mrporter.pomangam.common.security.crypt;

import java.math.BigInteger;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * RSA
 * 
 * @version 1.0 [2018. 1. 17.]
 * @author Choi
 */
public class RSA {

	public String decrypt(PrivateKey privateKey, String securedValue) {
		try {
			return decryptRsa(privateKey, securedValue);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
    private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] encryptedBytes = hexToByteArray(securedValue);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
        return decryptedValue;
    }

    /**
     * 16진 문자열을 byte 배열로 변환한다.
     */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() % 2 != 0) {
            return new byte[]{};
        }

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            byte value = (byte)Integer.parseInt(hex.substring(i, i + 2), 16);
            bytes[(int) Math.floor(i / 2)] = value;
        }
        return bytes;
    }

    /**
     * BigInteger를 사용해 hex를 byte[] 로 바꿀 경우 음수 영역의 값을 제대로 변환하지 못하는 문제가 있다.
     */
    @Deprecated
    public static byte[] hexToByteArrayBI(String hexString) {
        return new BigInteger(hexString, 16).toByteArray();
    }

    public static String base64Encode(byte[] data) throws Exception {
    BASE64Encoder encoder = new BASE64Encoder();
    String encoded = encoder.encode(data);
    return encoded;
    }

    public static byte[] base64Decode(String encryptedData) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] decoded = decoder.decodeBuffer(encryptedData);
        return decoded;
    }
}
