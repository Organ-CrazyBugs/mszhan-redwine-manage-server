package com.mszhan.redwine.manage.server.core.excelContent.processor;

import com.mszhan.redwine.manage.server.core.excelContent.InitExcelContent;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 吴树添
 * Date: 16-6-23
 * Time: 下午6:26
 * To change this template use File | Settings | File Templates.
 */
public class WriteProcessor {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private EnumerationService enumerationService;
    private String type;

    public WriteProcessor(String type) {
//        enumerationService = (EnumerationService) ApplicationContextUtil.getCONTEXT().getBean("enumerationService");
        this.type = type;
    }

    public void process(HttpServletResponse response, List<Map<String, Object>> list) {

        response.setContentType("application/vnd.ms-excel");
        Map<String, Map<String, Object>> screenMap = InitExcelContent.gainExcelScreenMap(type);
        if (CollectionUtils.isEmpty(screenMap)) {
            throw new RuntimeException("没有找到该类型的配置信息");
        }
        Map<String, String> attrMap = InitExcelContent.gainAttrMap(type);
        if (CollectionUtils.isEmpty(screenMap)) {
            throw new RuntimeException("没有找到该标题属性类型的配置信息");
        }
        String fileName = attrMap.get("fileName") == null ? "fileName" : attrMap.get("fileName");
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
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);
        // 生成一个表格
        Sheet sheet = workbook.createSheet(fileName);
        Map<String, Integer> indexMap = new HashMap<>();
        // 声明一个画图的顶级管理器
        CreationHelper factory = workbook.getCreationHelper();

        Drawing patriarch = sheet.createDrawingPatriarch();

        ClientAnchor clientAnchor = factory.createClientAnchor();
        //设置公共样式
        CellStyle commonStyle = workbook.createCellStyle();
        //列的格式
        DataFormat format = workbook.createDataFormat();
        //列的格式是文本格式
        commonStyle.setDataFormat(format.getFormat("@"));

