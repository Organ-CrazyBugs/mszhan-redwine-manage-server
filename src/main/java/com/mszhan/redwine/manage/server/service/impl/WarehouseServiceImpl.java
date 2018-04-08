package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.WarehouseMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Warehouse;
import com.mszhan.redwine.manage.server.service.WarehouseService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        if (warehouse == null) {
            throw BasicException.newInstance().error("缺少仓库信息参数", 500);
        }
        if (userId == null) {
            throw BasicException.newInstance().error("仓库创建人不能为空", 500);
        }
        if (StringUtils.isBlank(warehouse.getName())) {
            throw BasicException.newInstance().error("仓库名称不能为空", 500);
        }
        if (StringUtils.isBlank(warehouse.getPrincipal())) {
            throw BasicException.newInstance().error("仓库负责人不能为空", 500);
        }
        if (StringUtils.isBlank(warehouse.getPhone())) {
            throw BasicException.newInstance().error("联系方式不能为空", 500);
        }
        Condition checkNameCon = new Condition(Warehouse.class);
        checkNameCon.createCriteria().andEqualTo("name", warehouse.getName());
        int nameExists = this.warehouseMapper.selectCountByCondition(checkNameCon);
        if (nameExists > 0) {
            throw BasicException.newInstance().error("该仓库名称已经存在", 500);
        }

        warehouse.setCreator(userId);
        warehouse.setUpdator(userId);
        warehouse.setCreateDate(new Date());
        warehouse.setUpdateDate(new Date());
        this.warehouseMapper.insert(warehouse);
        return warehouse;
    }

    @Override
    public void changeStatus(Integer userId, List<Integer> warehouseIds, WarehouseStatus status) {
        if (userId == null) {
            throw BasicException.newInstance().error("仓库状态修改人不能为空", 500);
        }
        if (CollectionUtils.isEmpty(warehouseIds)) {
            throw BasicException.newInstance().error("仓库状态修改项不能为空", 500);
        }
        if (status == null) {
            throw BasicException.newInstance().error("仓库状态参数不能为空", 500);
        }

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
