package com.yumikorea.common.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class MDBUtils
{
	public static final String DEFAULT_RANDOM_GENERATOR_ALGORITHM = "SHA1PRNG";
	public static final String DEFAULT_KEY_DIGEST_GENERATOR_ALGORITHM = "MD5";

    public static boolean isOnWindows() {
    	boolean rst = false;
    	String osName = System.getProperties().getProperty("os.name");
    	if( osName != null ) rst = osName.toLowerCase().indexOf("windows") >= 0;
    	return rst;
    }

    public static String getTempDir() {

    	return System.getProperty("java.io.tmpdir");
    }

    public static boolean isExist( Object obj ) {

		if( obj instanceof String ) {
			return ((String)obj).length() > 0;
		}
		if( obj instanceof String[] ) {
			return ((String[])obj).length > 0;
		}
		else if( obj instanceof byte[] ) {
			return ((byte[])obj).length > 0;
		}
		else if( obj instanceof File ) {
			return ((File)obj).exists();
		}
		else if( obj != null ) {
			return true;
		}

		return false;
	}

	public static String hexString( byte[] bytes ) {

		if( bytes != null && bytes.length > 0 )
		{
			String result = (new BigInteger( 1, bytes )).toString( 16 );

			if( result.length() % 2 != 0 ) {
				return "0" + result;
			}

			return result;
		}

		return "";
	}

	public static byte[] hexToBytes( String hexstr ) {

		if( hexstr != null && hexstr.length() > 0 ) {
			return (new BigInteger( hexstr, 16 )).toByteArray();
		}

		return new byte[0];
	}

	public static byte[] toBytes( long value ) {

		return BigInteger.valueOf( value ).toByteArray();
	}

	public static SecureRandom newSecureRandom() throws NoSuchAlgorithmException {

		return SecureRandom.getInstance( DEFAULT_RANDOM_GENERATOR_ALGORITHM );
	}

	public static File getLocation() {

		return getLocation( MDBUtils.class );
	}

	
	public static File getLocation( /*@SuppressWarnings("rawtypes")*/ Class<MDBUtils> clazz ) {
		File file;
		if( clazz.getProtectionDomain() != null ) {
			CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
			if( codeSource != null ) {
				URL url = codeSource.getLocation();
				if( url != null ) {
					file = new File( url.getPath());
					file = file.isDirectory() ? file : file.getParentFile();
					return file; 
				}
			}
		}

		try {
			String className = clazz.getName().replace('.', '/')+".class";
			if( ClassLoader.getSystemClassLoader() != null ) {
				URL url = ClassLoader.getSystemClassLoader().getResource(className);
				if( url != null ) {
					String path2 = URLDecoder.decode(url.getPath(), "utf-8");
					file = new File( path2 );
					if( file != null ) file = file.isDirectory() ? file : file.getParentFile();
					if( file != null ) return file;
				}
			} else {
				int beginIndex = className.lastIndexOf("/");
				String simpleName = beginIndex >= 0 && beginIndex++ < className.length() ? className.substring(beginIndex) : className; // same : getSimpleName()
				if( clazz.getResource(simpleName) != null ) {
					file = new File( clazz.getResource(simpleName).getPath());
					if( file != null ) file = file.isDirectory() ? file : file.getParentFile();
					return file;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}