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
            long startTime = strToDateLong("2018-06-13 22:59:00").getTime();
            for(int j = 1; j < 361; j++){
                // 上报信息封装
                ReportInfo reportInfo = new ReportInfo();
                reportInfo.setCollectTime(new Date(startTime));
                reportInfo.setDeviceCode("deviceCode" + StringUtils.leftPad(String.valueOf(i), 7, "0"));
                reportInfo.setSim("13998184" + StringUtils.leftPad(String.valueOf(i), 3, "0"));
                reportInfo.setPm1_0(950+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setPm2_5(800+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setPm10(30+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setFormaldehyde(1.20F+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setTemperature(22F+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setHumidity(35+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setCo(111+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setCo2(222+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setNo(333+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setNo2(444+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setO3(555+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setSo2(666+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setTvoc(777+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setWindSpeed(888+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setWindDirection(95+(int)(1+Math.random()*(10-1+1)));
                reportInfo.setLongitude(121.123456F);
                reportInfo.setLatitude(31.123456F);
                reportInfo.setElectricity(85+(int)(1+Math.random()*(10-1+1)));

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
