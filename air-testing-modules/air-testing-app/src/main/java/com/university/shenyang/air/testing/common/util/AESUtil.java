package com.university.shenyang.air.testing.common.util;
 
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
 
/**
 * 实现AES加解密
 *
 * @author: xuzongxin
 * @date: 2018/8/21 15:12
 * @description:
 */
public class AESUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESUtil.class);
 
    private static final String KEY_ALGORITHM = "AES";
    private static final String CHAR_SET = "UTF-8";
    /**
     * AES的密钥长度
     */
    private static final Integer SECRET_KEY_LENGTH = 128;
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
 
    /**
     * AES加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(password)) {
            LOGGER.error("AES encryption params is null");
            return null;
        }
        try {
            //创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes(CHAR_SET);
            //初始化为加密密码器
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
            byte[] encryptByte = cipher.doFinal(byteContent);
            return parseByte2HexStr(encryptByte);
//            return Base64.encodeBase64String(encryptByte);
        } catch (Exception e) {
            LOGGER.error("AES encryption operation has exception,content:{},password:{}", content, password, e);
        }
        return null;
    }
 
    /**
     * AES解密操作
     *
     * @param encryptContent 加密的密文
     * @param password       解密的密钥
     * @return
     */
    public static String decrypt(String encryptContent, String password) {
        if (StringUtils.isEmpty(encryptContent) || StringUtils.isEmpty(password)) {
            LOGGER.error("AES decryption params is null");
            return null;
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行解密操作
//            byte[] result = cipher.doFinal(Base64.decodeBase64(encryptContent));
            byte[] result = cipher.doFinal(parseHexStr2Byte(encryptContent));
            return new String(result, CHAR_SET);
        } catch (Exception e) {
            LOGGER.error("AES decryption operation has exception,content:{},password:{}", encryptContent, password, e);
        }
        return null;
    }
 
    private static SecretKeySpec getSecretKey(final String password) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(password.getBytes());
        //生成指定算法密钥的生成器
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
//        keyGenerator.init(SECRET_KEY_LENGTH, new SecureRandom(password.getBytes()));
        keyGenerator.init(SECRET_KEY_LENGTH, secureRandom);
        //生成密钥
        SecretKey secretKey = keyGenerator.generateKey();
        //转换成AES的密钥
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
    }

    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
//        String str = "deviceCode0000083";
//        System.out.println("str:" + str);
//
//        String encryptStr = encrypt(str, "aa7889d3-435b-4ed2-99f9-08035661eda9");
//        System.out.println("encrypt:" + encryptStr);
//
//        String decryptStr = decrypt(encryptStr, "aa7889d3-435b-4ed2-99f9-08035661eda9");
//
//        System.out.println("decryptStr:" + decryptStr);

        String decryptStr = decrypt("912F03D5278DF4402677A3AAF44592896A2ACED496859E9EE9661DBE5A8FB974", "aa7889d3-435b-4ed2-99f9-08035661eda9");

        System.out.println("decryptStr:" + decryptStr);
    }
 
}