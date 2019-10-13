package com.university.shenyang.air.testing.app.service.impl;

import com.university.shenyang.air.testing.app.cache.DevicesManager;
import com.university.shenyang.air.testing.app.command.UserAddDeviceCommand;
import com.university.shenyang.air.testing.app.pojo.UserDeviceInfo;
import com.university.shenyang.air.testing.app.redis.RedisClient;
import com.university.shenyang.air.testing.app.service.CUserDeviceMappingService;
import com.university.shenyang.air.testing.app.util.Constants;
import com.university.shenyang.air.testing.common.pojo.UserInfo;
import com.university.shenyang.air.testing.mapper.CUserDeviceMappingMapper;
import com.university.shenyang.air.testing.model.CUserDeviceMapping;
import com.university.shenyang.air.testing.model.DeviceInfo;
import com.university.shenyang.air.testing.model.ReportInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by cjcqqqq on 2017/5/16.
 */
@Service("cUserDeviceMappingService")
public class CUserDeviceMappingServiceImpl implements CUserDeviceMappingService {
    @Autowired
    private CUserDeviceMappingMapper cUserDeviceMappingMapper;

    @Autowired
    private RedisClient redisClient;

    // 设备在线状态判断有效时间范围单位分钟
    @Value("${device.online.effective.time:30}")
    private int effectiveTime;

    @Override
    public int userAddDevice(String userId, String deviceCode, String deviceDesc) {
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("userId", userId);
        params.put("deviceCode", deviceCode);
        params.put("deviceDesc", deviceDesc);

        cUserDeviceMappingMapper.userAddDevice(params);
        return (int) params.get("result");
    }

    @Override
    public List<UserDeviceInfo> getUserDeviceList(String userId) {
        List<UserDeviceInfo> userDeviceInfos = new ArrayList<>();
        List<CUserDeviceMapping> mappings = cUserDeviceMappingMapper.getUserDeviceList(userId);
        for (CUserDeviceMapping row : mappings) {
            UserDeviceInfo userDeviceInfo = new UserDeviceInfo();
            BeanUtils.copyProperties(row, userDeviceInfo);

            boolean isOnline = false;
            String deviceCode = DevicesManager.getInstance().getDeviceCodeById(row.getDeviceId());
            if(deviceCode != null){
                ReportInfo reportInfo = redisClient.get(Constants.LATEST_REPORT_REDIS_KEY_PREFIX + deviceCode);
                if(reportInfo != null && (Math.abs((new Date()).getTime() - reportInfo.getCollectTime().getTime()) < effectiveTime * 1000)){
                    isOnline = true;
                }
            }

            userDeviceInfo.setIsOnline(isOnline?1:0);
            userDeviceInfos.add(userDeviceInfo);
        }

        return userDeviceInfos;
    }

    @Override
    public int setDefault(String userId, String deviceId) {
        return cUserDeviceMappingMapper.setDefault(userId,deviceId);
    }

    @Override
    public int deleteByUserIdAndMappingId(String userId, String mappingId) {
        return cUserDeviceMappingMapper.deleteByUserIdAndMappingId(userId,mappingId);
    }
}
