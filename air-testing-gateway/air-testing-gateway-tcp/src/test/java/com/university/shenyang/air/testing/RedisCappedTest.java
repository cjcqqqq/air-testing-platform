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
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chenjc
 * @version 1.0
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class RedisCappedTest {
    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RedisCappedTest.class);
//        application.setWebEnvironment(true);
        application.run(args);
    }

    @PostConstruct
    public void initDate() {

//        for(int i = 251; i < 501; i++){
//            redisTemplate.opsForZSet().add("test", ""+i, i);
//        }

//        redisTemplate.opsForZSet().removeRange("test",0,-151);

        Set<String> set = redisTemplate.opsForZSet().reverseRange("test", 0, 10);
        List<String> result = new ArrayList<String>();
        if(set != null && set.size() > 0){
            Iterator<String> it = set.iterator();
            while(it.hasNext()){
                result.add(it.next());
            }
        }
        Collections.reverse(result);

        for(String var : result){
            System.out.println(var);
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
