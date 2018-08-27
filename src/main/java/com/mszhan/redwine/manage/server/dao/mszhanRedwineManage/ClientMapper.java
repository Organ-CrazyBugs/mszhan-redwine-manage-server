package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Client;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ClientQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientMapper extends Mapper<Client> {

    Integer queryCount(ClientQuery query);

    List<Client> queryForPage(ClientQuery query);

    void deleteClients(List<Integer> idList);

    List<Client> checkName(@Param("id") Integer id, @Param("name") String name);

    List<Client> checkPhone(@Param("id") Integer id, @Param("phone") String phone);

}