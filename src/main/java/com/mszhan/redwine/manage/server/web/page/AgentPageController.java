package com.mszhan.redwine.manage.server.web.page;

import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.AgentsMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.OrderHeaderMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.OrderItemMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderHeader;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderItem;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by god on 2018/4/10.
 */
@RestController
@RequestMapping("/")
public class AgentPageController {

    @Autowired
    private AgentsMapper agentsMapper;
    @Autowired
    private OrderHeaderMapper orderHeaderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ProductMapper productMapper;

    @GetMapping(value = "/page/agent/index")
    public ModelAndView productIndex() {
        ModelAndView view = new ModelAndView("agent/agent");
        return view;
    }

    @RequestMapping(value = "/page/agent/print_unpay")
    public ModelAndView printUnpayOrders(Requests requests){
        Integer agentId = requests.getInteger("agentId", null);
        Agents agent = null;
        if (agentId != null) {
            agent = agentsMapper.selectByPrimaryKey(agentId);
        }
        if (agent == null) {
            return null;
        }
        if (agent.getBalance() == null) {
            agent.setBalance(BigDecimal.ZERO);
        }
        Condition orderCon = new Condition(OrderHeader.class);
        orderCon.createCriteria().andEqualTo("agentId", agent.getId())
                .andIn("status", Arrays.asList("SHIPPED", "RECEIVED"))
                .andEqualTo("paymentStatus", "UNPAID");
        List<OrderHeader> orderHeaders = this.orderHeaderMapper.selectByCondition(orderCon);

        if (!CollectionUtils.isEmpty(orderHeaders)) {
            List<String> orderIds = orderHeaders.stream().map(OrderHeader::getOrderId).collect(Collectors.toList());
            Condition fetchOrderItemCon = new Condition(OrderItem.class);
            fetchOrderItemCon.createCriteria().andIn("orderId", orderIds);
            List<OrderItem> orderItems = orderItemMapper.selectByCondition(fetchOrderItemCon);
            orderItems.forEach(oi -> {
                Condition proCon = new Condition(Product.class);
                proCon.createCriteria().andEqualTo("sku", oi.getSku());
                List<Product> products = this.productMapper.selectByCondition(proCon);
                String productName = "-";
                if (!CollectionUtils.isEmpty(products)) {
                    productName = StringUtils.defaultString(products.get(0).getProductName(), "-");
                }
                oi.setProductName(productName);
            });

            Map<String, List<OrderItem>> orderItemGroup = orderItems.stream().collect(Collectors.groupingBy(OrderItem::getOrderId));
            orderHeaders.forEach(orderHeader -> orderHeader.setOrderItems(orderItemGroup.get(orderHeader.getOrderId())));
        }
        BigDecimal allTotal = BigDecimal.ZERO;
        for (OrderHeader orderHeader : orderHeaders) {
            allTotal = allTotal.add(orderHeader.getTotalAmount());
        }
        ModelAndView view = new ModelAndView("agent/agent_print_unpay_order");
        view.addObject("agent", agent);
        view.addObject("orderHeaders", orderHeaders);
        view.addObject("allTotal", allTotal);
        return view;
    }
}
