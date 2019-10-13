package com.university.shenyang.air.testing.gateway.test;

import com.university.shenyang.air.testing.gateway.common.kit.Convert;
import com.university.shenyang.air.testing.gateway.common.kit.lang.ArraysUtils;
import com.university.shenyang.air.testing.gateway.util.Constants;
import com.university.shenyang.air.testing.model.ReportInfo;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrator on 2016/12/26.
 */
public class Test5 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String sim = new String(Convert.hexStringToBytes("6A62780000000000000000000000000000000000"), "ASCII");
        String sim2 = new String(Convert.hexStringToBytes("7A68757A68753D35363700000000000000000000"), "ASCII");
//        System.out.println(Convert.bytesToHexString(ArraysUtils.fillArrayTail("0000156".getBytes("ASCII"), 17, (byte) 0x00)));
        System.out.println(sim);
        System.out.println(sim2);
    }

    private static void create01Report(String deviceCode, int answerId, java.util.Date deviceTime, int interval) {
        byte[] requestContent = new byte[31];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));

        try {
//            if (deviceCode.getBytes("ASCII").length == 17) {
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + deviceCode);
//                return;
//            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(6, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(deviceTime.getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(deviceTime.getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(deviceTime.getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(deviceTime.getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(deviceTime.getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(deviceTime.getTime()));

        byte[] content = new byte[]{year, month, day, hour, minute, second};
        // content; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, content);

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("终端登入请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[39];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
//            if (deviceCode.getBytes("ASCII").length == 17) {
//                ArraysUtils.arrayappend(responseContent, 4, deviceCode.getBytes("ASCII"));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + deviceCode);
//                return;
//            }
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(14, 2));
        // 终端指令请求时间
        ArraysUtils.arrayappend(responseContent, 24, content);
        // 服务器时间
        ArraysUtils.arrayappend(responseContent, 30, content);
        ArraysUtils.arrayappend(responseContent, 36, Convert.intTobytes(interval, 2));
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("终端登入应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create02Report(ReportInfo reportInfo, int answerId) throws UnsupportedEncodingException {
        byte[] requestContent = new byte[111];

        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(2, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
//            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17) {
//                ArraysUtils.arrayappend(requestContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + reportInfo.getDeviceCode());
//                return;
//            }
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(reportInfo.getDeviceCode().getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(86, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(reportInfo.getCollectTime().getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(reportInfo.getCollectTime().getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(reportInfo.getCollectTime().getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(reportInfo.getCollectTime().getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(reportInfo.getCollectTime().getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(reportInfo.getCollectTime().getTime()));

        byte[] collectTime = new byte[]{year, month, day, hour, minute, second};
        // collectTime; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, collectTime);
        // sim; //sim卡号 20位 20个字节
        ArraysUtils.arrayappend(requestContent, 30, reportInfo.getSim().getBytes("ASCII"));
//        ArraysUtils.arrayappend(requestContent, 30, Convert.longTobytes(reportInfo.getSim(),4));
        // pm1_0; // pm1.0 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 34 + 16, Convert.intTobytes(reportInfo.getPm1_0(), 4));
        // pm2_5; // pm2.5 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 38 + 16, Convert.intTobytes(reportInfo.getPm2_5(), 4));
        // pm10;      // pm10  单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 42 + 16, Convert.intTobytes(reportInfo.getPm10(), 4));
        // formaldehyde; // 甲醛 0.00-9.99 * 100 2个字节
        ArraysUtils.arrayappend(requestContent, 46 + 16, Convert.intTobytes((int) (reportInfo.getFormaldehyde() * 100), 2));
        // temperature; // 温度 0-255 * 10 偏移500 0表示-50摄氏度 2个字节
        ArraysUtils.arrayappend(requestContent, 48 + 16, Convert.intTobytes((int) (reportInfo.getTemperature() * 10), 2));
        // humidity; // 湿度 0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 50 + 16, Convert.intTobytes(reportInfo.getHumidity(), 1));
        // co; // 一氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 51 + 16, Convert.intTobytes(reportInfo.getCo(), 4));
        // co2; // 二氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 55 + 16, Convert.intTobytes(reportInfo.getCo2(), 4));
        // no; // 一氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 59 + 16, Convert.intTobytes(reportInfo.getNo(), 4));
        // no2; // 二氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 63 + 16, Convert.intTobytes(reportInfo.getNo2(), 4));
        // o3; // 臭氧 预留4个字节
        ArraysUtils.arrayappend(requestContent, 67 + 16, Convert.intTobytes(reportInfo.getO3(), 4));
        // so2; // 二氧化硫 预留4个字节
        ArraysUtils.arrayappend(requestContent, 71 + 16, Convert.intTobytes(reportInfo.getSo2(), 4));
        // tvoc; // 有机气态物质 预留4个字节
        ArraysUtils.arrayappend(requestContent, 75 + 16, Convert.intTobytes(reportInfo.getTvoc(), 4));
        // windSpeed; // 风速 预留4个字节
        ArraysUtils.arrayappend(requestContent, 79 + 16, Convert.intTobytes(reportInfo.getWindSpeed(), 4));
        // windDirection; // 风向 预留2个字节
        ArraysUtils.arrayappend(requestContent, 83 + 16, Convert.intTobytes(reportInfo.getWindDirection(), 2));
        // longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 85 + 16, Convert.longTobytes((long) (reportInfo.getLongitude() * 1000000), 4));
        // latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 89 + 16, Convert.longTobytes((long) (reportInfo.getLatitude() * 1000000), 4));
        // electricity; // 太阳能电源电量0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 93 + 16, Convert.intTobytes(reportInfo.getElectricity(), 1));
        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("实时采集上报请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(2, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
//            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17) {
//                ArraysUtils.arrayappend(responseContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + reportInfo.getDeviceCode());
//                return;
//            }
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(reportInfo.getDeviceCode().getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));
        ArraysUtils.arrayappend(responseContent, 24, collectTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("实时采集上报应答报文::" + Convert.bytesToHexString(responseContent));
    }

    private static void create02Report(ReportInfo reportInfo, int answerId, byte[] requestTime) throws UnsupportedEncodingException {
        byte[] requestContent = new byte[111];

        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(2, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
//            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17) {
//                ArraysUtils.arrayappend(requestContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + reportInfo.getDeviceCode());
//                return;
//            }
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(reportInfo.getDeviceCode().getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(86, 2));

//        // 如果是终端校时指令应答内容为系统当前时间
//        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(reportInfo.getCollectTime().getTime()));
//        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(reportInfo.getCollectTime().getTime()));
//        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(reportInfo.getCollectTime().getTime()));
//        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(reportInfo.getCollectTime().getTime()));
//        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(reportInfo.getCollectTime().getTime()));
//        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(reportInfo.getCollectTime().getTime()));
//
//        byte[] collectTime = new byte[]{year, month, day, hour, minute, second};
        // collectTime; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, requestTime);
        // sim; //sim卡号 20位 20个字节
        ArraysUtils.arrayappend(requestContent, 30, reportInfo.getSim().getBytes("ASCII"));
//        ArraysUtils.arrayappend(requestContent, 30, Convert.longTobytes(reportInfo.getSim(),4));
        // pm1_0; // pm1.0 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 34 + 16, Convert.intTobytes(reportInfo.getPm1_0(), 4));
        // pm2_5; // pm2.5 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 38 + 16, Convert.intTobytes(reportInfo.getPm2_5(), 4));
        // pm10;      // pm10  单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 42 + 16, Convert.intTobytes(reportInfo.getPm10(), 4));
        // formaldehyde; // 甲醛 0.00-9.99 * 100 2个字节
        ArraysUtils.arrayappend(requestContent, 46 + 16, Convert.intTobytes((int) (reportInfo.getFormaldehyde() * 100), 2));
        // temperature; // 温度 0-255 * 10 偏移500 0表示-50摄氏度 2个字节
        ArraysUtils.arrayappend(requestContent, 48 + 16, Convert.intTobytes((int) (reportInfo.getTemperature() * 10), 2));
        // humidity; // 湿度 0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 50 + 16, Convert.intTobytes(reportInfo.getHumidity(), 1));
        // co; // 一氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 51 + 16, Convert.intTobytes(reportInfo.getCo(), 4));
        // co2; // 二氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 55 + 16, Convert.intTobytes(reportInfo.getCo2(), 4));
        // no; // 一氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 59 + 16, Convert.intTobytes(reportInfo.getNo(), 4));
        // no2; // 二氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 63 + 16, Convert.intTobytes(reportInfo.getNo2(), 4));
        // o3; // 臭氧 预留4个字节
        ArraysUtils.arrayappend(requestContent, 67 + 16, Convert.intTobytes(reportInfo.getO3(), 4));
        // so2; // 二氧化硫 预留4个字节
        ArraysUtils.arrayappend(requestContent, 71 + 16, Convert.intTobytes(reportInfo.getSo2(), 4));
        // tvoc; // 有机气态物质 预留4个字节
        ArraysUtils.arrayappend(requestContent, 75 + 16, Convert.intTobytes(reportInfo.getTvoc(), 4));
        // windSpeed; // 风速 预留4个字节
        ArraysUtils.arrayappend(requestContent, 79 + 16, Convert.intTobytes(reportInfo.getWindSpeed(), 4));
        // windDirection; // 风向 预留2个字节
        ArraysUtils.arrayappend(requestContent, 83 + 16, Convert.intTobytes(reportInfo.getWindDirection(), 2));
        // longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 85 + 16, Convert.longTobytes((long) (reportInfo.getLongitude() * 1000000), 4));
        // latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 89 + 16, Convert.longTobytes((long) (reportInfo.getLatitude() * 1000000), 4));
        // electricity; // 太阳能电源电量0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 93 + 16, Convert.intTobytes(reportInfo.getElectricity(), 1));
        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("实时采集上报请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(2, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
//            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17) {
//                ArraysUtils.arrayappend(responseContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + reportInfo.getDeviceCode());
//                return;
//            }
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(reportInfo.getDeviceCode().getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));
        ArraysUtils.arrayappend(responseContent, 24, requestTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("实时采集上报应答报文::" + Convert.bytesToHexString(responseContent));
    }

    private static void create03Report(ReportInfo reportInfo, int answerId) throws UnsupportedEncodingException {
        byte[] requestContent = new byte[111];

        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(3, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
//            if (reportInfo.getDeviceCode().getBytes("ASCII").length == 17) {
//                ArraysUtils.arrayappend(requestContent, 4, reportInfo.getDeviceCode().getBytes("ASCII"));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + reportInfo.getDeviceCode());
//                return;
//            }
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(reportInfo.getDeviceCode().getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(86, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(reportInfo.getCollectTime().getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(reportInfo.getCollectTime().getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(reportInfo.getCollectTime().getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(reportInfo.getCollectTime().getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(reportInfo.getCollectTime().getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(reportInfo.getCollectTime().getTime()));

        byte[] collectTime = new byte[]{year, month, day, hour, minute, second};
        // collectTime; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, collectTime);
        // sim; //sim卡号 20位 20个字节
        ArraysUtils.arrayappend(requestContent, 30, reportInfo.getSim().getBytes("ASCII"));
//        ArraysUtils.arrayappend(requestContent, 30, Convert.longTobytes(reportInfo.getSim(),4));
        // pm1_0; // pm1.0 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 34 + 16, Convert.intTobytes(reportInfo.getPm1_0(), 4));
        // pm2_5; // pm2.5 单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 38 + 16, Convert.intTobytes(reportInfo.getPm2_5(), 4));
        // pm10;      // pm10  单位:微克/立方米 整数 4个字节
        ArraysUtils.arrayappend(requestContent, 42 + 16, Convert.intTobytes(reportInfo.getPm10(), 4));
        // formaldehyde; // 甲醛 0.00-9.99 * 100 2个字节
        ArraysUtils.arrayappend(requestContent, 46 + 16, Convert.intTobytes((int) (reportInfo.getFormaldehyde() * 100), 2));
        // temperature; // 温度 0-255 * 10 偏移500 0表示-50摄氏度 2个字节
        ArraysUtils.arrayappend(requestContent, 48 + 16, Convert.intTobytes((int) (reportInfo.getTemperature() * 10), 2));
        // humidity; // 湿度 0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 50 + 16, Convert.intTobytes(reportInfo.getHumidity(), 1));
        // co; // 一氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 51 + 16, Convert.intTobytes(reportInfo.getCo(), 4));
        // co2; // 二氧化碳 预留4个字节
        ArraysUtils.arrayappend(requestContent, 55 + 16, Convert.intTobytes(reportInfo.getCo2(), 4));
        // no; // 一氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 59 + 16, Convert.intTobytes(reportInfo.getNo(), 4));
        // no2; // 二氧化氮 预留4个字节
        ArraysUtils.arrayappend(requestContent, 63 + 16, Convert.intTobytes(reportInfo.getNo2(), 4));
        // o3; // 臭氧 预留4个字节
        ArraysUtils.arrayappend(requestContent, 67 + 16, Convert.intTobytes(reportInfo.getO3(), 4));
        // so2; // 二氧化硫 预留4个字节
        ArraysUtils.arrayappend(requestContent, 71 + 16, Convert.intTobytes(reportInfo.getSo2(), 4));
        // tvoc; // 有机气态物质 预留4个字节
        ArraysUtils.arrayappend(requestContent, 75 + 16, Convert.intTobytes(reportInfo.getTvoc(), 4));
        // windSpeed; // 风速 预留4个字节
        ArraysUtils.arrayappend(requestContent, 79 + 16, Convert.intTobytes(reportInfo.getWindSpeed(), 4));
        // windDirection; // 风向 预留2个字节
        ArraysUtils.arrayappend(requestContent, 83 + 16, Convert.intTobytes(reportInfo.getWindDirection(), 2));
        // longitude; // 经度 以度为单位的经度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 85 + 16, Convert.longTobytes((long) (reportInfo.getLongitude() * 1000000), 4));
        // latitude; // 纬度 以度为单位的纬度值乘以10的6次方，精确到百万分之一度。 4个字节
        ArraysUtils.arrayappend(requestContent, 89 + 16, Convert.longTobytes((long) (reportInfo.getLatitude() * 1000000), 4));
        // electricity; // 太阳能电源电量0-100 1个字节
        ArraysUtils.arrayappend(requestContent, 93 + 16, Convert.intTobytes(reportInfo.getElectricity(), 1));
        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("补传采集上报请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(3, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(reportInfo.getDeviceCode().getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));
        ArraysUtils.arrayappend(responseContent, 24, collectTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("补传采集上报应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create08Report(String deviceCode, int answerId, java.util.Date deviceTime) {
        byte[] requestContent = new byte[31];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(8, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
//            if (deviceCode.getBytes("ASCII").length == 17) {
//                ArraysUtils.arrayappend(requestContent, 4, deviceCode.getBytes("ASCII"));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + deviceCode);
//                return;
//            }
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(6, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(deviceTime.getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(deviceTime.getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(deviceTime.getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(deviceTime.getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(deviceTime.getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(deviceTime.getTime()));

        byte[] content = new byte[]{year, month, day, hour, minute, second};
        // content; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, content);

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("终端校时请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(8, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));
        // 如果是终端校时指令应答内容为系统当前时间
        byte serverYear = Byte.valueOf(new SimpleDateFormat("yy").format(new java.util.Date().getTime()));
        byte serverMonth = Byte.valueOf(new SimpleDateFormat("MM").format(new java.util.Date().getTime()));
        byte serverDay = Byte.valueOf(new SimpleDateFormat("dd").format(new java.util.Date().getTime()));
        byte serverHour = Byte.valueOf(new SimpleDateFormat("HH").format(new java.util.Date().getTime()));
        byte serverMinute = Byte.valueOf(new SimpleDateFormat("mm").format(new java.util.Date().getTime()));
        byte serverSecond = Byte.valueOf(new SimpleDateFormat("ss").format(new java.util.Date().getTime()));

        byte[] serverTime = new byte[]{serverYear, serverMonth, serverDay, serverHour, serverMinute, serverSecond};
        ArraysUtils.arrayappend(responseContent, 24, serverTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("终端校时应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create08Report(String deviceCode, int answerId, byte[] deviceTime) {
        byte[] requestContent = new byte[31];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(8, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
//            if (deviceCode.getBytes("ASCII").length == 17) {
//                ArraysUtils.arrayappend(requestContent, 4, deviceCode.getBytes("ASCII"));
//            } else {
//                System.out.println("设备code码错误,deviceCode:" + deviceCode);
//                return;
//            }
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(6, 2));

        ArraysUtils.arrayappend(requestContent, 24, deviceTime);

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("终端校时请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(8, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));
        // 如果是终端校时指令应答内容为系统当前时间
        byte serverYear = Byte.valueOf(new SimpleDateFormat("yy").format(new java.util.Date().getTime()));
        byte serverMonth = Byte.valueOf(new SimpleDateFormat("MM").format(new java.util.Date().getTime()));
        byte serverDay = Byte.valueOf(new SimpleDateFormat("dd").format(new java.util.Date().getTime()));
        byte serverHour = Byte.valueOf(new SimpleDateFormat("HH").format(new java.util.Date().getTime()));
        byte serverMinute = Byte.valueOf(new SimpleDateFormat("mm").format(new java.util.Date().getTime()));
        byte serverSecond = Byte.valueOf(new SimpleDateFormat("ss").format(new java.util.Date().getTime()));

        byte[] serverTime = new byte[]{serverYear, serverMonth, serverDay, serverHour, serverMinute, serverSecond};
        ArraysUtils.arrayappend(responseContent, 24, serverTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("终端校时应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create80Report(String deviceCode, String account, String password, int answerId, java.util.Date requestTime) {
        byte[] requestContent = new byte[71];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(0x80, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(46, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(requestTime.getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(requestTime.getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(requestTime.getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(requestTime.getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(requestTime.getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(requestTime.getTime()));

        byte[] content = new byte[]{year, month, day, hour, minute, second};
        // content; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, content);
        try {
            ArraysUtils.arrayappend(requestContent, 30, ArraysUtils.fillArrayTail(account.getBytes("ASCII"), 20, (byte) 0x00));
            ArraysUtils.arrayappend(requestContent, 50, ArraysUtils.fillArrayTail(password.getBytes("ASCII"), 20, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("Wifi设置请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(0x80, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));

        ArraysUtils.arrayappend(responseContent, 24, content);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("Wifi设置应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create80Report(String deviceCode, String account, String password, int answerId, byte[] requestTime) {
        byte[] requestContent = new byte[71];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(0x80, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(46, 2));

        ArraysUtils.arrayappend(requestContent, 24, requestTime);
        try {
            ArraysUtils.arrayappend(requestContent, 30, ArraysUtils.fillArrayTail(account.getBytes("ASCII"), 20, (byte) 0x00));
            ArraysUtils.arrayappend(requestContent, 50, ArraysUtils.fillArrayTail(password.getBytes("ASCII"), 20, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("Wifi设置请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(0x80, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));

        ArraysUtils.arrayappend(responseContent, 24, requestTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("Wifi设置应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create81Report(String deviceCode, int interval, int answerId, java.util.Date requestTime) {
        byte[] requestContent = new byte[33];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(0x81, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(8, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(requestTime.getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(requestTime.getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(requestTime.getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(requestTime.getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(requestTime.getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(requestTime.getTime()));

        byte[] content = new byte[]{year, month, day, hour, minute, second};
        // content; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, content);
        ArraysUtils.arrayappend(requestContent, 30, Convert.intTobytes(interval, 2));

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("设备采集间隔设置请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(0x81, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));

        ArraysUtils.arrayappend(responseContent, 24, content);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("设备采集间隔设置应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create81Report(String deviceCode, int interval, int answerId, byte[] requestTime) {
        byte[] requestContent = new byte[33];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(0x81, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(8, 2));

        ArraysUtils.arrayappend(requestContent, 24, requestTime);
        ArraysUtils.arrayappend(requestContent, 30, Convert.intTobytes(interval, 2));

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("设备采集间隔设置请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[31];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(0x81, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(6, 2));

        ArraysUtils.arrayappend(responseContent, 24, requestTime);
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("设备采集间隔设置应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create8FReport(String deviceCode, int answerId, String wifiAccount, String wifiPassword, int interval, java.util.Date requestTime) {
        byte[] requestContent = new byte[31];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(0x8f, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(6, 2));

        // 如果是终端校时指令应答内容为系统当前时间
        byte year = Byte.valueOf(new SimpleDateFormat("yy").format(requestTime.getTime()));
        byte month = Byte.valueOf(new SimpleDateFormat("MM").format(requestTime.getTime()));
        byte day = Byte.valueOf(new SimpleDateFormat("dd").format(requestTime.getTime()));
        byte hour = Byte.valueOf(new SimpleDateFormat("HH").format(requestTime.getTime()));
        byte minute = Byte.valueOf(new SimpleDateFormat("mm").format(requestTime.getTime()));
        byte second = Byte.valueOf(new SimpleDateFormat("ss").format(requestTime.getTime()));

        byte[] queryTime = new byte[]{year, month, day, hour, minute, second};
        // content; // 采集时间 6个字节
        ArraysUtils.arrayappend(requestContent, 24, queryTime);

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("设备参数查询请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[73];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(0x8f, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(48, 2));

        ArraysUtils.arrayappend(responseContent, 24, queryTime);
        try {
            ArraysUtils.arrayappend(requestContent, 30, ArraysUtils.fillArrayTail(wifiAccount.getBytes("ASCII"), 20, (byte) 0x00));
            ArraysUtils.arrayappend(requestContent, 50, ArraysUtils.fillArrayTail(wifiPassword.getBytes("ASCII"), 20, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 70, Convert.intTobytes(interval, 2));
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("设备参数查询应答报文:" + Convert.bytesToHexString(responseContent));
    }

    private static void create8FReport(String deviceCode, int answerId, String wifiAccount, String wifiPassword, int interval, byte[] queryTime) {
        byte[] requestContent = new byte[31];
        ArraysUtils.arrayappend(requestContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(requestContent, 2, Convert.intTobytes(0x8f, 1));
        ArraysUtils.arrayappend(requestContent, 3, Convert.intTobytes(254, 1));
        try {
            ArraysUtils.arrayappend(requestContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(requestContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(requestContent, 22, Convert.intTobytes(6, 2));

        ArraysUtils.arrayappend(requestContent, 24, queryTime);

        byte crc = Convert.checkPackage(requestContent, 2, requestContent.length - 2);
        ArraysUtils.arrayappend(requestContent, requestContent.length - 1, new byte[]{crc});
        System.out.println("设备参数查询请求报文:" + Convert.bytesToHexString(requestContent));

        byte[] responseContent = new byte[73];

        ArraysUtils.arrayappend(responseContent, 0, Convert.intTobytes(Constants.HEAD, 2));
        ArraysUtils.arrayappend(responseContent, 2, Convert.intTobytes(0x8f, 1));
        ArraysUtils.arrayappend(responseContent, 3, Convert.intTobytes(answerId, 1));
        try {
            ArraysUtils.arrayappend(responseContent, 4, ArraysUtils.fillArrayTail(deviceCode.getBytes("ASCII"), 17, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 21, Convert.intTobytes(1, 1));
        ArraysUtils.arrayappend(responseContent, 22, Convert.intTobytes(48, 2));

        ArraysUtils.arrayappend(responseContent, 24, queryTime);
        try {
            ArraysUtils.arrayappend(responseContent, 30, ArraysUtils.fillArrayTail(wifiAccount.getBytes("ASCII"), 20, (byte) 0x00));
            ArraysUtils.arrayappend(responseContent, 50, ArraysUtils.fillArrayTail(wifiPassword.getBytes("ASCII"), 20, (byte) 0x00));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArraysUtils.arrayappend(responseContent, 70, Convert.intTobytes(interval, 2));
        crc = Convert.checkPackage(responseContent, 2, responseContent.length - 2);
        ArraysUtils.arrayappend(responseContent, responseContent.length - 1, new byte[]{crc});
        System.out.println("设备参数查询应答报文:" + Convert.bytesToHexString(responseContent));
    }
}
