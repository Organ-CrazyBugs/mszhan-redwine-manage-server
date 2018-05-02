package com.mszhan.redwine.manage.server.web.page;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.SecurityUtils;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.*;
import com.mszhan.redwine.manage.server.enums.AgentTypeEnum;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.*;
import com.mszhan.redwine.manage.server.service.OrderHeaderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import tk.mybatis.mapper.entity.Condition;

import java.util.*;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 10:23 2018/4/13
 */
@Controller
public class OrderPageController {
    @Autowired
    private AgentsMapper agentsMapper;
    @Autowired
    private OrderHeaderMapper orderHeaderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 订单打印出库单
     */
    @PostMapping("/page/order/print_output")
    public ModelAndView orderPrintOutputOrder(Requests requests){
        List<String> orderIds = requests.getStringArray("orderIds", ",", new ArrayList<>());
        List<OrderHeader> orderHeaders = new ArrayList<>();
        if (!orderIds.isEmpty()) {
            Condition ohCon = new Condition(OrderHeader.class);
            ohCon.createCriteria().andEqualTo("status", "SHIPPED")
                    .andIn("orderId", orderIds);
            orderHeaders = this.orderHeaderMapper.selectByCondition(ohCon);
            orderHeaders.forEach(oh -> {
                Condition oiCon = new Condition(OrderItem.class);
                oiCon.createCriteria().andEqualTo("orderId", oh.getOrderId());
                oh.setOrderItems(this.orderItemMapper.selectByCondition(oiCon));

                oh.getOrderItems().forEach(oi -> {
                    Condition proCon = new Condition(Product.class);
                    proCon.createCriteria().andEqualTo("sku", oi.getSku());
                    List<Product> products = this.productMapper.selectByCondition(proCon);
                    String productName = "";
                    if (!CollectionUtils.isEmpty(products)) {
                        productName = StringUtils.defaultString(products.get(0).getProductName());
                    }
                    oi.setProductName(productName);
                });
            });
        }
        ModelAndView view = new ModelAndView("order/order_output_warehouse");
        view.addObject("orderHeaders", orderHeaders);
        return view;
    }

    @RequestMapping("/page/order/order_list")
    public ModelAndView index(){
        String agentType = SecurityUtils.getAuthenticationUser().getAgentType();
        Integer agentId = SecurityUtils.getAuthenticationUser().getAgentId();
        List<Map<String, Object>> agents;
        if (AgentTypeEnum.ADMIN.toString().equals(agentType)) {
            agents = agentsMapper.fetchAllAgents();
        } else {
            Agents agent = this.agentsMapper.selectByPrimaryKey(agentId);
            Map<String, Object> map = new HashMap<>();
            map.put("id", agent.getId());
            map.put("name", agent.getName());
            map.put("phone", agent.getPhone());
            agents = Arrays.asList(map);
        }
        Condition warehouseCon = new Condition(Warehouse.class);
        warehouseCon.createCriteria().andEqualTo("status", "ENABLED");
        List<Warehouse> warehouses = this.warehouseMapper.selectByCondition(warehouseCon);

        ModelAndView view = new ModelAndView("order/order_list");
        view.addObject("agentType", agentType);
        view.addObject("agents", agents);
        view.addObject("warehouses", warehouses);
        return view;
    }

}
