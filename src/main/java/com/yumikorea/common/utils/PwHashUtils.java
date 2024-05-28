package com.yumikorea.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class PwHashUtils {
	
	public static String randomString(int len) {
		StringBuffer strBuffer = new StringBuffer();
		String iv = null, ivS = null;
		
		Random rd = new Random();
		
		for( int i = 0; i < len; i++ ) {
			if( iv == null || iv.length() < len ){
				ivS = Integer.toHexString(rd.nextInt());
				iv = strBuffer.append(ivS).toString();
			}
			if( iv.length() > len ) {
				iv = iv.substring(0, len);
				break;
			}
			if( iv.length() == len ) break;
		}

		return iv;
	}
	
	public static String getPwHash( String pw ) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance( "SHA-256" );
		
		byte[] hashedPw = md.digest( pw.getBytes() );
		
		return DatatypeConverter.printHexBinary( hashedPw ).toLowerCase();
	}
	
	public static String getPwHash( String pw, String id ) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance( "SHA-256" );
		
		byte[] hashedPw = md.digest( pw.getBytes() );
		
		md.update( hashedPw );
		md.update( id.getBytes() );
		
		byte[] finalPw = md.digest();
		
		return  DatatypeConverter.printHexBinary( finalPw ).toLowerCase();
	}
	
}
