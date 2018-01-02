package com.cai.easyuse.util.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.cai.easyuse.util.LogUtils;

import android.text.TextUtils;

/**
 * AES加解密工具类提供对文件和byte[]的加解密方法.
 * 
 * @author chenmeng
 * 
 */
public final class AES {
	private static final String TAG = "AES";
	private static final int BUFFER_SIZE = 4096;

	private AES() {
	}

	private static Cipher initDecryptCipher(byte[] key)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException {
		SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
		Cipher decryptCipher = Cipher.getInstance("AES");
		decryptCipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return decryptCipher;
	}

	/**
	 * 解密一個文件..
	 * 
	 * @param filePath 待解密的文件路径
	 * @param key 密钥
	 * @param targetFile 解密后的文件存储对象
	 * @return 解密成功返回true，反之false
	 */
	public static boolean decryptFile(String filePath, byte[] key,
									  File targetFile) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		File file = new File(filePath);
		return decryptFile(file, key, targetFile);
	}

	/**
	 * 解密一個文件.
	 * 
	 * @param file 待解密的文件
	 * @param key 密钥
	 * @param targetFile 解密后的文件存储对象
	 * @return 解密成功返回true，反之false
	 */
	public static boolean decryptFile(File file, byte[] key, File targetFile) {
		if (file == null || !file.exists() || key == null || key.length <= 0) {
			return false;
		}
		InputStream is = null;
		OutputStream os = null;
		Cipher cipher;
		try {
			cipher = initDecryptCipher(key);
			is = new FileInputStream(file);
			os = new FileOutputStream(targetFile);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			byte[] buf = new byte[BUFFER_SIZE];
			int len;
			while ((len = is.read(buf)) >= 0) {
				cos.write(buf, 0, len);
			}
			cos.flush();
			cos.close();
			return true;
		} catch (FileNotFoundException e) {
			LogUtils.e(TAG, e.getMessage());
			return false;
		} catch (InvalidKeyException e) {
			LogUtils.e(TAG, e.getMessage());
			return false;
		} catch (NoSuchAlgorithmException e) {
			LogUtils.e(TAG, e.getMessage());
			return false;
		} catch (NoSuchPaddingException e) {
			LogUtils.e(TAG, e.getMessage());
			return false;
		} catch (IOException e) {
			LogUtils.e(TAG, e.getMessage());
			return false;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LogUtils.e(TAG, e.getMessage());
					return false;
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					LogUtils.e(TAG, e.getMessage());
					return false;
				}
			}
		}
	}
}
