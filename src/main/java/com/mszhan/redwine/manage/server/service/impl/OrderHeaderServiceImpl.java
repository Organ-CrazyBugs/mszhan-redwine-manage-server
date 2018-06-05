package com.mszhan.redwine.manage.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mszhan.redwine.manage.server.config.security.User;
import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.core.SecurityUtils;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.*;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.*;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AddOrderPojo;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.OrderQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.CreateOrderVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.OrderCancelVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.OrderMarkPaymentVO;
import com.mszhan.redwine.manage.server.service.OrderHeaderService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import com.mszhan.redwine.manage.server.util.SequenceIdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
@Service
@Transactional
public class OrderHeaderServiceImpl extends AbstractService<OrderHeader> implements OrderHeaderService {
    @Resource
    private OrderHeaderMapper orderHeaderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private AgentsMapper agentsMapper;
    @Autowired
    private SequenceIdGenerator sequenceIdGenerator;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private InventoryMapper inventoryMapper;
    @Autowired
    private InboundHistoryMapper inboundHistoryMapper;
    @Autowired
    private OutboundHistoryMapper outboundHistoryMapper;
    @Autowired
    private AgentPriceHistoryMapper agentPriceHistoryMapper;

    @Override
    public PaginateResult<OrderHeader> queryForPage(OrderQuery query) {
        Integer count = orderHeaderMapper.queryCount(query);
        if (count == null || count.equals(0)){
            return PaginateResult.newInstance(0, new ArrayList<>());
        }
        List<OrderHeader> list = orderHeaderMapper.queryForPage(query);
        return PaginateResult.newInstance(Long.valueOf(count), list);
    }

