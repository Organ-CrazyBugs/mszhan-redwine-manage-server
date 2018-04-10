package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;

public class FetchAllUserLoginVO extends UserLogin {
    private String agentName;

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }
}
