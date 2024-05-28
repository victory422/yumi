package com.yumikorea.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class AesUtil {

	private static final int keySize = 128;
	private static final int iterationCount = 10000;
	public static final String salt = "79752f1d3fd2432043c48e45b35b24645eb826a25c6f1804e9152665c345a552";
	public static final String iv = "2fad5a477d13ecda7f718fbd8a9f0443";
	public static final String passPhrase = "730537FCF8DDD7AB7A2B43478AA70A0B";
//	public static String salt = MagicKMIPConstants.ACCOUNT_ENCRIPT_SALT;
//	public static String iv = MagicKMIPConstants.ACCOUNT_ENCRIPT_IV;
//	public static String passPhrase = MagicKMIPConstants.ACCOUNT_ENCRIPT_KEY;
	

	public static final String aesCBC = "AES/CBC/PKCS5Padding";
	public static final String aesCFB = "AES/CFB/PKCS5Padding";
	public static final String aesOFB = "AES/OFB/PKCS5Padding";
	
	public static final String mode = aesCBC;
	
	public static String encrypt(String plaintext) {
		if( plaintext == null ) return null;
		return encrypt(salt, iv, passPhrase, plaintext);
	}

	public static String decrypt(String ciphertext) {
		if( ciphertext == null ) return null;
		return decrypt(salt, iv, passPhrase, ciphertext);
	}

	private static String encrypt(String salt, String iv, String passPhrase, String plaintext) {
		if( plaintext == null ) return null;
		SecretKey key;
		byte[] encrypted;
		try {
			key = generateKey(salt, passPhrase);
			encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return encodeBase64(encrypted);
	}

	private static String decrypt(String salt, String iv, String passPhrase, String ciphertext){
		if( ciphertext == null ) return null;
		SecretKey key;
		byte[] decrypted;
		try {
			key = generateKey(salt, passPhrase);
			decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, decodeBase64(ciphertext));
			return new String(decrypted, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(mode);
			cipher.init(encryptMode, key, new IvParameterSpec(decodeHex(iv)));
			return cipher.doFinal(bytes);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException 
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

	private static SecretKey generateKey(String salt, String passPhrase) {
		SecretKeyFactory factory;
		SecretKey key;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), decodeHex(salt), iterationCount, keySize);
			key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
			return key;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private static String encodeBase64(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	private static byte[] decodeBase64(String str) {
		return Base64.decodeBase64(str);
	}

	private static String encodeHex(byte[] bytes) {
		return DatatypeConverter.printHexBinary(bytes);
//		return Hex.encodeHexString(bytes);
	}

	private static byte[] decodeHex(String str) {
		return DatatypeConverter.parseHexBinary(str);
//		return Hex.decodeHex(str.toCharArray());
	}

	@SuppressWarnings("unused")
	private static String getRandomHexString(int length) {
		byte[] salt = new byte[length];
		new SecureRandom().nextBytes(salt);
		return encodeHex(salt);
	}
	
}
