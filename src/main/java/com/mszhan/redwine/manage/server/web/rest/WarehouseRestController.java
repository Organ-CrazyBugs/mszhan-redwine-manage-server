package com.mszhan.redwine.manage.server.web.rest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.WarehouseMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Warehouse;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 10:53 2018/4/7
 */
@RestController
public class WarehouseRestController {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @GetMapping(value = "/api/warehouse/manage/list")
    public Object list(HttpServletRequest request) {
        Requests requests = Requests.newInstance(request);

        String warehouseName = requests.getString("warehouseName", null);
        String status = requests.getString("status", null);
        Integer offset = requests.getInteger("offset", 0);
        Integer limit = requests.getInteger("limit", 10);

        Condition searchCon = new Condition(Warehouse.class);
        Example.Criteria searchCriteria = searchCon.createCriteria();
        if (StringUtils.isNotBlank(warehouseName)) {
            searchCriteria.andLike("name", String.format("%%%s%%", warehouseName));
        }
        if (StringUtils.isNotBlank(status)) {
            searchCriteria.andEqualTo("status", status);
        }

        Page<Warehouse> page = PageHelper.offsetPage(offset, limit).doSelectPage(() -> this.warehouseMapper.selectByCondition(searchCon));

        return Responses.newInstance().succeed(PaginateResult.newInstance(page.getTotal(), page));
    }

}
