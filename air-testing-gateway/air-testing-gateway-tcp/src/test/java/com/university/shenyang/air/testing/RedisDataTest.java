package com.university.shenyang.air.testing;

import com.university.shenyang.air.testing.gateway.util.Constants;
import com.university.shenyang.air.testing.model.ReportInfo;
import org.apache.commons.lang.StringUtils;
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
public class RedisDataTest {
    @Autowired
    public RedisTemplate<String, ReportInfo> redisTemplate;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RedisDataTest.class);
//        application.setWebEnvironment(true);
        application.run(args);
    }

    @PostConstruct
    public void initDate() {

        for(int i = 1; i < 151; i++){
            long startTime = strToDateLong("2017-07-24 00:00:00").getTime();
            for(int j = 1; j < 17281; j++){
                // 上报信息封装
                ReportInfo reportInfo = new ReportInfo();
                reportInfo.setCollectTime(new Date(startTime));
                reportInfo.setDeviceCode("deviceCode" + StringUtils.leftPad(String.valueOf(i), 7, "0"));
                reportInfo.setSim("13998184711");
                reportInfo.setPm1_0(950);
                reportInfo.setPm2_5(800);
                reportInfo.setPm10(30);
                reportInfo.setFormaldehyde(1.20F);
                reportInfo.setTemperature(22);
                reportInfo.setHumidity(35);
                reportInfo.setCo(111);
                reportInfo.setCo2(222);
                reportInfo.setNo(333);
                reportInfo.setNo2(444);
                reportInfo.setO3(555);
                reportInfo.setSo2(666);
                reportInfo.setTvoc(777);
                reportInfo.setWindSpeed(888);
                reportInfo.setWindDirection(95);
                reportInfo.setLongitude(121.123456F);
                reportInfo.setLatitude(31.123456F);
                reportInfo.setElectricity(95);

                redisTemplate.opsForZSet().add(Constants.REPORT_REDIS_KEY_PREFIX + reportInfo.getDeviceCode(), reportInfo, reportInfo.getCollectTime().getTime());

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
}
