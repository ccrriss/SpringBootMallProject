package com.chrisjia.mall.util;

import com.chrisjia.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
    public static String getMD5Str(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] byteResult = md5.digest((str+ Constant.SALT).getBytes());
        String result = Base64.encodeBase64String(byteResult);
        return result;
    }
}