        int rowIndex = 0;
        int headerIndex = 0;
        int itemIndex = 0;
        Row headerRow = sheet.createRow(rowIndex);
        Row rowTitle = null;
        int index = 1;
        int valueIndex = 0;
        Iterator<String> screenIter = screenMap.keySet().iterator();
        Map<Integer, CellStyle> columnStyle = new HashMap<>();
        while (screenIter.hasNext()) {
            String key = screenIter.next();
            Map<String, Object> headerTitle = screenMap.get(key);
            Cell headerCell = headerRow.createCell(headerIndex);
            String headerText = headerTitle.get("text").toString();
            String valueKey = null;
            if (headerTitle.get("key") != null){
                valueKey = headerTitle.get("key").toString();
            }
            short size = 0;
            //设置样式
            CellStyle headerStyle = gainColorAndFontStyle(workbook, headerTitle, true);
            CellStyle itemStyle = gainColorAndFontStyle(workbook, headerTitle, false);
            if (headerTitle.get("items") != null) {
                if (index == 1) {
                    rowTitle = sheet.createRow(rowIndex + 1);
                    rowIndex++;
                    index = 0;
                }
                Map<String, Map<String, Object>> itemMapList = (Map<String, Map<String, Object>>) headerTitle.get("items");
                size = (short) itemMapList.size();
                Iterator<String> itemIter = itemMapList.keySet().iterator();
                while (itemIter.hasNext()) {
                    String itemKey = itemIter.next();
                    Map<String, Object> itemMap = itemMapList.get(itemKey);
                    if (itemMap.get("length") != null) {
                        Integer length = Integer.parseInt(itemMap.get("length").toString());
                        sheet.setColumnWidth(itemIndex, length * 256);
                    }
                    String text = itemMap.get("text").toString();
                    CellStyle itemHeaderStyle = gainColorAndFontStyle(workbook, headerTitle, true);
                    itemStyle = gainColorAndFontStyle(workbook, itemMap, false);
                    columnStyle.put(itemIndex, itemStyle);
                    indexMap.put(text, valueIndex++);
                    Cell cell = rowTitle.createCell(itemIndex);
                    cell.setCellStyle(itemHeaderStyle);
                    //批注
                    if (itemMap.get("comment") != null) {
                        String comment = itemMap.get("comment").toString();
                        writePoiComment(cell, comment, patriarch, factory, clientAnchor);
                    }
                    //下拉框
                    if (itemMap.get("select") != null) {
                        String select = itemMap.get("select").toString();
                        List<String> selectList = fetchSelect(select);
                        if (!selectList.isEmpty()) {
                            writePoiSelect(sheet, selectList, 2, itemIndex);
                        }
                    }

                    if (itemMap.get("necessary") != null && itemMap.get("necessary").toString().equals("true")) {
                        text = text + "*";
                    }
                    HSSFRichTextString rich = new HSSFRichTextString(text);
                    cell.setCellValue(rich);
                    itemIndex++;
                }
                sheet.addMergedRegion(new CellRangeAddress(
                        0, //first row (0-based)  from 行
                        0, //last row  (0-based)  to 行
                        headerIndex, //first column (0-based) from 列
                        headerIndex + (size - 1) //last column  (0-based)  to 列
                ));
            } else {
                if (headerTitle.get("length") != null) {
                    Integer length = Integer.parseInt(headerTitle.get("length").toString());
                    sheet.setColumnWidth(valueIndex, length * 256);
                }
                columnStyle.put(valueIndex, itemStyle);
                indexMap.put(valueKey, valueIndex++);


            }
            //批注
            if (headerTitle.get("comment") != null) {
                String comment = headerTitle.get("comment").toString();
                writePoiComment(headerCell, comment, patriarch, factory, clientAnchor);
            }
            //下拉框
            if (headerTitle.get("select") != null) {
                String select = headerTitle.get("select").toString();
                List<String> selectList = fetchSelect(select);
                if (!selectList.isEmpty()) {
                    writePoiSelect(sheet, selectList, 1, headerIndex);
                }
            }
            if (headerTitle.get("necessary") != null && headerTitle.get("necessary").toString().equals("true")) {
                headerText = headerText + "*";
            }
            //设置文本格式
            sheet.setDefaultColumnStyle(headerIndex, commonStyle);
            headerCell.setCellStyle(headerStyle);
            XSSFRichTextString rich = new XSSFRichTextString(headerText);
            headerCell.setCellValue(rich);
            if (size != 0) {
                headerIndex = headerIndex + (size);
            } else {
                headerIndex++;
            }
        }
        rowIndex++;

