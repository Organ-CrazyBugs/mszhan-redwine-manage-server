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
        String clientName = requests.getString("clientName", null);
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
                        createStartDate, createEndDate, deliveryStartDate, deliveryEndDate, clientName
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
    public void orderSalesExport(OrderQuery query, HttpServletResponse response){
        this.orderHeaderService.orderSalesExport(query, response);
    }

    @GetMapping(value = "/api/order/lead_out_outbound_excel")
    public void leadOutOutboundExcel(HttpServletResponse res, OrderQuery query){
        orderHeaderService.leadOutOrderOutboundExcel(res, query);
    }
}