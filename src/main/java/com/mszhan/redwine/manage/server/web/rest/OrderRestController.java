package com.mszhan.redwine.manage.server.web.rest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.CreateOrderVO;
import com.mszhan.redwine.manage.server.service.OrderHeaderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 10:23 2018/4/13
 */
@RestController
public class OrderRestController {
    @Autowired
    private OrderHeaderService orderHeaderService;
    @Autowired
    private ProductMapper productMapper;

    @GetMapping(value = "/api/product/selectPopupList")
    public Object selectProductDataList(Requests requests){
        String sku = requests.getString("sku", null);
        String productName = requests.getString("productName", null);
        Integer offset = requests.getInteger("offset", 0);
        Integer limit = requests.getInteger("limit", 10);

        String skuLikeVal = StringUtils.isBlank(sku) ? "" : String.format("%%%s%%", sku);
        String productNameLikeVal = StringUtils.isBlank(productName) ? "" : String.format("%%%s%%", productName);

        Page<Object> page = PageHelper.offsetPage(offset, limit)
                .doSelectPage(() -> this.productMapper.fetchProductSelectPopupData(skuLikeVal, productNameLikeVal));

        return Responses.newInstance().succeed(PaginateResult.newInstance(page.getTotal(), page));
    }

    @PostMapping(value = "/api/order/create")
    public Object createOrder(@RequestBody CreateOrderVO vo){
        this.orderHeaderService.createOrder(vo);
        return Responses.newInstance().succeed();
    }
}
