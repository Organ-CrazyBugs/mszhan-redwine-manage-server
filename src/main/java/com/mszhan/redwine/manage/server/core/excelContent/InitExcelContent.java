package com.mszhan.redwine.manage.server.core.excelContent;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 吴树添
 * Date: 16-6-17
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
public class InitExcelContent {
    private static final Logger logger = LoggerFactory.getLogger(InitExcelContent.class);
    public static Map<String, Map<String, Map<String, Object>>> excelScreenMap = null;
    public static Map<String, Map<String, String>> excelNameMap = null;
    public static Map<String, Integer> lengthMap = null;
    public static Map<String, Map<String, String>>  contentAttributeMap = null;



    public static void initScreen() throws Exception {
        excelNameMap = new HashMap<>();
        excelScreenMap = new HashMap<>();
        lengthMap = new HashMap<>();
        contentAttributeMap = new HashMap<>();
//        File filePage = new File(InitExcelContent.class.getResource("").getPath() + "excelContentXml");
        InputStream is=InitExcelContent.class.getResourceAsStream("excelContentXml/content.xml");
//        logger.info("##########初始化3" + filePage.getPath());
//        File[] files = filePage.listFiles();
//        logger.info("##########初始化4 size" + files.length);
//        for (File file : files) {
//            if (!file.isFile() || !file.getAbsolutePath().endsWith(".xml")){
//                continue;
//            }
            SAXReader reader = new SAXReader();
            Document document = null;
            try {
                document = reader.read(is);
            } catch (DocumentException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            Element rootElement = document.getRootElement();
            List<Element> element = rootElement.elements();
            for (Element screenElement : element) {
                Map<String, String> nameMap = new LinkedHashMap<>();
                Map<String, String> attrMap = new HashMap<>();
                Attribute screenName = screenElement.attribute("name");
                Attribute fileNameAttr = screenElement.attribute("fileName");
                Attribute complexAttr = screenElement.attribute("complex");
                if (screenName == null || screenName.getText() == null){
                    throw new Exception("存在excel配置name属性名称为空");
                }
                if (fileNameAttr == null || fileNameAttr.getText() == null){
                    throw new Exception("存在excel配置fileName属性为空");
                }
                if (complexAttr != null && complexAttr.getText() != null){
                    attrMap.put("complex", complexAttr.getText());
                }
                attrMap.put("fileName", fileNameAttr.getText());
                attrMap.put("name", screenName.getText());
                contentAttributeMap.put(screenName.getText(), attrMap);
                List<Element> headerTitles = screenElement.elements("headerTitle");
                Map<String, Map<String, Object>> headerTitleList = new LinkedHashMap<>();
                for (Element headerTitle : headerTitles){
                    Map<String, Object> headerMap = new HashMap<>();
                    List<Element> itemList =  headerTitle.elements("itemTitle");
                    Map<String, Map<String, Object>> itemMapList = new LinkedHashMap<>();
                    if (!itemList.isEmpty()){
                        lengthMap.put(screenName.getText(), 2);
                        for (Element item : itemList){
                            Map<String, Object> itemHeaderMap = new HashMap<>();
                            gainHeaderItemMap(item,itemHeaderMap ,itemMapList, nameMap, screenName.getText(), false);
                        }
                        headerMap.put("items", itemMapList);

                    } else {
                        lengthMap.put(screenName.getText(), 1);
                    }
                    if (itemList.isEmpty()){
                        gainHeaderItemMap(headerTitle,headerMap, headerTitleList, nameMap, screenName.getText(), false);
                    } else {
                        gainHeaderItemMap(headerTitle,headerMap, headerTitleList, nameMap, screenName.getText(), true);
                    }
                }
                excelScreenMap.put(screenName.getText(), headerTitleList);
                excelNameMap.put(screenName.getText(), nameMap);
            }
//        }
    }
//    public static void main(String[] aa){
//        try {
//            getResource();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
    private static void getResource() throws IOException {
        //返回读取指定资源的输入流
        InputStream is=InitExcelContent.class.getResourceAsStream("excelContentXml/content.xml");
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String s="";
        while((s=br.readLine())!=null)
            System.out.println(s);
    }

    private static void gainHeaderItemMap(Element headerTitle,Map<String, Object> headerMap, Map<String, Map<String, Object>> headMap, Map<String, String> nameMap, String screenName, Boolean hasItem) throws Exception {
        Attribute text = headerTitle.attribute("text");
        Attribute key = headerTitle.attribute("key");
        Attribute fontSize = headerTitle.attribute("font-size");
        Attribute fontColor = headerTitle.attribute("font-color");
        Attribute color = headerTitle.attribute("color");
        Attribute necessary = headerTitle.attribute("necessary");
        Attribute itemColor = headerTitle.attribute("item-color");
        Attribute itemSize = headerTitle.attribute("item-font-size");
        Attribute itemFontColor = headerTitle.attribute("item-font-color");
        Attribute comment = headerTitle.attribute("comment");
        Attribute select = headerTitle.attribute("select");
        Attribute type = headerTitle.attribute("type");
        Attribute length = headerTitle.attribute("length");
        Attribute format = headerTitle.attribute("format");

        if (text == null || StringUtils.isBlank(text.getText())) {
            throw new Exception(String.format("[%s]的text属性不能为空", screenName));
        }
            headerMap.put("text", text.getText());
        if (headMap.containsKey(text.getText())){
            throw new Exception(String.format("[%s]的text属性[%s]重复", screenName, text.getText()));
        }

        if ( !hasItem  && (key == null || StringUtils.isBlank(key.getText()))) {
            throw new Exception(String.format("[%s]的key属性不能为空", screenName));
        }
        if (key != null && StringUtils.isNotBlank(key.getText())) {
            headerMap.put("key", key.getText());
        }

        if (!hasItem  && (type == null || StringUtils.isBlank(type.getText()))) {
            throw new Exception(String.format("[%s]的type属性不能为空", screenName));
        }
        if (type != null && StringUtils.isNotBlank(type.getText())){
            headerMap.put("type", type.getText());
        }
        if (fontSize != null && StringUtils.isNotBlank(fontSize.getText())){
            headerMap.put("fontSize", fontSize.getText());
        }

        if (necessary != null && StringUtils.isNotBlank(necessary.getText())){
            headerMap.put("necessary", Boolean.valueOf(necessary.getText()));
        } else {
            headerMap.put("necessary", false);
        }

        if (color != null && StringUtils.isNotBlank(color.getText())){
            headerMap.put("color", color.getText());
        }

        if (itemSize != null && StringUtils.isNotBlank(itemSize.getText())){
            headerMap.put("itemFontSize", itemSize.getText());
        }

        if (comment != null && StringUtils.isNotBlank(comment.getText())){
            headerMap.put("comment", comment.getText());
        }
        if (select != null && StringUtils.isNotBlank(select.getText())){
            headerMap.put("select", select.getText());
        }
        if (format != null && StringUtils.isNotBlank(format.getText())){
            headerMap.put("format", format.getText());
        }
        if (length != null && StringUtils.isNotBlank(length.getText())){
            headerMap.put("length", length.getText());
        }
        if (itemColor != null && StringUtils.isNotBlank(itemColor.getText())){
            headerMap.put("itemColor", itemColor.getText());
        }
        if (fontColor != null && StringUtils.isNotBlank(fontColor.getText())){
            headerMap.put("fontColor", fontColor.getText());
        }
        if (itemFontColor != null && StringUtils.isNotBlank(itemFontColor.getText())){
            headerMap.put("itemFontColor", itemFontColor.getText());
        }
        if (!hasItem){
            nameMap.put(text.getText(), key.getText());
        }
        headMap.put(text.getText(), headerMap);
    }

    public static Map<String, Map<String, Object>> gainExcelScreenMap(String type) {

        return excelScreenMap.get(type);
    }

    public static Map<String, String> gainExcelNameMap(String type){
           if (excelNameMap.containsKey(type)){
               return excelNameMap.get(type);
           }
        return null;
    }

    public static Map<String, String> gainAttrMap(String type){
        if (contentAttributeMap.containsKey(type)){
            return contentAttributeMap.get(type);
        }
        return null;
    }

    public static Integer gainTitleLength(String type){
        if (lengthMap.containsKey(type)){
            return lengthMap.get(type);
        }
        return null;
    }

//    public static void main(String[] aa) {
//        System.out.println(contentAttributeMap);
//    }

}
