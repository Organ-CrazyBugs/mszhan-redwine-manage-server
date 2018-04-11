package com.mszhan.redwine.manage.server.web.rest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.core.SecurityUtils;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.WarehouseMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Warehouse;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.WarehouseEditVO;
import com.mszhan.redwine.manage.server.service.WarehouseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 10:53 2018/4/7
 */
@RestController
public class WarehouseRestController {

    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping(value = "/api/warehouse/manage/list")
    public Object list(Requests request) {
        String warehouseName = request.getString("warehouseName", null);
        String status = request.getString("status", null);
        Integer offset = request.getInteger("offset", 0);
        Integer limit = request.getInteger("limit", 10);

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

    @PostMapping(value = "/api/warehouse/manage/create")
    public Object createWarehouse(@RequestBody WarehouseEditVO createVO){
        Warehouse warehouse = new Warehouse();
        warehouse.setName(createVO.getName());
        warehouse.setAddress(createVO.getAddress());
        warehouse.setPhone(createVO.getPhone());
        warehouse.setPrincipal(createVO.getPrincipal());
        warehouse.setTel(createVO.getTel());
        warehouse.setRemark(createVO.getRemark());
        warehouse.setStatus(WarehouseService.WarehouseStatus.ENABLED.toString());

        Integer userId = SecurityUtils.getAuthenticationUser().getUserLoginId();

        this.warehouseService.createWarehouse(userId, warehouse);

        return Responses.newInstance().succeed();
    }

    @PutMapping(value = "/api/warehouse/manage/change_status")
    public Object changeStatus(Requests request){
        List<Integer> warehouseIds = request.getIntegerArray("warehouseIds", ",", new ArrayList<>());
        WarehouseService.WarehouseStatus status = request.getEnum("status", WarehouseService.WarehouseStatus.class, null);

        Integer userId = SecurityUtils.getAuthenticationUser().getUserLoginId();

        this.warehouseService.changeStatus(userId, warehouseIds, status);

        return Responses.newInstance().succeed();
    }

    @PutMapping(value = "/api/warehouse/manage/update")
    public Object update(@RequestBody WarehouseEditVO editVO){
        Warehouse warehouse = new Warehouse();
        warehouse.setId(editVO.getId());
        warehouse.setName(editVO.getName());
        warehouse.setAddress(editVO.getAddress());
        warehouse.setPhone(editVO.getPhone());
        warehouse.setPrincipal(editVO.getPrincipal());
        warehouse.setTel(editVO.getTel());
        warehouse.setRemark(editVO.getRemark());

        Integer userId = SecurityUtils.getAuthenticationUser().getUserLoginId();

        this.warehouseService.updateWarehouse(userId, warehouse);

        return Responses.newInstance().succeed();
    }

}
