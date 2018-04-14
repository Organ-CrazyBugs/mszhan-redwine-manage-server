package com.mszhan.redwine.manage.server.web.rest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 10:23 2018/4/13
 */
@RestController
public class OrderRestController {

    @Autowired
    private ProductMapper productMapper;

    @GetMapping(value = "/api/product/selectPopupList")
    public Object selectProductDataList(Requests requests){
        String sku = requests.getString("sku", null);
        String productName = requests.getString("productName", null);
        Integer offset = requests.getInteger("offset", 0);
        Integer limit = requests.getInteger("limit", 10);

        Page<Object> page = PageHelper.offsetPage(offset, limit)
                .doSelectPage(() -> this.productMapper.fetchProductSelectPopupData(sku, productName));

        return Responses.newInstance().succeed(PaginateResult.newInstance(page.getTotal(), page));
    }

}
