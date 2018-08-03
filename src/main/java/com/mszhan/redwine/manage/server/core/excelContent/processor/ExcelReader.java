package com.mszhan.redwine.manage.server.core.excelContent.processor;

/**
 * Created with IntelliJ IDEA.
 * User: 吴树添
 * Date: 16-7-1
 * Time: 下午3:01
 * To change this template use File | Settings | File Templates.
 */

//import com.aukey.aries.base.websocket.WebSocketServiceHandler;
//import com.aukey.aries.common.ApplicationContextUtil;
//import com.aukey.aries.security.SecurityAuthenticationSuccessHandler;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

public abstract class ExcelReader extends DefaultHandler {

    private SharedStringsTable sst;
    private String lastContents;
    private boolean nextIsString;

    private int sheetIndex = -1;
    private int titleLength = 1;
    private List<String> rowlist = new ArrayList<String>();
    private int curRow = 0;     //当前行
    private int curCol = 0;     //当前列索引
    private int preCol = 0;     //上一列列索引
    private int titleRow = 0;   //标题行，一般情况下为0
    private int rowsize = 0;    //列数
    private int batchNumber = 1000;  //批量数量
    private boolean lastSheet = false;

    private InputStream inputStream;

    private int countNum = 0; //批量累加数量

    private List<Map<String, Object>> resultList = new ArrayList<>();
    private List<String> errorList = new ArrayList<>();

    private List<String> warnList = new ArrayList<>();

    private Map<Integer, String> indexMap = new HashMap<>();

    private Map<String, String> contentAttrMap = null;

//    private WebSocketServiceHandler webSocketServiceHandler;

//    private String signature;

    //excel记录行操作方法，以行索引和行元素列表为参数，对一行元素进行操作，元素为String类型
//  public abstract void optRows(int curRow, List<String> rowlist) throws SQLException ;

    //excel记录行操作方法，以sheet索引，行索引和行元素列表为参数，对sheet的一行元素进行操作，元素为String类型
    public abstract void optRows(List<Map<String, Object>> listMap, Map<Integer, String> indexMap);

    public abstract void complexOptRows(List<Map<String, Object>> mapList, Map<Integer, String> indexMap, Boolean over);

    //初始化
    public void init(Integer titleLength, Map<String, String> contentAttrMap, InputStream inputStream, HttpServletRequest request) {
        this.titleLength = titleLength;
        this.contentAttrMap = contentAttrMap;
        this.inputStream = inputStream;
//        webSocketServiceHandler = ApplicationContextUtil.getCONTEXT().getBean(WebSocketServiceHandler.class);
//        this.signature = (String) request.getSession(false).getAttribute(SecurityAuthenticationSuccessHandler.SIGNATURE_UUID);

    }

    //只遍历一个sheet，其中sheetId为要遍历的sheet索引，从1开始，1-3
    public void processOneSheet(String filename,int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        // rId2 found by processing the Workbook
        // 根据 rId# 或 rSheet# 查找sheet
        InputStream sheet2 = r.getSheet("rId"+sheetId);
        sheetIndex++;
        InputSource sheetSource = new InputSource(sheet2);

        parser.parse(sheetSource);
        sheet2.close();
    }

