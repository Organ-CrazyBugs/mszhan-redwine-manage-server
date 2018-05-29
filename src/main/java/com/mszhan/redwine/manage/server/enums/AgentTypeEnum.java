package com.mszhan.redwine.manage.server.enums;

public enum AgentTypeEnum {

    ADMIN("超级管理员"),
    GENERAL_AGENT("总代理"),
    AGENT("代理");

    private String value;

    AgentTypeEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
