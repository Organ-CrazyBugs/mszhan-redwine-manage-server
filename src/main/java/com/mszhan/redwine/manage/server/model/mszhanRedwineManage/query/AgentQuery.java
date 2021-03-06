package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by god on 2018/4/7.
 */
public class AgentQuery extends PageQuery {

    private String names;

    private String type;

    private String phones;

    private Integer agentId;

    private String createStartDate;

    private String createEndDate;

    private List<String> nameList;

    private List<String> phoneList;


    public String getCreateStartDate() {
        String startDate = StringUtils.trimToNull(this.createStartDate);
        if (StringUtils.isNotBlank(startDate)){
            startDate = String.format("%s 00:00:00", startDate);
        }
        return startDate;
    }

    public String getCreateEndDate() {
        String endDate = StringUtils.trimToNull(this.createEndDate);
        if (StringUtils.isNotBlank(endDate)){
            endDate = String.format("%s 23:59:59", endDate);
        }
        return endDate;
    }
    public void setCreateStartDate(String createStartDate) {
        this.createStartDate = createStartDate;
    }


    public void setCreateEndDate(String createEndDate) {
        this.createEndDate = createEndDate;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public List<String> getPhoneList() {
        return fetchObjList(phones, phoneList);
    }

    public void setPhoneList(List<String> phoneList) {
        this.phoneList = phoneList;
    }

    public String getType() {
        return StringUtils.trimToNull(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public List<String> getNameList() {
        return fetchObjList(names, nameList);
    }

    public void setNameList(List<String> nameList) {
        this.nameList = nameList;
    }

    public List<String> fetchObjList(String obj, List<String> objList) {
        if (!CollectionUtils.isEmpty(objList)){
            return objList;
        }
        obj = StringUtils.trimToNull(obj);
        if (StringUtils.isNotBlank(obj)){
            objList = new ArrayList<>();
            String str[] = obj.split("\\,");
            for (String st : str){
                if (StringUtils.isNotBlank(st)){
                    objList.add(st);
                }
            }
        }
        return objList;
    }


}
