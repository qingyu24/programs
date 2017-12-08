/**
 * TestSecurity.java 2013-3-18上午10:30:12
 */
package security;

import java.security.*;
import java.security.spec.*;
import java.util.*;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * @author ddoq
 * @version 1.0.0
 *
 */
public class TestSecurity
{
	private static Random m_Random = new Random();
	private static byte[] m_Bytes = null;
	
	public static void main(String[] args) throws Exception
	{
		long stm = System.currentTimeMillis();
		for ( int i = 0; i < 100000; ++i )
		{
//			long stm1 = System.currentTimeMillis();
			Test3ForC();
//			System.out.println("use:" + (System.currentTimeMillis() - stm1) + "[" + i + "]");
		}
		System.out.println("total:" + (System.currentTimeMillis() - stm));
//		synchronized (args)
//		{
//			args.wait();
//		}
	}
	
	/**
	 * SHA
	 */
	public static void Test1() throws NoSuchAlgorithmException
	{
		MessageDigest alga = _GetMessageDigest();
		alga.update(_GetBytes());
		byte[] digesta = alga.digest();
		
		MessageDigest algb = _GetMessageDigest();
		algb.update(_GetBytes());
		if ( MessageDigest.isEqual(digesta, algb.digest()))
		{
			System.out.println("check ok");
		}
		else
		{
			System.out.println("fail");
		}
	}
	
	/**
	 * DSA
	 */
	public static void Test2() throws Exception
	{
		String key = "RSA";
	
		KeyPairGenerator keygen = KeyPairGenerator.getInstance(key);
		
		SecureRandom secrand = new SecureRandom();
		secrand.setSeed(12345678);
		keygen.initialize(1024, secrand);
		
		KeyPair keys = keygen.generateKeyPair();
		PublicKey pubkey = keys.getPublic();
		PrivateKey prikey = keys.getPrivate();
		
		byte[] encrypt= _SimulateClient(prikey.getEncoded(), key);
		
		Cipher cipher  = Cipher.getInstance(key);
		cipher.init(Cipher.DECRYPT_MODE, pubkey);
		byte[] decrypt = cipher.doFinal(encrypt);
		
		System.out.println("result:" + Arrays.equals(_GetBytes(), decrypt));
	}
	
	/**
	 * DES
	 */
	public static void Test3() throws Exception
	{
		String key = "DES";
		KeyGenerator keygen = _GetKeyGenerator(key);
		SecretKey deskey = keygen.generateKey();
		
		byte[] securityMsg = _SimulateClientTest3(deskey.getEncoded(), key);
		
		Cipher c1 = Cipher.getInstance(key);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		byte[] normalMsg = c1.doFinal(securityMsg);
		
		if ( Arrays.equals(normalMsg, _GetBytes()) )
		{
			System.out.println("check ok:" + securityMsg.length + "," + _GetBytes().length);
		}
		else
		{
			System.out.println("fail");
		}
	}
	
	public static void Test3ForC() throws Exception
	{
//		SecureRandom secrand = new SecureRandom();
//		secrand.setSeed(9834234);
//		KeyGenerator gen = KeyGenerator.getInstance("DES");
//		gen.init(secrand);
//		SecretKey key = gen.generateKey();
		byte[] bs = _GenKey();
		byte[] securityMsg = _SimulateClientTest4(bs);
		
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(bs);
		SecretKey sKey = SecretKeyFactory.getInstance("DES").generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(bs);
		cipher.init(Cipher.DECRYPT_MODE, sKey, iv);
		byte[] normalMsg = cipher.doFinal(securityMsg);
		
		if ( Arrays.equals(normalMsg, _GetBytes()) )
		{
			System.out.println("check ok:" + securityMsg.length + "," + _GetBytes().length);
		}
		else
		{
			System.out.println("fail");
		}
	}
	
	private static byte[] _GenKey()
	{
		byte[] bs = new byte[8];
		for ( int i = 0 ; i < bs.length; ++i )
		{
			bs[i] = (byte) i;
		}
		return bs;
	}

	public static KeyGenerator _GetKeyGenerator(String key) throws NoSuchAlgorithmException
	{
		return KeyGenerator.getInstance(key);
	}
	
	public static byte[] _SimulateClientTest3(byte[] bs, String key) throws Exception
	{
		SecretKey deskey = new SecretKeySpec(bs, key);
		Cipher c1 = Cipher.getInstance(key);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		byte[] cipherByte = c1.doFinal(_GetBytes());
		return cipherByte;
	}
	
	public static byte[] _SimulateClientTest4(byte[] bs) throws Exception
	{
		Cipher c1 = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(bs);
		SecretKey secretKey = SecretKeyFactory.getInstance("DES").generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(bs);
		c1.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		byte[] cipherByte = c1.doFinal(_GetBytes());
		return cipherByte;
	}
	
	public static byte[] _SimulateClient(byte[] bs, String key) throws Exception
	{
		KeyFactory f = KeyFactory.getInstance(key);
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bs);
		PrivateKey prikey = f.generatePrivate(privateKeySpec);
		
		Cipher cipher = Cipher.getInstance(key);
		cipher.init(Cipher.ENCRYPT_MODE, prikey);
		
		byte[] result = cipher.doFinal(_GetBytes());
		return result;
	}
	
	public static MessageDigest _GetMessageDigest() throws NoSuchAlgorithmException
	{
		return MessageDigest.getInstance("MD5");
//		return MessageDigest.getInstance("SHA-1");
	}
	
	public static byte[] _GetBytes()
	{
		if ( m_Bytes == null )
		{
			m_Bytes = new byte[2048];
			m_Random.nextBytes(m_Bytes);
//			for ( int i = 0; i < m_Bytes.length; ++i )
//			{
//				m_Bytes[i] = (byte) i;
//			}
		}
		return m_Bytes;
	}
}
