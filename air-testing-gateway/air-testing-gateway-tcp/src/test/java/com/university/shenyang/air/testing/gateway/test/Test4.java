package com.university.shenyang.air.testing.gateway.test;

import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.test.pojo.UpdateDeviceInfoResult;
import com.university.shenyang.air.testing.gateway.util.Constants;
import com.university.shenyang.air.testing.model.ReportInfo;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/26.
 */
public class Test4 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "xhjyey";
        String strMd5 = md5(str);
        System.out.println(strMd5);
    }

    private static String md5(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(md5Bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
