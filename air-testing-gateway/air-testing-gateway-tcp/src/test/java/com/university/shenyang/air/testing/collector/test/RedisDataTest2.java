package com.university.shenyang.air.testing.collector.test;

import com.university.shenyang.air.testing.collector.test.pojo.ReportInfo;
import com.university.shenyang.air.testing.collector.util.Constants;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

/**
 * @author chenjc
 * @version 1.0
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class RedisDataTest2 {
    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RedisDataTest2.class);
//        application.setWebEnvironment(true);
        application.run(args);
    }

    @PostConstruct
    public void initDate() {

        for(int i = 1; i < 11; i++){
            long startTime = strToDateLong("2017-06-25 21:33:00").getTime();
            for(int j = 1; j < 8641; j++){
                // 上报信息封装
                ReportInfo reportInfo = new ReportInfo();
                reportInfo.setCollectTime(getStringDate(startTime));
                reportInfo.setDeviceCode(String.valueOf(1999000 + i));
                reportInfo.setSim("13998184711");
                reportInfo.setPm1_0(950+ j);
                reportInfo.setPm2_5(800+ j);
                reportInfo.setPm10(30+ j);
                reportInfo.setFormaldehyde(1.20F+ j);
                reportInfo.setTemperature(22+ j);
                reportInfo.setHumidity(35+ j);
                reportInfo.setCo(111+ j);
                reportInfo.setCo2(222+ j);
                reportInfo.setNo(333+ j);
                reportInfo.setNo2(444+ j);
                reportInfo.setO3(555+ j);
                reportInfo.setSo2(666);
                reportInfo.setTvoc(777+ j);
                reportInfo.setWindSpeed(888+ j);
                reportInfo.setWindDirection(95+ j);
                reportInfo.setLongitude(121.123456F+ j);
                reportInfo.setLatitude(31.123456F+ j);
                reportInfo.setElectricity(95+ j);

                JSONObject object = JSONObject.fromObject(reportInfo);
                String json = object.toString();
//                System.out.println(json);

                redisTemplate.opsForZSet().add(Constants.REPORT_REDIS_KEY_PREFIX + reportInfo.getDeviceCode(), json, startTime);

                startTime += 10000;
            }


        }

    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static java.util.Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        java.util.Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(long startTime) {
        Date currentTime = new Date(startTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}
