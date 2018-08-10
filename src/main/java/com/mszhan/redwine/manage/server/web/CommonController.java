package com.mszhan.redwine.manage.server.web;

import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.WarehouseMapper;
import com.mszhan.redwine.manage.server.enums.AgentOperationTypeEnum;
import com.mszhan.redwine.manage.server.enums.PaymentTypeEnum;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private WarehouseMapper warehouseMapper;


    @GetMapping(value = "/agents_payment_type_select")
    public Object paymentTypeSelect() {
        Map<String, Object> resultMap = new HashMap<>();
        for (PaymentTypeEnum en : PaymentTypeEnum.values()){
            resultMap.put(en.toString(), en.getValue());
        }
        return Responses.newInstance().succeed(resultMap);
    }

    @GetMapping(value = "/agents_operation_type_select")
    public Object agentsOperationTypeSelect() {
        Map<String, Object> resultMap = new HashMap<>();
        for (AgentOperationTypeEnum en : AgentOperationTypeEnum.values()){
            resultMap.put(en.toString(), en.getValue());
        }
        return Responses.newInstance().succeed(resultMap);
    }

    @GetMapping(value = "/warehouse_select")
    public Object warehouseSelect() {
        Map<String, Object> resultMap = new HashMap<>();

        List<Warehouse> warehouseList = warehouseMapper.selectAll();
        for (Warehouse ws : warehouseList){
            resultMap.put(ws.getId().toString(), ws.getName());
        }
        return Responses.newInstance().succeed(resultMap);
    }




}
