package com.university.shenyang.air.testing.gateway.util;


public class Constants {
    public final static String PROTOCOL_GB = "GB";
    public final static int HEAD = 0x2323;
    public static final int JT_MAX_LEN = 1023;
    public static byte[] JT_ESCAPE =new byte[] { 0x7e, 0x7d};
    public static byte[][] JT_TO_ESCAPE = new byte[][] { { 0x7D, 0x02 }, { 0x7D, 0x01 }};
    public static  String PROTOCOL = "protocol";

    public static class JT_REGISTER_RES {
        /** 成功 */
        public static final byte SUCCESS = 0x00;
        /** 设备已被注册 */
        public static final byte VEHICLE_ALREADY_REGISTER = 0x01;
        /** 数据库中无该设备 */
        public static final byte VEHICLE_NOT_IN_DB = 0x02;
        /** 终端已被注册 */
        public static final byte TERMINAL_ALREADY_REGISTER = 0x03;
        /** 数据库中无该终端 */
        public static final byte TERMINAL_NOT_IN_DB = 0x04;
    }


    public static class PLATFORM_COMMON_RES {
        /** 成功/确认 */
        public static final byte SUCCESS = 0x00;
        /** 失败 */
        public static final byte FAILURE = 0x01;
        /** 消息错误 */
        public static final byte MSG_ERROR = 0x02;
        /** 不支持 */
        public static final byte NOT_SUPPORT = 0x03;
        /** 报警处理 */
        public static final byte ALARM_PROCESS = 0x04;
    }
}
