package csc309.A4;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;

public class SecurePassword {
	private static int getSecureRandom() throws NoSuchAlgorithmException {
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		return sr.nextInt();
	}

	public static int chooseNewSalt() throws NoSuchAlgorithmException {
	    return getSecureRandom();
	}
	
	private static String computeSHA (String preimage) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    md.update(preimage.getBytes("UTF-8"));
	    byte raw[] = md.digest();
	    return (new sun.misc.BASE64Encoder().encode(raw));
	}

	public static String getSaltedHash(String pwd, int salt) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return computeSHA(pwd + "|" + salt);
	}
}


