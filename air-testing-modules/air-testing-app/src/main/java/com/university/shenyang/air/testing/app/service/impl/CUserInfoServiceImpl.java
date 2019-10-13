package com.university.shenyang.air.testing.app.service.impl;

import com.university.shenyang.air.testing.app.command.AddCUserCommand;
import com.university.shenyang.air.testing.app.command.AddDeviceCommand;
import com.university.shenyang.air.testing.app.service.CUserInfoService;
import com.university.shenyang.air.testing.app.service.DeviceInfoService;
import com.university.shenyang.air.testing.mapper.CUserInfoMapper;
import com.university.shenyang.air.testing.mapper.DeviceInfoMapper;
import com.university.shenyang.air.testing.model.CUserInfo;
import com.university.shenyang.air.testing.model.DeviceInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by cjcqqqq on 2017/5/16.
 */
@Service("cUserInfoService")
public class CUserInfoServiceImpl implements CUserInfoService {
    @Autowired
    private CUserInfoMapper cUserInfoMapper;

    @Override
    public int insertCUserInfo(AddCUserCommand addCUserCommand) {
        CUserInfo record = new CUserInfo();
        record.setUsername(addCUserCommand.getUsername());
        record.setPassword(addCUserCommand.getPassword());
        record.setCreatetime(new Date());

        return cUserInfoMapper.insert(record);
    }

    @Override
    public CUserInfo queryByUsername(String username) {
        return cUserInfoMapper.selectByUsername(username);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return cUserInfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updatePassword(Long id, String password) {
        return cUserInfoMapper.updatePassword(id, password);
    }
}