    /**
     * 遍历 excel 文件
     */
    public void process(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        Iterator<InputStream> sheets = r.getSheetsData();

        while (sheets.hasNext()) {
            curRow = 0;
            sheetIndex++;
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }


    /**
     * 遍历 excel 文件
     */
    public void process() throws Exception {
        OPCPackage pkg = OPCPackage.open(inputStream);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        Iterator<InputStream> sheets = r.getSheetsData();

        while (sheets.hasNext()) {
            curRow = 1;
            sheetIndex++;
            InputStream sheet = sheets.next();
            if (!sheets.hasNext()){
                lastSheet = true;
            }
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();

        }
    }

    /**
     * 遍历 excel 文件
     */
    public void processOneSheet() throws Exception {
        OPCPackage pkg = OPCPackage.open(inputStream);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        InputStream sheet2 = r.getSheet("rId"+ 1);
        lastSheet = true;
        sheetIndex++;
        curRow = 1;
        InputSource sheetSource = new InputSource(sheet2);
        try {
//            webSocketServiceHandler.sendMsgToUser(signature, String.format("正在解析数据......"));
        } catch(Exception ex){
            ex.printStackTrace();
        }
        parser.parse(sheetSource);

        sheet2.close();

    }

    public XMLReader fetchSheetParser(SharedStringsTable sst)
            throws SAXException {
        XMLReader parser = XMLReaderFactory
                .createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        // c => 单元格
        if (name.equals("c")) {
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            String cellType = attributes.getValue("t");
            String rowStr = attributes.getValue("r");
            curCol = this.getRowIndex(rowStr);
            if (cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }
        }
        // 置空
        lastContents = "";
    }

    public void endElement(String uri, String localName, String name)
             {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        try {
        if (nextIsString) {
            try {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx))
                        .toString();
            } catch (Exception e) {

            }
        }

        // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
        // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        if (name.equals("v")) {
            String value = StringUtils.trimToNull(lastContents);

            int cols = curCol-preCol;
            if (cols > 1){
                for (int i = 0;i < cols-1;i++){
                    rowlist.add(preCol, "");
                }
            }
            preCol = curCol;
            rowlist.add(curCol - 1, value);
        }else {
            //如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                int tmpCols = rowlist.size();
                if(curRow > this.titleRow && tmpCols < this.rowsize){
                    for (int i = 0 ; i < this.rowsize - tmpCols; i++){
                        rowlist.add(rowlist.size(), "");
                    }
                }
                System.out.println(rowlist);
                    Map<String, Object> result = new HashMap<>();
                    result.put("rowList", rowlist.toArray());
                    result.put("curRow", curRow);
                    result.put("sheetIndex", sheetIndex);
//                    int rowSize = rowlist.size();
//                    if (rowSize == 0){
//                        if (resultList.size() != 0){
//                            if (contentAttrMap.get("complex") != null) {
//                                complexOptRows(resultList, indexMap, true);
//                            } else {
//                                optRows(resultList, indexMap);
//                            }
//                            resultList.clear();
//                        }
//                        countNum = 0;
//                    } else {
                    if (curRow == titleLength && sheetIndex >= 1){
                        rowlist.clear();
                        curRow ++;
                        return ;
                    }
                    if(curRow == titleLength && sheetIndex == 0) {
                        int size = rowlist.size();
                        for (int i = 0;i < size; i ++) {
                            String value = rowlist.get(i);
                            if (StringUtils.isBlank(value)) {
                                return;
                            }
                            indexMap.put(i, StringUtils.trimToNull(value).replaceAll("\\*", ""));
                        }
                        rowlist.clear();
                        countNum ++;
                        curRow ++;
                        return ;
                    }
                        resultList.add(result);
                        countNum ++;
                        if (countNum == batchNumber){
                            if (contentAttrMap.get("complex") != null) {
                                try {
//                                    webSocketServiceHandler.sendMsgToUser(signature, String.format("正在保存数据......"));
                                } catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                complexOptRows(resultList, indexMap, false);
                            } else {
                                try {
//                                    webSocketServiceHandler.sendMsgToUser(signature, String.format("正在保存数据......"));
                                } catch(Exception ex){
                                    ex.printStackTrace();
                                }
                                optRows(resultList, indexMap);
                            }
                            resultList.clear();
                            countNum = 0;
                        }
                rowlist.clear();
                curRow++;
                curCol = 0;
                preCol = 0;
            }
        }
        } catch (ProcessorException ex){
            ex.printStackTrace();
            throw new ProcessorException(ex.getMessage());
        } catch(Exception ex){
            ex.printStackTrace();
            throw new ProcessorException(ex.getMessage());
        }

    }

    public void endDocument() throws SAXException {
        if (CollectionUtils.isNotEmpty(resultList)) {
            if (contentAttrMap.get("complex") != null) {
                if (lastSheet) {
                    try {
//                        webSocketServiceHandler.sendMsgToUser(signature, String.format("正在保存数据......"));
                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                    complexOptRows(resultList, indexMap, true);
                    resultList.clear();
                    countNum = 0;
                }
            } else {
                if (lastSheet) {
                    try {
//                        webSocketServiceHandler.sendMsgToUser(signature, String.format("正在保存数据......"));
                    } catch(Exception ex){
                        ex.printStackTrace();
                    }
                    optRows(resultList, indexMap);
                    resultList.clear();
                    countNum = 0;
                } else {
                    if (countNum == batchNumber) {
                        optRows(resultList, indexMap);
                        resultList.clear();
                        countNum = 0;
                    }
                }
            }

        }
        try {
//            webSocketServiceHandler.sendMsgToUser(signature, String.format("解析完成！！"));
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        //得到单元格内容的值
        lastContents += new String(ch, start, length);
    }

    //得到列索引，每一列c元素的r属性构成为字母加数字的形式，字母组合为列索引，数字组合为行索引，
    //如AB45,表示为第（A-A+1）*26+（B-A+1）*26列，45行
    public int getRowIndex(String rowStr){
        rowStr = rowStr.replaceAll("[^A-Z]", "");
        byte[] rowAbc = rowStr.getBytes();
        int len = rowAbc.length;
        float num = 0;
        for (int i=0;i<len;i++){
            num += (rowAbc[i]-'A'+1)*Math.pow(26,len-i-1 );
        }
        return (int) num;
    }

//    public class ProcessorException extends RuntimeException {
//        public ProcessorException(String msg){
//            super(msg);
//            errorList.add(msg);
//        }
//
//        public ProcessorException(){
//            super("自定义异常ProcessorException");
//        }
//    }

    public List<Map<String, Object>> getResultList() {
        return resultList;
    }

    public void setResultList(List<Map<String, Object>> resultList) {
        this.resultList = resultList;
    }

    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public int getTitleRow() {
        return titleRow;
    }

    public void setTitleRow(int titleRow) {
        this.titleRow = titleRow;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

//    public String getSignature() {
//        return signature;
//    }


//    public WebSocketServiceHandler getWebSocketServiceHandler() {
//        return webSocketServiceHandler;
//    }

        public List<String> getWarnList() {
            return warnList;
        }
    public void addWarnList(String str){
        warnList.add(str);
    }

}
