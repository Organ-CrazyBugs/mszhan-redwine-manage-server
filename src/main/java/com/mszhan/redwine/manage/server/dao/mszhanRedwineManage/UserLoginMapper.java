package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.FetchAllUserLoginVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserLoginMapper extends Mapper<UserLogin> {

    List<FetchAllUserLoginVO> fetchAllUserLogin(
        @Param("agentId") Integer agentId,
        @Param("status") String status,
        @Param("personName") String personName,
        @Param("userName") String userName
    );

}