package com.mszhan.redwine.manage.server.web;

import com.mszhan.redwine.manage.server.enums.AgentOperationTypeEnum;
import com.mszhan.redwine.manage.server.enums.PaymentTypeEnum;
import com.mszhan.redwine.manage.server.util.ResponseUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/common")
public class CommonController {



    @GetMapping(value = "/agents_payment_type_select")
    public ResponseUtils.ResponseVO paymentTypeSelect() {
        Map<String, Object> resultMap = new HashMap<>();
        for (PaymentTypeEnum en : PaymentTypeEnum.values()){
            resultMap.put(en.toString(), en.getValue());
        }
        ResponseUtils.ResponseVO vo = new ResponseUtils.ResponseVO();
        vo.setData(resultMap);
        return  vo;
    }

    @GetMapping(value = "/agents_operation_type_select")
    public ResponseUtils.ResponseVO agentsOperationTypeSelect() {
        Map<String, Object> resultMap = new HashMap<>();
        for (AgentOperationTypeEnum en : AgentOperationTypeEnum.values()){
            resultMap.put(en.toString(), en.getValue());
        }
        ResponseUtils.ResponseVO vo = new ResponseUtils.ResponseVO();
        vo.setData(resultMap);
        return  vo;
    }

}