    @Transactional
    @Override
    public OrderHeader createOrder(CreateOrderVO orderVO) {
        Assert.notNull(orderVO, "订单信息不能为空");
        Assert.notNull(orderVO.getAgentId(), "所属代理不能为空");
        Assert.hasLength(orderVO.getCustomerName(), "收件人不能为空");
        Assert.hasLength(orderVO.getPhoneNumber(), "联系电话不能为空");
//        Assert.hasLength(orderVO.getPostalCode(), "邮政编码不能为空");

        Assert.hasLength(orderVO.getAddress(), "收件人地址不能为空");
        Assert.notEmpty(orderVO.getProductList(), "商品项信息不能为空");
        Assert.isTrue(orderVO.getShipAmount() == null || orderVO.getShipAmount().compareTo(BigDecimal.ZERO) >= 0, "请输入正确的运费信息");

        BigDecimal paymentAmount = BigDecimal.ZERO;
        if (StringUtils.isNotBlank(orderVO.getPaymentTypeId()) || orderVO.getPaymentAmount() != null) {
            paymentAmount = Optional.ofNullable(orderVO.getPaymentAmount()).orElse(BigDecimal.ZERO);
            Assert.hasLength(orderVO.getPaymentTypeId(), "请选择订单支付方式");
            Assert.isTrue(paymentAmount.compareTo(BigDecimal.ZERO) > 0, "支付金额不能小于等于零");
        }

        BigDecimal orderTotal = BigDecimal.ZERO;
        if (orderVO.getShipAmount() != null) {
            orderTotal = orderTotal.add(orderVO.getShipAmount());
        }

        User authenticationUser = SecurityUtils.getAuthenticationUser();
        Integer createAgentId = authenticationUser.getAgentId();
        Agents agentInfo = agentsMapper.selectByPrimaryKey(orderVO.getAgentId());
        Assert.notNull(agentInfo, "选择代理信息未找到");

        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setAgentId(orderVO.getAgentId());
        orderHeader.setAddress(orderVO.getAddress());
        orderHeader.setClientName(orderVO.getCustomerName());
        orderHeader.setAgentName(agentInfo.getName());
        orderHeader.setShippingFee(orderVO.getShipAmount() == null ? BigDecimal.ZERO : orderVO.getShipAmount());

        orderHeader.setPostalCode(orderVO.getPostalCode());
        orderHeader.setRemark(orderVO.getOrderRemark());
        orderHeader.setCreateDate(new Date());
        orderHeader.setCreator(createAgentId);
        orderHeader.setUpdateDate(new Date());

        orderHeader.setUpdator(createAgentId);
        orderHeader.setPhoneNumber(orderVO.getPhoneNumber());

        // 校验订单项信息
        List<OrderItem> items = orderVO.getProductList().stream().map(proItem -> {
            Assert.hasLength(proItem.getSku(), "商品SKU不能为空");
            Assert.notNull(proItem.getQuantity(), "请指定商品数量");
            Assert.isTrue(proItem.getQuantity() > 0, "请指定正确的商品数量(正整数)");
            Assert.notNull(proItem.getWarehouseId(), "商品发货仓不能为空");

            Warehouse warehouse = warehouseMapper.selectByPrimaryKey(proItem.getWarehouseId());
            Assert.notNull(warehouse, "商品发货仓信息未找到");

            BigDecimal unitPrice = Optional.ofNullable(proItem.getUnitPrice()).orElse(BigDecimal.ZERO);
            BigDecimal packPrice = Optional.ofNullable(proItem.getPackagePrice()).orElse(BigDecimal.ZERO);
            String itemType = unitPrice.compareTo(BigDecimal.ZERO) == 0 ? "GIFT" : "SALES";

            OrderItem item = new OrderItem();
            item.setSku(proItem.getSku());
            item.setCreateDate(new Date());
            if ("UNIT_BOX".equals(proItem.getUnit())) {
                // 一箱为6支
                item.setQuantity(proItem.getQuantity() * 6);
            } else {
                item.setQuantity(proItem.getQuantity());
            }
            item.setPackagingFee(packPrice);
            item.setUnitPrice(unitPrice);

            item.setType(itemType);
            item.setCreateDate(new Date());
            item.setCreator(createAgentId);
            item.setUpdateDate(new Date());
            item.setUpdator(createAgentId);

            item.setWarehouseId(warehouse.getId());
            item.setWarehouseName(warehouse.getName());
            return item;
        }).collect(Collectors.toList());

        for (OrderItem item : items) {
            orderTotal = orderTotal.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()))).add(item.getPackagingFee());
        }

        orderHeader.setTotalAmount(orderTotal);
        orderHeader.setStatus("WAIT_DEAL");

        // 创建订单号
        String orderId = sequenceIdGenerator.getSeqValue("OrderId", 4);// 创建订单号
        orderHeader.setOrderId(orderId);
        orderHeader.setPaymentStatus("UNPAID");
        this.orderHeaderMapper.insert(orderHeader);

        // 计算订单支付状态
        if (paymentAmount.compareTo(BigDecimal.ZERO) != 0) {    //存在支付金额
            OrderMarkPaymentVO paymentVO = new OrderMarkPaymentVO();
            paymentVO.setOrderId(orderHeader.getOrderId());
            paymentVO.setPaymentAmount(paymentAmount);
            paymentVO.setPaymentTypeId(orderVO.getPaymentTypeId());
            paymentVO.setRemark(orderVO.getPaymentRemark());

            orderMarkPayment(paymentVO);
        }

        for (OrderItem item : items) {
            item.setOrderId(orderId);
        }
        this.orderItemMapper.insertList(items);
        return orderHeader;
    }

    /**
     * 订单标记已发货
     */
    @Transactional
    @Override
    public void orderMarkShipped(List<String> orderIds) {
        User user = SecurityUtils.getAuthenticationUser();
        Assert.notNull(user, "未登录，请先登陆");
        if (CollectionUtils.isEmpty(orderIds)) {
            return;
        }
        Condition con = new Condition(OrderHeader.class);
        con.createCriteria().andIn("orderId", orderIds);
        List<OrderHeader> orderHeaders = this.orderHeaderMapper.selectByCondition(con);
        if (CollectionUtils.isEmpty(orderHeaders)) {
            return;
        }
        orderHeaders.forEach(orderHeader -> {
            // 判断订单状态, 待处理的订单状态才能出库
            if (!"WAIT_DEAL".equals(orderHeader.getStatus())) {
                return;
            }

            Condition itemCon = new Condition(OrderItem.class);
            itemCon.createCriteria().andEqualTo("orderId", orderHeader.getOrderId());
            List<OrderItem> orderItems = this.orderItemMapper.selectByCondition(itemCon);
            if (CollectionUtils.isEmpty(orderItems)) {
                return;
            }
            orderItems.forEach(orderItem -> {
                Condition inventoryCon = new Condition(Inventory.class);
                inventoryCon.createCriteria().andEqualTo("sku", orderItem.getSku())
                        .andEqualTo("wareHouseId", orderItem.getWarehouseId());
                List<Inventory> inventories = this.inventoryMapper.selectByCondition(inventoryCon);
                if (CollectionUtils.isEmpty(inventories)) {
                    throw BasicException.newInstance().error(String.format("订单[%s]中SKU[%s]发货仓库[%s]没有找到库存信息",
                            orderHeader.getOrderId(), orderItem.getSku(), orderItem.getWarehouseName()), 500);
                }
                Inventory inventory = inventories.get(0);
                Integer invQty = inventory.getQuantity() == null ? 0 : inventory.getQuantity();
                if (invQty < orderItem.getQuantity()) {
                    throw BasicException.newInstance().error(String.format("订单[%s]中SKU[%s]发货仓库[%s]库存不足",
                            orderHeader.getOrderId(), orderItem.getSku(), orderItem.getWarehouseName()), 500);
                }
                // 扣减库存
                Inventory updateInv = new Inventory();
                updateInv.setId(inventory.getId());
                updateInv.setQuantity(invQty - orderItem.getQuantity() );
                this.inventoryMapper.updateByPrimaryKeySelective(updateInv);

                // 插入出入库记录信息
                OutboundHistory history = new OutboundHistory();
                history.setCreateDate(new Date());
                history.setOrderId(orderHeader.getOrderId());
                history.setOrderItemId(orderItem.getId());
                history.setSku(orderItem.getSku());
                history.setType("SALES_INBOUND");
                history.setCreator(user.getAgentId());
                history.setCreatorName(user.getAgentName());
                history.setQuantity(orderItem.getQuantity());
                history.setWarehouseId(orderItem.getWarehouseId());

                this.outboundHistoryMapper.insert(history);
            });

            // 更新订单状态为已发货
            OrderHeader updateOH = new OrderHeader();
            updateOH.setOrderId(orderHeader.getOrderId());
            updateOH.setStatus("SHIPPED");
            this.orderHeaderMapper.updateByPrimaryKeySelective(updateOH);
        });
    }

    @Transactional
    @Override
    public void orderMarkPayment(OrderMarkPaymentVO paymentVO) {
        User user = SecurityUtils.getAuthenticationUser();
        Assert.notNull(user, "缺少登陆信息");
        Assert.notNull(paymentVO, "缺少参数信息");
        Assert.hasLength(paymentVO.getOrderId(), "订单号参数不能为空");
        Assert.hasLength(paymentVO.getPaymentTypeId(), "支付方式不能为空");
        Assert.notNull(paymentVO.getPaymentAmount(), "支付金额不能为空");

        OrderHeader orderHeader = this.orderHeaderMapper.selectByPrimaryKey(paymentVO.getOrderId());
        Assert.notNull(orderHeader, "订单号：" + paymentVO.getOrderId() + "订单信息未找到");
        Assert.isTrue("UNPAID".equals(orderHeader.getPaymentStatus()), "未付款订单才能标帐操作");
        Assert.isTrue(!"REMOVED".equals(orderHeader.getStatus()), "已删除的订单无法标帐操作");

        BigDecimal totalAmount = orderHeader.getTotalAmount();
        Assert.isTrue(totalAmount.compareTo(paymentVO.getPaymentAmount()) == 0, "支付金额必须等于应付总额");

        // 改变订单支付状态
        OrderHeader updateOh = new OrderHeader();
        updateOh.setOrderId(paymentVO.getOrderId());
        updateOh.setPaymentStatus("PAID");
        this.orderHeaderMapper.updateByPrimaryKeySelective(updateOh);

        // 如果是 扣除AGENT_PAYMENT支付类型，扣除代理商余额
        if ("AGENT_PAYMENT".equals(paymentVO.getPaymentTypeId())) {
            Agents agents = this.agentsMapper.selectByPrimaryKey(orderHeader.getAgentId());
            Assert.notNull(agents, "订单所属代理信息未找到");
            BigDecimal balance = agents.getBalance() == null ? BigDecimal.ZERO : agents.getBalance();
            Assert.isTrue(balance.compareTo(paymentVO.getPaymentAmount()) >= 0, "代理余额不足以支付订单应付总额，当前代理余额：" + balance.setScale(2, BigDecimal.ROUND_HALF_UP));

            Agents updateAgents = new Agents();
            updateAgents.setId(agents.getId());
            updateAgents.setBalance(balance.subtract(orderHeader.getTotalAmount()));
            this.agentsMapper.updateByPrimaryKeySelective(updateAgents);
        }

        // 记录支付日志
        AgentPriceHistory history = new AgentPriceHistory();
        history.setCreateDate(new Date());
        history.setCreator(user.getAgentId());
        history.setCreatorName(user.getAgentName());
        history.setAgentId(orderHeader.getAgentId());
        history.setPaymentType(paymentVO.getPaymentTypeId());

        history.setPrice(paymentVO.getPaymentAmount());
        history.setType("SPEND");
        history.setRemark(paymentVO.getRemark());
        history.setOrderId(paymentVO.getOrderId());
        this.agentPriceHistoryMapper.insert(history);
    }

    @Transactional
    @Override
    public void orderCancel(OrderCancelVO cancelVO) {
        Assert.notNull(cancelVO, "参数信息不能为空");
        Assert.hasLength(cancelVO.getOrderId(), "需求消单的订单号不能为空");

        OrderHeader orderHeader = this.orderHeaderMapper.selectByPrimaryKey(cancelVO.getOrderId());
        Assert.notNull(orderHeader, "订单号对应的信息未找到");
        Assert.isTrue(!"REMOVED".equals(orderHeader.getStatus()), "已删除订单无法进行消单操作");

        if ("SHIPPED".equalsIgnoreCase(orderHeader.getStatus())) {
            // 已发货，回滚库存
            Condition fetchItemCon = new Condition(OrderItem.class);
            fetchItemCon.createCriteria().andEqualTo("orderId", orderHeader.getOrderId());
            List<OrderItem> orderItems = this.orderItemMapper.selectByCondition(fetchItemCon);
            orderItems.forEach(item -> {
                Condition inventoryCon = new Condition(Inventory.class);
                inventoryCon.createCriteria().andEqualTo("sku", item.getSku())
                        .andEqualTo("wareHouseId", item.getWarehouseId());
                List<Inventory> inventories = this.inventoryMapper.selectByCondition(inventoryCon);
                if (inventories.isEmpty()) {
                    return;
                }
                Inventory inventory = inventories.get(0);
                Integer invQty = inventory.getQuantity() == null ? 0 : inventory.getQuantity();
                // 增加库存
                Inventory updateInv = new Inventory();
                updateInv.setId(inventory.getId());
                updateInv.setQuantity(invQty + item.getQuantity());
                this.inventoryMapper.updateByPrimaryKeySelective(updateInv);
            });

            // 删除出库记录
            Condition delHistoryCon = new Condition(OutboundHistory.class);
            delHistoryCon.createCriteria().andEqualTo("orderId", cancelVO.getOrderId());
            this.outboundHistoryMapper.deleteByCondition(delHistoryCon);
        }

        // 标记订单状态为 REMOVED
        OrderHeader updateHeader = new OrderHeader();
        updateHeader.setOrderId(cancelVO.getOrderId());
        updateHeader.setStatus("REMOVED");
        this.orderHeaderMapper.updateByPrimaryKeySelective(updateHeader);
    }

    @Override
    public void addOrder(AddOrderPojo addOrderPojo) {
        if (StringUtils.isBlank(addOrderPojo.getOrderContent())){
            throw BasicException.newInstance().error("订单信息不能为空", 500);
        }
        if (StringUtils.isBlank(addOrderPojo.getOrderItemContents())){
            throw BasicException.newInstance().error("订单商品信息不能为空", 500);
        }
        ObjectMapper objMr = new ObjectMapper();
        OrderHeader orderHeader = null;
        try {
            orderHeader = objMr.readValue(addOrderPojo.getOrderContent(), OrderHeader.class);
        } catch (IOException e) {
            throw BasicException.newInstance().error("订单信息转化异常", 500);
        }
    }
}
