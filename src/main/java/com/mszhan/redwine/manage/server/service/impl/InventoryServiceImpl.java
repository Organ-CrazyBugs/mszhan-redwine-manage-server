package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.SecurityUtils;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.InboundHistoryMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.InventoryMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.OutboundHistoryMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.InboundHistory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Inventory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OutboundHistory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.InventoryQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.FetchInventoryVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryInputVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryOutputVO;
import com.mszhan.redwine.manage.server.service.InventoryService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;


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

    @Override
    public void leadOutOutboundDetail(InventoryQuery query, HttpServletResponse response) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        response.setContentType("application/vnd.ms-excel");
        String fileName = "inventory_outbound";
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
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("黑体");
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        font.setFontHeightInPoints((short)12);//设置字体大小
        cellStyle.setFont(font);

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleFont.setFontName("黑体");
        titleFont.setFontHeightInPoints((short)13);//设置字体大小
        titleStyle.setFont(titleFont);
        int rowNum = 0;

        Map<Integer, Object> titleMap = new LinkedHashMap<>();
        titleMap.put(0, "仓库");
        titleMap.put(1, "操作日期");
        titleMap.put(2, "类型");
        titleMap.put(3, "产品名称");
        titleMap.put(4, "条码");
        titleMap.put(5, "数量");
        titleMap.put(6, "操作人");
        titleMap.put(7, "说明");
        Map<String, Integer> indexMap = new HashMap<>();
        indexMap.put("warehouseName", 0);
        indexMap.put("createDate", 1);
        indexMap.put("type", 2);
        indexMap.put("productName", 3);
        indexMap.put("sku", 4);
        indexMap.put("quantity", 5);
        indexMap.put("creatorName", 6);
        indexMap.put("remark", 7);

        genCompanySheetHead(sheet, titleStyle, rowNum ++, titleMap);
        try {
            query.setLimit(5000);
            query.setPageNumber(1);
            while (true){
                List<Map<String, Object>> outboundHistoryList = outboundHistoryMapper.leadOutOutboundDetail(query);
                if (CollectionUtils.isEmpty(outboundHistoryList)){
                    break;
                }
                for (Map<String, Object> b : outboundHistoryList){
                    Row row = sheet.createRow(rowNum);
                    switch(b.get("type").toString()){
                        case "ALLOT_OUTPUT" : {b.put("type", "调拨出库"); break;}
                        case "OTHER_OUTPUT" : {b.put("type", "其他出库"); break;}
                        case "SALES_OUTBOUND" : {b.put("type", "销售出库"); break;}
                    }
                    Timestamp createDate = (Timestamp) b.get("createDate");
                    createCell(cellStyle, row, indexMap.get("warehouseName"), b.get("warehouseName"));
                    createCell(cellStyle, row, indexMap.get("createDate"), sdf.format(new Date(createDate.getTime())));
                    createCell(cellStyle, row, indexMap.get("type"), b.get("type"));
                    createCell(cellStyle, row, indexMap.get("productName"), b.get("productName"));
                    createCell(cellStyle, row, indexMap.get("sku"), b.get("sku"));
                    createCell(cellStyle, row, indexMap.get("quantity"), b.get("quantity"));
                    createCell(cellStyle, row, indexMap.get("creatorName"), b.get("createName"));
                    createCell(cellStyle, row, indexMap.get("remark"), b.get("remark"));
                    rowNum ++;
                }
                query.setPageNumber(query.getPageNumber() + 1);

            }


        } catch(Exception ex){
                ex.printStackTrace();
        }

        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void leadOutInboundDetail(InventoryQuery query, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        response.setContentType("application/vnd.ms-excel");
        String fileName = "inventory_inbound";
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
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("黑体");
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        font.setFontHeightInPoints((short)12);//设置字体大小
        cellStyle.setFont(font);

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleFont.setFontName("黑体");
        titleFont.setFontHeightInPoints((short)13);//设置字体大小
        titleStyle.setFont(titleFont);

        int rowNum = 0;

        Map<Integer, Object> titleMap = new LinkedHashMap<>();
        titleMap.put(0, "仓库");
        titleMap.put(1, "操作日期");
        titleMap.put(2, "类型");
        titleMap.put(3, "产品名称");
        titleMap.put(4, "条码");
        titleMap.put(5, "数量");
        titleMap.put(6, "操作人");
        titleMap.put(7, "说明");
        Map<String, Integer> indexMap = new HashMap<>();
        indexMap.put("warehouseName", 0);
        indexMap.put("createDate", 1);
        indexMap.put("type", 2);
        indexMap.put("productName", 3);
        indexMap.put("sku", 4);
        indexMap.put("quantity", 5);
        indexMap.put("creatorName", 6);
        indexMap.put("remark", 7);

        genCompanySheetHead(sheet, titleStyle, rowNum ++, titleMap);
        try {
            query.setLimit(5000);
            query.setPageNumber(1);
            while (true){
                List<Map<String, Object>> outboundHistoryList = inboundHistoryMapper.leadOutInboundDetail(query);
                if (CollectionUtils.isEmpty(outboundHistoryList)){
                    break;
                }
                for (Map<String, Object> b : outboundHistoryList){
                    Row row = sheet.createRow(rowNum);
                    switch(b.get("type").toString()){
                        case "PURCHASE_INPUT" : {b.put("type", "采购入库"); break;}
                        case "ALLOT_INPUT" : {b.put("type", "调拨入库"); break;}
                        case "OTHER_INPUT" : {b.put("type", "其他入库"); break;}
                    }
                    Timestamp createDate = (Timestamp) b.get("createDate");
                    createCell(cellStyle, row, indexMap.get("warehouseName"), b.get("warehouseName").toString());
                    createCell(cellStyle, row, indexMap.get("createDate"), sdf.format(new Date(createDate.getTime())));
                    createCell(cellStyle, row, indexMap.get("type"), b.get("type").toString());
                    createCell(cellStyle, row, indexMap.get("productName"), b.get("productName").toString());
                    createCell(cellStyle, row, indexMap.get("sku"), b.get("sku").toString());
                    createCell(cellStyle, row, indexMap.get("quantity"), b.get("quantity"));
                    createCell(cellStyle, row, indexMap.get("creatorName"), b.get("createName"));
                    createCell(cellStyle, row, indexMap.get("remark"), b.get("remark"));
                    rowNum ++;
                }
                query.setPageNumber(query.getPageNumber() + 1);
            }

        } catch(Exception ex){
            ex.printStackTrace();
        }

        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leadOutInventory(InventoryQuery query, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        response.setContentType("application/vnd.ms-excel");
        String fileName = "inventory_inbound";
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
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("黑体");
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        font.setFontHeightInPoints((short)12);//设置字体大小
        cellStyle.setFont(font);

        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleFont.setFontName("黑体");
        titleFont.setFontHeightInPoints((short)13);//设置字体大小
        titleStyle.setFont(titleFont);

        int rowNum = 0;

        Map<Integer, Object> titleMap = new LinkedHashMap<>();
        titleMap.put(0, "产品名称");
        titleMap.put(1, "所属仓库");
        titleMap.put(2, "库存数量");
        titleMap.put(3, "产品品牌");
        titleMap.put(4, "产品条码");
        titleMap.put(5, "最近更新时间");
        Map<String, Integer> indexMap = new HashMap<>();
        indexMap.put("productName", 0);
        indexMap.put("warehouseName", 1);
        indexMap.put("quantityDes", 2);
        indexMap.put("brandName", 3);
        indexMap.put("sku", 4);
        indexMap.put("updateDate", 5);

        genCompanySheetHead(sheet, titleStyle, rowNum ++, titleMap);

        List<FetchInventoryVO> queryAll = inventoryMapper.queryForLeadOut(query);
        Integer allTotal = 0;
        for (FetchInventoryVO v : queryAll){
            Row row = sheet.createRow(rowNum++);
            String qtt = "";
            String wineType = v.getWineType();
            Integer qty = v.getQuantity();
            String unit = v.getUnit();
            allTotal = allTotal + qty;
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
            createCell(cellStyle, row, indexMap.get("productName"), v.getProductName());
            createCell(cellStyle, row, indexMap.get("warehouseName"), v.getWarehouseName());
            createCell(cellStyle, row, indexMap.get("quantityDes"), v.getQuantityDes());
            createCell(cellStyle, row, indexMap.get("brandName"), v.getBrandName());
            createCell(cellStyle, row, indexMap.get("sku"), v.getSku());
            createCell(cellStyle, row, indexMap.get("updateDate"), sdf.format(v.getUpdateDate()));
        }
        Row row = sheet.createRow(rowNum++);
        createCell(cellStyle, row, 0, "合计");
        createCell(cellStyle, row, indexMap.get("quantityDes"), allTotal);

        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public  void genCompanySheetHead(Sheet sheet, CellStyle cellStyle,  int rowNum, Map<Integer, Object> values) {
        Row row = sheet.createRow(rowNum);
        for (Integer cellNum : values.keySet()) {
            Cell cell = row.createCell(cellNum);
            cell.setCellStyle(cellStyle);
            Object value = values.get(cellNum);
            generateValue(value, cell);

        }
    }

    /**
     * @param row
     * @param cellNum 第几列的列号
     * @param value   值
     */
    public  void createCell(CellStyle cellStyle, Row row, int cellNum, Object value) {
        Cell cell = row.createCell(cellNum);
        cell.setCellStyle(cellStyle);
        generateValue(value, cell);
    }

    private  void generateValue(Object value, Cell cell) {
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
        } else if (value instanceof RichTextString) {
            cell.setCellValue((RichTextString) value);
        } else if (value instanceof BigDecimal){
            cell.setCellValue(value.toString());
        } else if (value instanceof Integer){
            cell.setCellValue(value.toString());
        }
    }
}
