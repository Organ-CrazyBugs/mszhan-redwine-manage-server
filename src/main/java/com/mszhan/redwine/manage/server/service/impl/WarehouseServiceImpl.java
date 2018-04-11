package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.WarehouseMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Warehouse;
import com.mszhan.redwine.manage.server.service.WarehouseService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
@Service
@Transactional
public class WarehouseServiceImpl extends AbstractService<Warehouse> implements WarehouseService {
    @Resource
    private WarehouseMapper warehouseMapper;

    @Override
    public Warehouse createWarehouse(Integer userId, Warehouse warehouse) {
        Assert.notNull(warehouse, "缺少仓库信息参数");
        Assert.notNull(userId, "仓库创建人不能为空");
        Assert.hasLength(warehouse.getName(), "仓库名称不能为空");
        Assert.hasLength(warehouse.getPrincipal(), "仓库负责人不能为空");
        Assert.hasLength(warehouse.getPhone(), "联系方式不能为空");

        Condition checkNameCon = new Condition(Warehouse.class);
        checkNameCon.createCriteria().andEqualTo("name", warehouse.getName());
        int nameExists = this.warehouseMapper.selectCountByCondition(checkNameCon);
        Assert.isTrue(nameExists == 0, "该仓库名称已经存在");

        warehouse.setCreator(userId);
        warehouse.setUpdator(userId);
        warehouse.setCreateDate(new Date());
        warehouse.setUpdateDate(new Date());
        this.warehouseMapper.insert(warehouse);
        return warehouse;
    }

    @Override
    public void updateWarehouse(Integer userId, Warehouse warehouse){
        Assert.notNull(userId, "仓库状态修改人不能为空");
        Assert.notNull(warehouse, "缺少仓库信息参数");
        Assert.hasLength(warehouse.getName(), "仓库名称不能为空");
        Assert.hasLength(warehouse.getPrincipal(), "仓库负责人不能为空");
        Assert.hasLength(warehouse.getPhone(), "联系方式不能为空");

        Condition con = new Condition(Warehouse.class);
        con.createCriteria().andEqualTo("name", warehouse.getName())
                .andNotEqualTo("id", warehouse.getId());
        int count = this.warehouseMapper.selectCountByCondition(con);
        Assert.isTrue(count == 0, "该仓库名称已经存在");

        warehouse.setUpdateDate(new Date());
        warehouse.setUpdator(userId);

        this.warehouseMapper.updateByPrimaryKeySelective(warehouse);
    }

    @Override
    public void changeStatus(Integer userId, List<Integer> warehouseIds, WarehouseStatus status) {
        Assert.notNull(userId, "仓库状态修改人不能为空");
        Assert.notNull(status, "仓库状态参数不能为空");
        Assert.notEmpty(warehouseIds, "仓库状态修改项不能为空");

        Warehouse record = new Warehouse();
        record.setStatus(status.toString());
        record.setUpdator(userId);
        record.setUpdateDate(new Date());

        Condition con = new Condition(Warehouse.class);
        con.createCriteria().andIn("id", warehouseIds)
                .andNotEqualTo("status", status.toString());
        this.warehouseMapper.updateByConditionSelective(record, con);
    }
}