        // 遍历集合数据，产生数据行
        if (list == null || list.size() == 0) {
            try {
                workbook.write(out);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            return;
        }
        Iterator<Map<String, Object>> it = list.iterator();

        while (it.hasNext()) {
            Row dataRow = sheet.createRow(rowIndex);
            Map<String, Object> columnMap = it.next();

            for (Map.Entry<String, Object> entry : columnMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                Integer keyIndex = indexMap.get(key);
                if (keyIndex == null) {
                    continue;
                }
                Cell cell = dataRow.createCell(keyIndex);
                cell.setCellStyle(columnStyle.get(keyIndex));

                String textValue = "";
                if (value != null && StringUtils.isNotBlank(value.toString())) {
                    textValue = value.toString();
                }
                cell.setCellValue(textValue);
            }
            rowIndex++;
        }
        try {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    /**
     * 批注
     *
     * @param cell
     * @param commentText
     */
    private void writePoiComment(Cell cell, String commentText, Drawing patriarch, CreationHelper factory, ClientAnchor clientAnchor) {

//        if (StringUtils.isNotBlank(commentText)) {
//            Comment comment = patriarch.createCellComment(clientAnchor);
//            RichTextString richTextString = factory.createRichTextString(commentText);
//            comment.setString(richTextString);
//            comment.setAuthor("Apache POI");
////            comment.setColumn(2);
////            comment.setRow(2);
////            HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
////            设置注释内容
//
//            cell.setCellComment(comment);
//        }
    }

    /**
     * 下拉框枚举
     *
     * @param type
     * @return
     */
    private List<String> fetchSelect(String type) {
        List<String> selectList = new ArrayList<>();
//        switch (type) {
//            case "text":
//                return enumerationService.findText();
//            case "boolean":
//                return enumerationService.findBoolean();
//
//        }
        return selectList;
    }

    private CellStyle gainColorAndFontStyle(Workbook workbook, Map<String, Object> headerTitle, Boolean header) {
        DataFormat format = workbook.createDataFormat();
        Integer fontSize = 9;
        String fontColor = null;
        String color = null;
        if (header) {
            if (headerTitle.get("color") != null) {
                color = headerTitle.get("color").toString();
            }
            try {
                if (headerTitle.get("fontSize") != null) {
                    fontSize = Integer.parseInt(headerTitle.get("fontSize").toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                fontSize = 9;
            }
            if (headerTitle.get("fontColor") != null) {
                fontColor = headerTitle.get("fontColor").toString();
            }
        } else {
            try {
                if (headerTitle.get("itemFontSize") != null) {
                    fontSize = Integer.parseInt(headerTitle.get("itemFontSize").toString());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                fontSize = 9;
            }
            if (headerTitle.get("itemColor") != null) {
                color = headerTitle.get("itemColor").toString();
            }
            if (headerTitle.get("itemFontColor") != null) {
                fontColor = headerTitle.get("itemFontColor").toString();
            }
        }
        CellStyle style = workbook.createCellStyle();
//        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        if (StringUtils.isNotBlank(color)) {
            if (header) {
                chooseHeaderColor(color, style);
            } else {
                chooseItemColor(color, style);
            }
        }
        style.setFillPattern(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        Font font = workbook.createFont();
        if (StringUtils.isNotBlank(fontColor)) {
            chooseFontColor(fontColor, font);
        } else {
            font.setColor(HSSFColor.WHITE.index);
        }
        if (fontSize != null) {
            font.setFontHeightInPoints((short) fontSize.intValue());
        } else {
            font.setFontHeightInPoints((short) 9);
        }
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(format.getFormat("@"));
        return style;
    }

    /**
     * 下拉框
     *
     * @param sheet        excel页对象
     * @param selectValues 下拉框的值
     * @param beginColNum  第几行开始
     * @param beginRowNum  第几列
     */
    public void writePoiSelect(Sheet sheet, List<String> selectValues, int beginColNum, int beginRowNum) {
        if (selectValues == null || selectValues.size() == 0) {
            return;
        }
        int size = selectValues.size();
        String[] values = new String[size];
        for (int i = 0; i < size; i++) {
            values[i] = selectValues.get(i);
        }
        int maxRowNum = SpreadsheetVersion.EXCEL97.getLastRowIndex();//maxRowNum=65535
        System.out.println("maxRowNum:" + maxRowNum);
        int maxColNum = SpreadsheetVersion.EXCEL97.getLastColumnIndex();
        System.out.println("maxColNum:" + maxColNum);//maxColNum=255

        DataValidationHelper dvHelper = sheet.getDataValidationHelper();

        DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(values);

        CellRangeAddressList regions = new CellRangeAddressList(beginColNum, maxRowNum, beginRowNum, beginRowNum);

        DataValidation validation = dvHelper.createValidation(dvConstraint, regions);

//        // 生成下拉框内容
//        DVConstraint constraint = DVConstraint.createExplicitListConstraint(values);
//        // 绑定下拉框和作用区域
//        HSSFDataValidation data_validation = new HSSFDataValidation(regions, constraint);


        if (validation instanceof XSSFDataValidation)

        {

            validation.setSuppressDropDownArrow(true);

            validation.setShowErrorBox(true);

        } else {

// If the Datavalidation contains an instance of the HSSFDataValidation // class then 'true' should be passed to the setSuppressDropDownArrow()

// method and the call to setShowErrorBox() is not necessary. validation.setSuppressDropDownArrow(true);

        }
        // 对sheet页生效
//        sheet.addValidationData(data_validation);
        sheet.addValidationData(validation);
    }


    private void chooseHeaderColor(String color, CellStyle style) {
        switch (color) {
            case "WHITE":
                style.setFillForegroundColor(HSSFColor.WHITE.index);
                break;
            case "BLUE":
                style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
                break;
            case "YELLOW":
                style.setFillForegroundColor(HSSFColor.YELLOW.index);
                break;
            case "GREEN":
                style.setFillForegroundColor(HSSFColor.GREEN.index);
                break;
            default:
                style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
                ;
        }
    }

    private void chooseFontColor(String fontColor, Font style) {
        switch (fontColor) {
            case "WHITE":
                style.setColor(HSSFColor.WHITE.index);
                break;
            case "BLUE":
                style.setColor(HSSFColor.SKY_BLUE.index);
                break;
            case "YELLOW":
                style.setColor(HSSFColor.YELLOW.index);
                break;
            case "GREEN":
                style.setColor(HSSFColor.GREEN.index);
                break;
            case "BLACK":
                style.setColor(HSSFColor.BLACK.index);
                break;
            default:
                style.setColor(HSSFColor.BLACK.index);
        }
    }

    private void chooseItemColor(String color, CellStyle style) {
        switch (color) {
            case "WHITE":
                style.setFillForegroundColor(HSSFColor.WHITE.index);
                break;
            case "BLUE":
                style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
                break;
            case "YELLOW":
                style.setFillForegroundColor(HSSFColor.YELLOW.index);
                break;
            case "GREEN":
                style.setFillForegroundColor(HSSFColor.GREEN.index);
                break;
            default:
                style.setFillForegroundColor(HSSFColor.WHITE.index);
        }
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static void main(String[] args) throws IOException {
        //1.创建一个工作簿对象
        SXSSFWorkbook wb = new SXSSFWorkbook(100);

        //2.得到一个POI的工具类
        CreationHelper factory = wb.getCreationHelper();

        //3. 创建一个工作表
        Sheet sheet = wb.createSheet();

        //4.得到一个换图的对象
        Drawing drawing = sheet.createDrawingPatriarch();
        //5. ClientAnchor是附属在WorkSheet上的一个对象，  其固定在一个单元格的左上角和右下角.
        ClientAnchor anchor = factory.createClientAnchor();

        //6. 创建一个单元格（A1单元格）
        Cell cell0 = sheet.createRow(0).createCell(0);
        //6.1. 对这个单元格设置值
        cell0.setCellValue("Test");
        //6.2. 对这个单元格加上注解
        Comment comment0 = drawing.createCellComment(anchor);
        RichTextString str0 = factory.createRichTextString("Hello, World!");
        comment0.setString(str0);
        comment0.setAuthor("Apache POI");
        cell0.setCellComment(comment0);

        //7. 创建一个单元格（4F单元格）
        Cell cell1 = sheet.createRow(3).createCell(5);
        //7.1. 对这个单元格设置值
        cell1.setCellValue("F4");
        //7.2. 对这个单元格加上注解
        Comment comment1 = drawing.createCellComment(anchor);
        RichTextString str1 = factory.createRichTextString("Hello, World!");
        comment1.setString(str1);
        comment1.setAuthor("Apache POI");
        cell1.setCellComment(comment1);

        //8. 创建一个单元格（4F单元格）
        Cell cell2 = sheet.createRow(2).createCell(2);
        cell2.setCellValue("C3");

        Comment comment2 = drawing.createCellComment(anchor);
        RichTextString str2 = factory.createRichTextString("XSSF can set cell comments");
        //9。为注解设置字体
        Font font = wb.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 14);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(IndexedColors.RED.getIndex());
        str2.applyFont(font);

        comment2.setString(str2);
        comment2.setAuthor("Apache POI");
        comment2.setColumn(2);
        comment2.setRow(2);
        //10. 保存成Excel文件
        String fname = "D:/comments.xlsx";
        FileOutputStream out = new FileOutputStream(fname);
        wb.write(out);
        out.close();

    }
}
