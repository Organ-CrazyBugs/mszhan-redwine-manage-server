package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.core.SecurityUtils;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.InboundHistoryMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.InventoryMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.OutboundHistoryMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.InboundHistory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Inventory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OutboundHistory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryInputVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryOutputVO;
import com.mszhan.redwine.manage.server.service.InventoryService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
@Service
@Transactional
public class InventoryServiceImpl extends AbstractService<Inventory> implements InventoryService {
    @Resource
    private InventoryMapper inventoryMapper;
    @Autowired
    private InboundHistoryMapper inboundHistoryMapper;
    @Autowired
    private OutboundHistoryMapper outboundHistoryMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public void inventoryInput(InventoryInputVO vo) {
        Integer agentId = SecurityUtils.getAuthenticationUser().getAgentId();
        String agentName = SecurityUtils.getAuthenticationUser().getAgentName();

        Assert.notNull(vo, "缺少参数信息");
        Assert.hasLength(vo.getSku(), "缺少SKU信息");

        Condition fetchProCon = new Condition(Product.class);
        fetchProCon.createCriteria().andEqualTo("sku", vo.getSku());
        int proCount = this.productMapper.selectCountByCondition(fetchProCon);
        Assert.isTrue(proCount > 0, "SKU信息未找到");

        Condition inventoryCon = new Condition(Inventory.class);
        inventoryCon.createCriteria().andEqualTo("sku", vo.getSku())
                .andEqualTo("wareHouseId", vo.getWarehouseId());
        List<Inventory> inventories = this.inventoryMapper.selectByCondition(inventoryCon);

        Inventory inventory;
        if (CollectionUtils.isEmpty(inventories)) {
            inventory = new Inventory();
            inventory.setSku(vo.getSku());
            inventory.setWareHouseId(vo.getWarehouseId());
            inventory.setCreateDate(new Date());
            inventory.setUpdateDate(new Date());
            inventory.setUpdator(agentId);
            inventory.setCreator(agentId);
            inventory.setQuantity(0);

            this.inventoryMapper.insert(inventory);
        } else {
            inventory = inventories.get(0);
        }

        Inventory updateInv = new Inventory();
        updateInv.setId(inventory.getId());
        updateInv.setQuantity(inventory.getQuantity() + vo.getBottleQty());
        updateInv.setUpdateDate(new Date());
        updateInv.setUpdator(agentId);

        this.inventoryMapper.updateByPrimaryKeySelective(updateInv);

        // 插入出入库记录信息
        InboundHistory history = new InboundHistory();
        history.setCreateDate(new Date());
        history.setSku(vo.getSku());
        history.setType(vo.getInputType());
        history.setCreator(agentId);
        history.setCreatorName(agentName);
        history.setQuantity(vo.getBottleQty());
        history.setWarehouseId(vo.getWarehouseId());
        history.setRemark(vo.getRemark());

        this.inboundHistoryMapper.insert(history);
    }

    @Override
    public void inventoryOutput(InventoryOutputVO vo) {
        Integer agentId = SecurityUtils.getAuthenticationUser().getAgentId();
        String agentName = SecurityUtils.getAuthenticationUser().getAgentName();

        Condition inventoryCon = new Condition(Inventory.class);
        inventoryCon.createCriteria().andEqualTo("sku", vo.getSku())
                .andEqualTo("wareHouseId", vo.getWarehouseId());
        List<Inventory> inventories = this.inventoryMapper.selectByCondition(inventoryCon);

        Inventory inventory;
        if (CollectionUtils.isEmpty(inventories)) {
            throw BasicException.newInstance().error("仓库中SKU商品信息未找到", 500);
        } else {
            inventory = inventories.get(0);
        }

        if (inventory.getQuantity() < vo.getBottleQty()) {
            throw BasicException.newInstance().error("仓库中SKU商品库存不足，当前库存数量：" + inventory.getQuantity(), 500);
        }

        Inventory updateInv = new Inventory();
        updateInv.setId(inventory.getId());
        updateInv.setQuantity(inventory.getQuantity() - vo.getBottleQty());
        updateInv.setUpdateDate(new Date());
        updateInv.setUpdator(agentId);

        this.inventoryMapper.updateByPrimaryKeySelective(updateInv);

        // 插入出入库记录信息
        OutboundHistory history = new OutboundHistory();
        history.setCreateDate(new Date());
        history.setSku(vo.getSku());
        history.setType(vo.getInputType());
        history.setCreator(agentId);
        history.setCreatorName(agentName);
        history.setQuantity(vo.getBottleQty());
        history.setWarehouseId(vo.getWarehouseId());
        history.setRemark(vo.getRemark());

        this.outboundHistoryMapper.insert(history);
    }
}
