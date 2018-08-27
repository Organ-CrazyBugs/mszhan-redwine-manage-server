package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.core.AbstractService;
import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ClientMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Client;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ClientQuery;
import com.mszhan.redwine.manage.server.service.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by god on 2018/8/15.
 */
@Service
public class ClientServiceImpl extends AbstractService<Client> implements ClientService {

    @Autowired
    private ClientMapper clientMapper;


    @Override
    public PaginateResult<Client> queryForPage(ClientQuery query) {
        Integer count = clientMapper.queryCount(query);
        if (count == null || count.equals(0)){
            return PaginateResult.newInstance(0, new ArrayList<>());
        }
        List<Client> list = clientMapper.queryForPage(query);
        return PaginateResult.newInstance(Long.valueOf(count), list);
    }

    @Override
    public void deleteClient(String ids) {
        if (StringUtils.isBlank(ids)){
            throw BasicException.newInstance().error("id不能为空", 500);
        }
        String[] str = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String id : str){
            idList.add(Integer.parseInt(id));
        }
        clientMapper.deleteClients(idList);
    }

    @Override
    public void updateClient(Client client) {
        if (client.getId() == null){
            throw BasicException.newInstance().error("id不能为空", 500);
        }
        if (StringUtils.isBlank(client.getName())){
            throw BasicException.newInstance().error("客户名不能为空", 500);
        }
        if (StringUtils.isBlank(client.getPhoneNumber())){
            throw BasicException.newInstance().error("客户电话不能为空", 500);
        }
        client.setName(StringUtils.trimToNull(client.getName()));
        client.setPhoneNumber(StringUtils.trimToNull(client.getPhoneNumber()));
        List<Client> checkNameList = clientMapper.checkName(client.getId(), client.getName());
        if (!CollectionUtils.isEmpty(checkNameList)){
            throw BasicException.newInstance().error("已经存在该客户名称", 500);
        }
        List<Client> checkPhoneList = clientMapper.checkPhone(client.getId(), client.getPhoneNumber());
        if (!CollectionUtils.isEmpty(checkPhoneList)){
            throw BasicException.newInstance().error("已经存在该电话号码", 500);
        }
        clientMapper.updateByPrimaryKey(client);
    }

    @Override
    public void saveClient(Client client) {
        if (StringUtils.isBlank(client.getName())){
            throw BasicException.newInstance().error("id不能为空", 500);
        }
        if (StringUtils.isBlank(client.getAddress())){
            throw BasicException.newInstance().error("联系地址不能为空", 500);
        }
        if (StringUtils.isBlank(client.getPhoneNumber())){
            throw BasicException.newInstance().error("电话号码不能为空", 500);
        }
        client.setPhoneNumber(StringUtils.trimToNull(client.getPhoneNumber()));
        client.setName(StringUtils.trimToNull(client.getName()));
        client.setAddress(StringUtils.trimToNull(client.getAddress()));
        List<Client> checkNameList = clientMapper.checkName(null, client.getName());
        if (!CollectionUtils.isEmpty(checkNameList)){
            throw BasicException.newInstance().error("已经存在该客户名称", 500);
        }
        List<Client> checkPhoneList = clientMapper.checkPhone(null, client.getPhoneNumber());
        if (!CollectionUtils.isEmpty(checkPhoneList)){
            throw BasicException.newInstance().error("已经存在该电话号码", 500);
        }
        clientMapper.insert(client);
    }
}
