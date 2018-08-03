package com.mszhan.redwine.manage.server.core.excelContent.processor;

import com.mszhan.redwine.manage.server.core.excelContent.InitExcelContent;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: 吴树添
 * Date: 16-7-1
 * Time: 下午4:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class SimpleExcelReader extends ExcelReader {

    private Map<String, String> nameMap;
    private Map<String, Map<String, Object>> screenMap;
    //    private List<String> errorList = new ArrayList<>();
    private List<Map<String, Object>> listMap = new ArrayList<>();
    private Map<String, List<Map<String, Object>>> complexMap = new HashMap<>();
    private Map<String, String> contentAttrMap = null;
    private String type = null;

    public SimpleExcelReader(String type, HttpServletRequest request, String fileName)  {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile ordersFile = multipartRequest.getFile(fileName);

        InputStream fileInputStream = null;
        String name = null;
        try {
            fileInputStream = ordersFile.getInputStream();
            name = ordersFile.getOriginalFilename();
            super.setInputStream(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProcessorException("获取文件异常");
        }
        if (!name.endsWith(".xlsx")){
             throw new ProcessorException("文件必须是xlsx 2007版本的excel文件");
        }
        nameMap = InitExcelContent.gainExcelNameMap(type);
        screenMap = InitExcelContent.gainExcelScreenMap(type);
        if (screenMap == null) {
            throw new ProcessorException("没有找到该类型的配置信息");
        }
        contentAttrMap = InitExcelContent.gainAttrMap(type);
        Integer titleLength = InitExcelContent.gainTitleLength(type);
        this.type = type;
        init(titleLength, contentAttrMap, fileInputStream, request);
    }

    public abstract void handler();



    @Override
    public void optRows(List<Map<String, Object>> mapList, Map<Integer, String> indexMap) {
        try {
        for (Map<String, Object> map : mapList) {
            Object[] rowList = (Object[]) map.get("rowList");
            if (null == map.get("rowList") || 0 == rowList.length) {
                continue;
            }
            int curRow = (Integer) map.get("curRow");
            int sheetIndex = (Integer) map.get("sheetIndex");
            int size = rowList.length;
            Map<String, Object> resultMap = new HashMap<>();
            for (int i = 0; i < size; i++) {
                String headerName = indexMap.get(i);
                if (null == headerName) {
                    return ;
                }
                String name = nameMap.get(headerName);
                if (StringUtils.isBlank(name)){
                    throw new ProcessorException(String.format("%s没有找到对应配置信息", headerName));
                }
                Map<String, Object> fontMap = screenMap.get(headerName);
                if (null == fontMap){
                    throw new ProcessorException(String.format("%s没有找到对应属性信息", headerName));
                }
                if ( null == fontMap.get("type")){
                    throw new ProcessorException(String.format("%s没有找到对应[type]属性信息", headerName));
                }
                String fontType = fontMap.get("type").toString();
                Boolean necessary = (Boolean) fontMap.get("necessary");
                String value = StringUtils.trimToNull(rowList[i].toString());
                String format = null;
                if ("Date".equals(fontType)){
                    if (null != fontMap.get("format")) {
                        format = fontMap.get("format").toString();
                    } else {
                        //日期类型必须要有format属性
                        throw new ProcessorException(String.format("%s日期类型必须配置format属性", name));
                    }
                }
                resultMap.put(name, chooseType(fontType, curRow, value, headerName, super.getErrorList(), necessary, format));
            }
            resultMap.put("curRow", curRow);
            resultMap.put("sheetIndex", sheetIndex);
            listMap.add(resultMap);
        }
        } catch (ProcessorException ex){
            ex.printStackTrace();
            throw new ProcessorException(ex.getMessage());
        } catch(Exception ex){
          ex.printStackTrace();
          throw new ProcessorException("解析数据错误");
        }
        handler();
        listMap.clear();
    }

    @Override
    public void complexOptRows(List<Map<String, Object>> mapList, Map<Integer, String> indexMap, Boolean over) {
        String keyStr = contentAttrMap.get("complex");
        String[] keyColumn = keyStr.split(",");
        List<String> keyList = Arrays.asList(keyColumn);
        int keySize = keyList.size();
        for (Map<String, Object> map : mapList) {
            Object[] rowList = (Object[]) map.get("rowList");
            if (null == map.get("rowList") || 0 == rowList.length) {
                continue;
            }
            int curRow = (Integer) map.get("curRow");
            int sheetIndex = (Integer) map.get("sheetIndex");
            int size = rowList.length;
            Map<String, Object> resultMap = new HashMap<>();
            for (int i = 0; i < size; i++) {
                String headerName = indexMap.get(i);
                if (headerName == null) {
                    return;
                }
                String name = nameMap.get(headerName);
                if (StringUtils.isBlank(name)){
                    throw new ProcessorException(String.format("%s没有找到对应配置信息", headerName));
                }
                Map<String, Object> fontMap = screenMap.get(headerName);
                if (fontMap == null){
                    throw new ProcessorException(String.format("%s没有找到对应属性信息", headerName));
                }
                if (fontMap.get("type") == null){
                    throw new ProcessorException(String.format("%s没有找到对应[type]属性信息", headerName));
                }
                String fontType = fontMap.get("type").toString();
                Boolean necessary = (Boolean) fontMap.get("necessary");
                String value = null;
                if (rowList[i] != null){
                    value = StringUtils.trimToNull(rowList[i].toString());
                }
                String format = null;
                if ("Date".equals(fontType)){
                    if (null != fontMap.get("format")) {
                        format = fontMap.get("format").toString();
                    } else {
                        //日期类型必须要有format属性
                        throw new ProcessorException(String.format("%s日期类型必须配置format属性", name));
                    }
                }
                resultMap.put(name, chooseType(fontType, curRow, value, headerName, super.getErrorList(), necessary, format));
                resultMap.put("curRow", curRow);
                resultMap.put("sheetIndex", sheetIndex);
            }
            String joinKey = "";
            // todo:可能异常的地方
            for (int i = 0;i < keySize ; i ++){
                if (resultMap.get(keyColumn[i]) == null){
                    throw new ProcessorException(String.format("第三行%s复杂类型key[%s]找不到数据或者配置有问题", curRow, keyColumn[i]));
                }
                String obj =  resultMap.get(keyColumn[i]).toString();
                if (i == (keySize -1)){
                    joinKey = joinKey  + obj;
                } else {
                    joinKey = joinKey  + obj + "|";
                }
            }
            if (complexMap.containsKey(joinKey)){
                complexMap.get(joinKey).add(resultMap);
            } else {
                List<Map<String, Object>> complexList = new ArrayList<>();
                complexList.add(resultMap);
                complexMap.put(joinKey, complexList);
            }

        }
        if (over){
            handler();
        }

    }


    public Map<String, String> getNameMap() {
        return nameMap;
    }

    public void setNameMap(Map<String, String> nameMap) {
        this.nameMap = nameMap;
    }

    public Map<String, Map<String, Object>> getScreenMap() {
        return screenMap;
    }

    public void setScreenMap(Map<String, Map<String, Object>> screenMap) {
        this.screenMap = screenMap;
    }

    public List<Map<String, Object>> getListMap() {
        return listMap;
    }

    public void setListMap(List<Map<String, Object>> listMap) {
        this.listMap = listMap;
    }

    public Map<String, List<Map<String, Object>>> getComplexMap() {
        return complexMap;
    }

    public void setComplexMap(Map<String, List<Map<String, Object>>> complexMap) {
        this.complexMap = complexMap;
    }

    public Map<String, String> getContentAttrMap() {
        return contentAttrMap;
    }

    public void setContentAttrMap(Map<String, String> contentAttrMap) {
        this.contentAttrMap = contentAttrMap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  Object chooseType(String type, int j, String value, String headerName, List<String> errorList, Boolean necessary, String format) {

        switch (type) {
            case "String":
                return getMapString(value, j, headerName, necessary);
            case "BigDecimal":
                return getMapBigDecimal(value, j, headerName, necessary);
            case "Date":
                return getMapDateWithFormat(value, j, headerName, format, necessary);
            case "Integer":
                return getMapInteger(value, j, headerName, necessary);
        }
        return null;
    }

    public  String getMapString(Object value, int index, String name, boolean isNotAllowNull) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            if (isNotAllowNull) {
//                errorList.add(String.format("第%s行%s,不能为空", index, name));
                throw new ProcessorException(String.format("第%s行%s,不能为空", index, name));
            } else {
                return null;
            }
        }
        return value.toString();
    }

    public  BigDecimal getMapBigDecimal(Object value, int index, String name, boolean isNotAllowNull) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            if (isNotAllowNull) {
//                errorList.add(String.format("第%s行%s,不能为空", index, name));
                throw new ProcessorException(String.format("第%s行%s,不能为空", index, name));
            } else {
                return null;
            }
        }
        try {
            BigDecimal big = new BigDecimal(value.toString());
            return big;
        } catch (Exception ex) {
            ex.printStackTrace();
//            errorList.add(String.format("第%s行%s，不是数字类型", name, name));
            throw new ProcessorException(String.format("第%s行%s，不是数字类型", index, name));
        }
    }

    public  Integer getMapInteger(Object value, int index, String name, boolean isNotAllowNull) {
        if (value == null || StringUtils.isBlank(value.toString())) {
            if (isNotAllowNull) {
//                errorList.add(String.format("第%s行%s,不能为空", index, name));
                throw new ProcessorException(String.format("第%s行%s,不能为空", index, name));
            } else {
                return null;
            }
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
//            errorList.add(String.format("第%s行%s，不是数字类型", name, name));
            throw new ProcessorException(String.format("第%s行%s，不是整数数字类型", index, name));
        }
    }

    public  Date getMapDateWithFormat(Object value, int index, String name, String format, boolean isNotAllowNull) {

        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        String[] formats = format.split(",");
        if (value == null || StringUtils.isBlank(value.toString())) {
            if (isNotAllowNull) {
//                errorList.add(String.format("第%s行%s,不能为空", index, name));
                throw new ProcessorException(String.format("第%s行%s,不能为空", index, name));
//                return null;
            } else {
                return null;
            }
        }
        Date date = null;
        try {
            date = HSSFDateUtil.getJavaDate(Double.valueOf(value.toString()));
            return date;
        } catch( Exception ex){
            for (String mat : formats){
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(mat);
                    date = sdf.parse(value.toString());
                    return date;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new ProcessorException(String.format("第%s行%s，不是日期类型或者日期类型格式不是%s", index, name, format));
    }

}
