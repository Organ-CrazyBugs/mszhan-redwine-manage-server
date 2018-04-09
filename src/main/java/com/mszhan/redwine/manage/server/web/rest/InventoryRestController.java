package com.mszhan.redwine.manage.server.web.rest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.InventoryMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.FetchInventoryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class InventoryRestController {

    @Autowired
    private InventoryMapper inventoryMapper;

    @GetMapping("/api/inventory/list")
    public Object list(HttpServletRequest request) {
        Requests requests = Requests.newInstance(request);

        Integer warehouseId = requests.getInteger("warehouseId", null);
        String sku = requests.getString("sku", null);
        String productName = requests.getString("productName", null);
        String brandName = requests.getString("brandName", null);

        Integer offset = requests.getInteger("offset", 0);
        Integer limit = requests.getInteger("limit", 10);

        String skuLike = StringUtils.isBlank(sku) ? "" : String.format("%%%s%%", sku);
        String productNameLike = StringUtils.isBlank(productName) ? "" : String.format("%%%s%%", productName);
        String brandNameLike = StringUtils.isBlank(brandName) ? "" : String.format("%%%s%%", brandName);

        Page<FetchInventoryVO> page = PageHelper.offsetPage(offset, limit)
                .doSelectPage(() -> this.inventoryMapper.fetchInventory(warehouseId, skuLike, productNameLike, brandNameLike));

        return Responses.newInstance().succeed(PaginateResult.newInstance(page.getTotal(), page));
    }

}
