package com.bumingzeyi.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author :BuMing
 * @date : 2022-03-28 21:31
 */
public class SHAUtils {

    /**
     * 字符串SHA加密
     * @param strText 加密字符串
     * @param strType 加密类型
     * @return
     */
    public static String SHA(final String strText, final String strType) {
        // 返回值
        String strResult = null;
        // 是否是有效字符串
        if(strText != null && strText.length() > 0) {
            try {
                //SHA加密开始
                //创建加密对象,并传入入加密类型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                //传入要加密的字符串
                messageDigest.update(strText.getBytes());
                //得到byte类型结果
                byte byteBuffer[] = messageDigest.digest();
                //将byte转换为string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for(int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if(hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    /**
     * 传入文本内容,返回SHA-256串
     * @param strText
     * @return
     */
    public static String SHA256(final String strText) {
        return SHA(strText, "SHA-256");
    }

    /**
     * 传入文本内容,返回SHA-512串
     * @param strText
     * @return
     */
    public static String SHA512(final String strText) {
        return SHA(strText, "SHA-512");
    }

}
