package com.university.shenyang.air.testing.app.service;

import com.university.shenyang.air.testing.app.command.AddCUserCommand;
import com.university.shenyang.air.testing.model.CUserInfo;
import com.university.shenyang.air.testing.model.DeviceInfo;

/**
 * Created by chenjc on 2019/6/30.
 */
public interface CUserInfoService {
    int insertCUserInfo(AddCUserCommand record);

    CUserInfo queryByUsername(String username);

    int deleteByPrimaryKey(Long id);

    int updatePassword(Long id, String password);
}
