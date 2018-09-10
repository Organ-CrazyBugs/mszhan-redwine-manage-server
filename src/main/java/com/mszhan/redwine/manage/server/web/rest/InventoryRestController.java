package com.mszhan.redwine.manage.server.web.rest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.InventoryMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.InventoryQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.FetchInventoryVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryInputVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryOutputVO;
import com.mszhan.redwine.manage.server.service.InventoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class InventoryRestController {

    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InventoryService inventoryService;

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
        List<FetchInventoryVO> voList = page.getResult();
        for (FetchInventoryVO v : voList){
            String qtt = "";
            String wineType = v.getWineType();
            Integer qty = v.getQuantity();
            String unit = v.getUnit();
            if (StringUtils.isNotBlank(wineType)) {
                String des;
                if (qty < 6) {
                    des = "";
                } else if (qty % 6 == 0) {
                    des = String.format("（%s箱）", qty/6);
                } else {
                    des = String.format("（%s箱%s%s）", qty/6, qty%6, unit);
                }
                qtt = String.format("%s%s%s", qty, unit, des);
            } else {
                if (qty >= 0){
                    qtt = String.format("%s%s", qty, unit);
                }
            }
            v.setQuantityDes(qtt);
        }
        return Responses.newInstance().succeed(PaginateResult.newInstance(page.getTotal(), page));
    }

    @PostMapping("/api/inventory/input")
    public Object inventoryInput(@RequestBody InventoryInputVO vo) {
        this.inventoryService.inventoryInput(vo);
        return Responses.newInstance().succeed();
    }

    @PostMapping("/api/inventory/output")
    public Object inventoryOutput(@RequestBody InventoryOutputVO vo) {
        this.inventoryService.inventoryOutput(vo);
        return Responses.newInstance().succeed();
    }


    @GetMapping(value = "/api/inventory/lead_out_outbound_excel")
    public void leadOutOutboundExcel(HttpServletResponse res, InventoryQuery query){
        inventoryService.leadOutOutboundDetail(query, res);
    }

    @GetMapping(value = "/api/inventory/lead_out_inbound_excel")
    public void leadOutInboundExcel(HttpServletResponse res, InventoryQuery query){
        inventoryService.leadOutInboundDetail(query, res);
    }

    @GetMapping(value = "/api/inventory/lead_out_inventory_excel")
    public void leadOutInventoryExcel(HttpServletResponse res, InventoryQuery query){
        inventoryService.leadOutInventory(query, res);
    }

}
