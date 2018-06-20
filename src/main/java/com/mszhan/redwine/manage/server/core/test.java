package com.mszhan.redwine.manage.server.core;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by god on 2018/6/20.
 */
public class test {


    public static void main(String[] aa) throws IOException {

        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);

        //导出文件
        File file = new File("F:\\test2.xlsx");

        long start = System.currentTimeMillis();
        Sheet sheet = workbook.createSheet("sheet");
        sheet.setDefaultColumnWidth(20);
//            生成标题
        Map<Integer, Object> firstTitles = new HashMap<>();
        firstTitles.put(0, "深圳市汇纳酒业有限公司");
        genCompanySheetHead(workbook,sheet, 0, firstTitles, (short)16);

        Map<Integer, Object> twoTitles = new HashMap<>();
        twoTitles.put(0, "2017年11月份出库明细表");
        genCompanySheetHead( workbook, sheet, 1, twoTitles, (short) 14);
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)  from 行
                0, //last row  (0-based)  to 行
                0, //first column (0-based) from 列
                8 //last column  (0-based)  to 列
        ));
        sheet.addMergedRegion(new CellRangeAddress(
                1, //first row (0-based)  from 行
                1, //last row  (0-based)  to 行
                0, //first column (0-based) from 列
                8 //last column  (0-based)  to 列
        ));
        Map<String, Integer> indexKeyMap = new HashMap<>();
        Map<Integer, Object> dataMap = new LinkedHashMap<>();
        dataMap.put(0, "序号");
        dataMap.put(1, "日期");
        dataMap.put(2, "客户");
        dataMap.put(3, "金额");
        dataMap.put(4, "品名");
        dataMap.put(5, "数量");
        dataMap.put(6, "单价");
        genCompanySheetHead(workbook, sheet, 2, dataMap, (short)13);
        indexKeyMap.put("index", 0);
        indexKeyMap.put("date", 1);
        indexKeyMap.put("client", 2);
        indexKeyMap.put("total", 3);
        indexKeyMap.put("productName", 4);
        indexKeyMap.put("qty", 5);
        indexKeyMap.put("cost", 6);

        Map<String, Map<String, Object>> dataListMap = new HashMap<>();
        Map<String, Object> resultMap = new HashMap<>();
        String key = String.format("%s|%s", "11月12日", "小白");
        resultMap.put("data", "11月12日");
        resultMap.put("client", "小白");
        resultMap.put("total", "400");
        List<Map<String, Object>> dataList = new ArrayList<>();
        resultMap.put("index", "0");
        resultMap.put("list", dataList);
        Map<String, Object> data = new HashMap<>();
        data.put("productName", "测试产品1");
        data.put("qty", "200瓶(12箱0瓶)");
        data.put("cost", "1.00");
        Map<String, Object> data1 = new HashMap<>();
        data1.put("productName", "测试产品1");
        data1.put("qty", "200瓶(12箱0瓶)");
        data1.put("cost", "1.00");

        Map<String, Object> data2 = new HashMap<>();
        data2.put("productName", "测试产品1");
        data2.put("qty", "200瓶(12箱0瓶)");
        data2.put("cost", "1.00");
        dataList.add(data);
        dataList.add(data1);
        dataList.add(data2);
        dataListMap.put(key, resultMap);

        int rowNum = 3;
        for (Map.Entry<String, Map<String, Object>> da : dataListMap.entrySet()) {
            Map<String, Object> resMap = da.getValue();
            String va = da.getKey();
            List<Map<String, Object>> list = (List<Map<String, Object>>) resMap.get("list");
            int listSize = list.size();
            Row row = sheet.createRow(rowNum);
            int k = 0;
            createCell(workbook , row, k, resMap.get("index"));
            sheet.addMergedRegion(new CellRangeAddress(
                    rowNum, //first row (0-based)  from 行
                    rowNum + listSize - 1, //last row  (0-based)  to 行
                    k, //first column (0-based) from 列
                    k //last column  (0-based)  to 列
            ));
            k++;
            createCell(workbook, row, k, resMap.get("data"));
            sheet.addMergedRegion(new CellRangeAddress(
                    rowNum, //first row (0-based)  from 行
                    rowNum + listSize - 1, //last row  (0-based)  to 行
                    k, //first column (0-based) from 列
                    k //last column  (0-based)  to 列
            ));
            k++;
            createCell(workbook, row, k, resMap.get("client"));
            sheet.addMergedRegion(new CellRangeAddress(
                    rowNum, //first row (0-based)  from 行
                    rowNum + listSize - 1, //last row  (0-based)  to 行
                    k, //first column (0-based) from 列
                    k //last column  (0-based)  to 列
            ));
            k++;
            createCell(workbook, row, k, resMap.get("total"));
            sheet.addMergedRegion(new CellRangeAddress(
                    rowNum, //first row (0-based)  from 行
                    rowNum + listSize - 1, //last row  (0-based)  to 行
                    k, //first column (0-based) from 列
                    k //last column  (0-based)  to 列
            ));

            k++;
            for (int i = 0; i < listSize; i++) {
                Row newRow = null;
                if (i == 0) {
                    newRow = row;
                } else {
                    newRow = sheet.createRow(rowNum + i);
                }
                Map<String, Object> listData = list.get(i);
                createCell(workbook, newRow, k, listData.get("productName"));
                createCell(workbook, newRow, k + 1, listData.get("qty"));
                createCell(workbook, newRow, k + 2, listData.get("cost"));
            }

            rowNum++;
        }


        FileOutputStream out = new FileOutputStream(file);
        workbook.write(out);

        System.out.println((System.currentTimeMillis() - start));
        out.close();
    }


    /**
     * 写入标题
     *
     * @param sheet
     * @param rowNum 第几行的行号
     * @param values key:第几列的列号  value:值
     */
    public static void genSheetHead(Sheet sheet, SXSSFWorkbook wb,int rowNum, Map<Integer, Object> values) {

        Row row = sheet.createRow(rowNum);
        for (Integer cellNum : values.keySet()) {
            CellStyle cellStyle = wb.createCellStyle();
            Font font = wb.createFont();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            font.setFontName("黑体");
            font.setFontHeightInPoints((short)12);//设置字体大小
            cellStyle.setFont(font);
            Cell cell = row.createCell(cellNum);
            cell.setCellStyle(cellStyle);
            Object value = values.get(cellNum);
            generateValue(value, cell);
        }
    }

    public static void genCompanySheetHead(SXSSFWorkbook wb,Sheet sheet, int rowNum, Map<Integer, Object> values, short fontsize) {
        Row row = sheet.createRow(rowNum);
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        font.setFontName("黑体");
        font.setFontHeightInPoints(fontsize);//设置字体大小
        cellStyle.setFont(font);
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
    public static void createCell(SXSSFWorkbook wb, Row row, int cellNum, Object value) {
        Cell cell = row.createCell(cellNum);
        CellStyle cellStyle = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        font.setFontHeightInPoints((short)12);//设置字体大小
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        generateValue(value, cell);
    }

    public static void createCellMerge(Sheet sheet, Row row, int cellNum, Object value, Integer mergeSize, Integer totalIndex) {
        Cell cell = row.createCell(cellNum);
        generateValue(value, cell);
//        sheet.addMergedRegion(new CellRangeAddress(
//                totalIndex + , //first row (0-based)  from 行
//                totalIndex, //last row  (0-based)  to 行
//                0, //first column (0-based) from 列
//                8 //last column  (0-based)  to 列
//        ))
    }

    private static void generateValue(Object value, Cell cell) {
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
        }
    }

}
