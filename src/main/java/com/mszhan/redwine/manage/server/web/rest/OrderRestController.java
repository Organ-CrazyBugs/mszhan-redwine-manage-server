package com.mszhan.redwine.manage.server.web.rest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mszhan.redwine.manage.server.config.security.User;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.core.SecurityUtils;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.OrderHeaderMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.OrderItemMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.enums.AgentTypeEnum;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderHeader;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderItem;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.OrderQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.CreateOrderVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.OrderCancelVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.OrderItemPriceUpdateVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.OrderMarkPaymentVO;
import com.mszhan.redwine.manage.server.service.OrderHeaderService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Condition;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private OrderHeaderMapper orderHeaderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;

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

    @GetMapping(value = "/api/order/list")
    public Object orderList(Requests requests) {
        Integer offset = requests.getInteger("offset", 0);
        Integer limit = requests.getInteger("limit", 10);

        Integer agentId = requests.getInteger("agentId", null);
        String orderId = requests.getString("orderId", null);
        String sku = requests.getString("sku", null);
        String productName = requests.getString("productName", null);
        String brandName = requests.getString("brandName", null);

        String orderStatus = requests.getString("orderStatus", null);
        String paymentStatus = requests.getString("paymentStatus", null);
        Date createStartDate = requests.getDate("createStartDate", new SimpleDateFormat("yyyy-MM-dd"), null);
        Date createEndDate = requests.getDate("createEndDate", new SimpleDateFormat("yyyy-MM-dd"), null);
        Date deliveryStartDate = requests.getDate("deliveryStartDate", new SimpleDateFormat("yyyy-MM-dd"), null);
        Date deliveryEndDate = requests.getDate("deliveryEndDate", new SimpleDateFormat("yyyy-MM-dd"), null);

        String brandNameLikeVal = StringUtils.isBlank(brandName) ? "" : String.format("%%%s%%", brandName);
        String productNameLikeVal = StringUtils.isBlank(productName) ? "" : String.format("%%%s%%", productName);

        User user = SecurityUtils.getAuthenticationUser();
        if (AgentTypeEnum.AGENT.toString().equals(user.getAgentType())){
            agentId = user.getAgentId();
        }
        Integer finalAgentId = agentId;
        Page<OrderHeader> page = PageHelper.offsetPage(offset, limit)
                .doSelectPage(() -> this.orderHeaderMapper.fetchOrders(finalAgentId, orderId, productNameLikeVal, sku, brandNameLikeVal, orderStatus, paymentStatus,
                        createStartDate, createEndDate, deliveryStartDate, deliveryEndDate
                        ));

        if (!CollectionUtils.isEmpty(page)) {
            List<String> orderIds = page.stream().map(OrderHeader::getOrderId).collect(Collectors.toList());
            Condition fetchOrderItemCon = new Condition(OrderItem.class);
            fetchOrderItemCon.createCriteria().andIn("orderId", orderIds);
            List<OrderItem> orderItems = orderItemMapper.selectByCondition(fetchOrderItemCon);
            for (OrderItem orderItem : orderItems){
                List<Product> products = productMapper.queryProductBySku(orderItem.getSku());
                if (!CollectionUtils.isEmpty(products)){
                    orderItem.setProductName(products.get(0).getProductName());
                }
            }
            Map<String, List<OrderItem>> orderItemGroup = orderItems.stream().collect(Collectors.groupingBy(OrderItem::getOrderId));
            page.forEach(orderHeader -> orderHeader.setOrderItems(orderItemGroup.get(orderHeader.getOrderId())));
        }

        return Responses.newInstance().succeed(PaginateResult.newInstance(page.getTotal(), page));
    }

    /**
     * 订单标记已发货
     */
    @PostMapping(value = "/api/order/mark_shipped")
    public Object orderMarkShipped(Requests requests){
        List<String> orderIds = requests.getStringArray("orderIds", ",", new ArrayList<>());
        this.orderHeaderService.orderMarkShipped(orderIds);
        return Responses.newInstance().succeed();
    }

    /**
     * 订单标记已付款
     * @param paymentVO
     * @return
     */
    @PostMapping(value = "/api/order/mark_payment")
    public Object orderMarkPayment(@RequestBody OrderMarkPaymentVO paymentVO){
        this.orderHeaderService.orderMarkPayment(paymentVO);
        return Responses.newInstance().succeed();
    }

    @PostMapping(value = "/api/order/cancel")
    public Object orderCancel(Requests requests) {
        String orderId = requests.getString("orderId", null);
        OrderCancelVO cancelVO = new OrderCancelVO();
        cancelVO.setOrderId(orderId);
        this.orderHeaderService.orderCancel(cancelVO);
        return Responses.newInstance().succeed();
    }

    @PostMapping("/api/order/update_item_price")
    public Object updateItemPrice(@RequestBody OrderItemPriceUpdateVO vo) {
        this.orderHeaderService.updateItemPrice(vo);
        return Responses.newInstance().succeed();
    }

    @PostMapping(value = "/api/order/create")
    public Object createOrder(@RequestBody CreateOrderVO vo){
        OrderHeader order = this.orderHeaderService.createOrder(vo);
        return Responses.newInstance().succeed(order);
    }

    @GetMapping("/api/order/order_sales_export")
    public void exportOrderSalesExcel(Requests requests, HttpServletResponse response) {
        Integer agentId = requests.getInteger("agentId", null);
        String orderId = requests.getString("orderId", null);
        String sku = requests.getString("sku", null);
        String productName = requests.getString("productName", null);
        String brandName = requests.getString("brandName", null);

        String orderStatus = requests.getString("orderStatus", null);
        String paymentStatus = requests.getString("paymentStatus", null);
        Date createStartDate = requests.getDate("createStartDate", new SimpleDateFormat("yyyy-MM-dd"), null);
        Date createEndDate = requests.getDate("createEndDate", new SimpleDateFormat("yyyy-MM-dd"), null);
        Date deliveryStartDate = requests.getDate("deliveryStartDate", new SimpleDateFormat("yyyy-MM-dd"), null);
        Date deliveryEndDate = requests.getDate("deliveryEndDate", new SimpleDateFormat("yyyy-MM-dd"), null);

        String brandNameLikeVal = StringUtils.isBlank(brandName) ? "" : String.format("%%%s%%", brandName);
        String productNameLikeVal = StringUtils.isBlank(productName) ? "" : String.format("%%%s%%", productName);

        User user = SecurityUtils.getAuthenticationUser();
        if (AgentTypeEnum.AGENT.toString().equals(user.getAgentType())){
            agentId = user.getAgentId();
        }

        List<OrderHeader> orderHeaders = this.orderHeaderMapper.fetchOrders(agentId, orderId, productNameLikeVal, sku, brandNameLikeVal, orderStatus, paymentStatus,
                createStartDate, createEndDate, deliveryStartDate, deliveryEndDate
        );

        if (!CollectionUtils.isEmpty(orderHeaders)) {
            List<String> orderIds = orderHeaders.stream().map(OrderHeader::getOrderId).collect(Collectors.toList());
            List<OrderItem> orderItems = orderItemMapper.fetchOrderItemsByOrderId(orderIds);
            Map<String, List<OrderItem>> orderItemGroup = orderItems.stream().collect(Collectors.groupingBy(OrderItem::getOrderId));
            orderHeaders.forEach(orderHeader -> orderHeader.setOrderItems(orderItemGroup.get(orderHeader.getOrderId())));
        }

        // 计算项目产品种类个数
        List<String> skuList = orderHeaders.stream()
                .flatMap(oh -> oh.getOrderItems().stream())
                .map(oi -> String.format("%s___%s", oi.getSku(), oi.getProductName())).distinct()
                .collect(Collectors.toList());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        response.setContentType("application/vnd.ms-excel");
        String fileName = "OrderSalesExport";
        try {
            response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "utf-8") + "-" + sdf.format(new Date()) + ".xlsx");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        Sheet sheet = workbook.createSheet("sheet");
        sheet.setDefaultColumnWidth(20);

        int rowIndex = 0;
        Row topRow = sheet.createRow(rowIndex++);
        Cell topCell = topRow.createCell(0);
        topCell.setCellValue("深圳市汇纳酒业有限公司");
        sheet.addMergedRegion(new CellRangeAddress(
                0,
                0,
                0,
                skuList.size() + 5
        ));

        topRow = sheet.createRow(rowIndex++);
        topCell = topRow.createCell(0);
        topCell.setCellValue("销售报表");
        sheet.addMergedRegion(new CellRangeAddress(
                1,
                1,
                0,
                skuList.size() + 5
        ));

        Row header = sheet.createRow(rowIndex++);

        for (int i = -3; i < skuList.size() + 3; i++) {
            Cell cell = header.createCell(i + 3);
            if (i == -3) {
                cell.setCellValue("序号");
            } else if (i == -2) {
                cell.setCellValue("日期");
            } else if (i == -1) {
                cell.setCellValue("客户");
            } else if (i < skuList.size()){
                cell.setCellValue(skuList.get(i).split("___")[1]);
            } else if (i == skuList.size()) {
                cell.setCellValue("销售额");
            } else if (i == skuList.size() + 1) {
                cell.setCellValue("成本");
            } else if (i == skuList.size() + 2) {
                cell.setCellValue("利润");
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        BigDecimal salesAmountTotal = BigDecimal.ZERO;
        BigDecimal costAmountTotal = BigDecimal.ZERO;

        for (int i = 0; i < orderHeaders.size(); i++) {
            Row row = sheet.createRow(rowIndex++);

            BigDecimal salesAmount = BigDecimal.ZERO;
            BigDecimal costAmount = BigDecimal.ZERO;
            for (int j = -3; j < skuList.size() + 3; j++) {
                Cell cell = row.createCell(j + 3);
                if (j == -3) {
                    cell.setCellValue(i + 1);
                } else if (j == -2) {
                    cell.setCellValue(dateFormat.format(orderHeaders.get(i).getCreateDate()));
                } else if (j == -1) {
                    cell.setCellValue( orderHeaders.get(i).getClientName() + "");
                } else if (j < skuList.size()){
                    String qty = "";
                    for (OrderItem orderItem : orderHeaders.get(i).getOrderItems()) {
                        if (skuList.get(j).startsWith(orderItem.getSku() + "___")) {
                            if (StringUtils.isNotBlank(orderItem.getWineType())) {
                                String des;
                                if (orderItem.getQuantity() < 6) {
                                    des = "";
                                } else if (orderItem.getQuantity() % 6 == 0) {
                                    des = String.format("（%s箱）", orderItem.getQuantity()/6);
                                } else {
                                    des = String.format("（%s箱%s瓶）", orderItem.getQuantity()/6, orderItem.getQuantity()%6);
                                }
                                qty = String.format("%s瓶%s", orderItem.getQuantity(), des);
                            } else {
                                qty = orderItem.getQuantity() + "";
                            }
                        }

                    }
                    cell.setCellValue(qty);
                } else if (j == skuList.size()) {
                    for (OrderItem orderItem : orderHeaders.get(i).getOrderItems()) {
                        salesAmount = salesAmount.add(orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getQuantity())));
                    }
                    cell.setCellValue(salesAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                } else if (j == skuList.size() + 1) {
                    for (OrderItem orderItem : orderHeaders.get(i).getOrderItems()) {
                        if (orderItem.getCost() == null) {
                            orderItem.setCost(BigDecimal.ZERO);
                        }
                        costAmount = costAmount.add(orderItem.getCost().multiply(new BigDecimal(orderItem.getQuantity())));
                    }
                    cell.setCellValue(costAmount.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                } else if (j == skuList.size() + 2) {
                    cell.setCellValue(salesAmount.subtract(costAmount).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }

            salesAmountTotal = salesAmountTotal.add(salesAmount);
            costAmountTotal = costAmountTotal.add(costAmount);
        }

        Map<String, Integer> sum = orderHeaders.stream().flatMap(oh -> oh.getOrderItems().stream())
                .collect(Collectors.groupingBy(OrderItem::getSku, Collectors.reducing(0, OrderItem::getQuantity, (i1, i2) -> i1 + i2)));

        Row footer = sheet.createRow(rowIndex++);
        for (int i = -3; i < skuList.size() + 3; i++) {
            Cell cell = footer.createCell(i + 3);
            if (i == -3) {
                cell.setCellValue("合计");
            } else if (i == -2) {
                cell.setCellValue("");
            } else if (i == -1) {
                cell.setCellValue("");
            } else if (i < skuList.size()){
                cell.setCellValue(sum.get(skuList.get(i).split("___")[0]));
            } else if (i == skuList.size()) {
                cell.setCellValue(salesAmountTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            } else if (i == skuList.size() + 1) {
                cell.setCellValue(costAmountTotal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            } else if (i == skuList.size() + 2) {
                cell.setCellValue(salesAmountTotal.subtract(costAmountTotal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/api/order/lead_out_outbound_excel")
    public void leadOutOutboundExcel(HttpServletResponse res, OrderQuery query){
        orderHeaderService.leadOutOrderOutboundExcel(res, query);
    }
}