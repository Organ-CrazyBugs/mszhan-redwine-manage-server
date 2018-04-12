package com.mszhan.redwine.manage.server.core;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 对HttpServletRequest的parameter获取进行扩展, 更加方便的获取需要类型的参数值
 *
 * Created by iblilife@163.com on 2017/12/5.
 */
public class Requests {
    private HttpServletRequest request;

    private Function<String, Integer> integerConvertFun = val -> {
        try {
            return Integer.valueOf(val);
        } catch (Exception e) {
            String msg = String.format("值[%s]转换Integer类型失败 Error: %s", val, e.getMessage());
            System.err.println(msg);
        }
        return null;
    };

    private Function<String, Double> doubleConvertFun = val -> {
        try {
            return Double.valueOf(val);
        } catch (Exception e) {
            String msg = String.format("值[%s]转换Double类型失败 Error: %s", val, e.getMessage());
            System.err.println(msg);
        }
        return null;
    };

    private Function<String, Long> longConvertFun = val -> {
        try {
            return Long.valueOf(val);
        } catch (Exception e) {
            String msg = String.format("值[%s]转换Long类型失败 Error: %s", val, e.getMessage());
            System.err.println(msg);
        }
        return null;
    };

    private Function<String, BigDecimal> bigDecimalConvertFun = val -> {
        try {
            return new BigDecimal(val);
        } catch (Exception e) {
            String msg = String.format("值[%s]转换BigDecimal类型失败 Error: %s", val, e.getMessage());
            System.err.println(msg);
        }
        return null;
    };

    public static Requests newInstance(HttpServletRequest request){
        return new Requests(request);
    }

    public boolean isAjax() {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ||
                (request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json"));
    }

    public Requests(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    //== String
    public String getString(String name, String defaultVal) {
        return getParameter(name, Function.identity(), defaultVal);
    }

    public List<String> getStringArray(String name, String splitRegex, List<String> defaultVal) {
        return getParameterArray(name, Arrays.asList(splitRegex), defaultVal, Function.identity());
    }

    public List<String> getStringArray(String name, List<String> splitRegexList, List<String> defaultVal) {
        return getParameterArray(name, splitRegexList, defaultVal, Function.identity());
    }

    //== Integer
    public Integer getInteger(String name, Integer defaultVal) {
        return getParameter(name, integerConvertFun, defaultVal);
    }

    public List<Integer> getIntegerArray(String name, String splitRegex, List<Integer> defaultVal) {
        return getParameterArray(name, Arrays.asList(splitRegex), defaultVal, integerConvertFun);
    }

    public List<Integer> getIntegerArray(String name, List<String> splitRegexList, List<Integer> defaultVal) {
        return getParameterArray(name, splitRegexList, defaultVal, integerConvertFun);
    }

    //== Double
    public Double getDouble(String name, Double defaultVal) {
        return getParameter(name, doubleConvertFun, defaultVal);
    }

    public List<Double> getDoubleArray(String name, String splitRegex, List<Double> defaultVal) {
        return getParameterArray(name, Arrays.asList(splitRegex), defaultVal, doubleConvertFun);
    }

    public List<Double> getDoubleArray(String name, List<String> splitRegexList, List<Double> defaultVal) {
        return getParameterArray(name, splitRegexList, defaultVal, doubleConvertFun);
    }

    //== Long
    public Long getLong(String name, Long defaultVal) {
        return getParameter(name, longConvertFun, defaultVal);
    }

    public List<Long> getLongArray(String name, String splitRegex, List<Long> defaultVal) {
        return getParameterArray(name, Arrays.asList(splitRegex), defaultVal, longConvertFun);
    }

    public List<Long> getLongArray(String name, List<String> splitRegexList, List<Long> defaultVal) {
        return getParameterArray(name, splitRegexList, defaultVal, longConvertFun);
    }

    //== BigDecimal
    public BigDecimal getBigDecimal(String name, BigDecimal defaultVal) {
        return getParameter(name, bigDecimalConvertFun, defaultVal);
    }

    public List<BigDecimal> getBigDecimalArray(String name, String splitRegex, List<BigDecimal> defaultVal) {
        return getParameterArray(name, Arrays.asList(splitRegex), defaultVal, bigDecimalConvertFun);
    }

    public List<BigDecimal> getBigDecimalArray(String name, List<String> splitRegexList, List<BigDecimal> defaultVal) {
        return getParameterArray(name, splitRegexList, defaultVal, bigDecimalConvertFun);
    }

    //== Date
    public Date getDate(String name, SimpleDateFormat dateFormat, Date defaultVal) {
        String valString = getString(name, null);
        if (valString == null) {
            return defaultVal;
        }
        Date date = null;
        try {
            date = dateFormat.parse(valString);
        } catch (ParseException e) {
            String msg = String.format("值[%s]转换Date类型失败 Error: %s", valString, e.getMessage());
            System.err.println(msg);
        }
        return date;
    }

    //== Enum
    private <T extends Enum<T>> T convertEnum(String valString, Class<T> enumClazz) {
        if (valString == null) {
            return null;
        }
        T result = null;
        try {
            result = T.valueOf(enumClazz, valString);
        } catch (Exception e) {
            String msg = String.format("值[%s]转换Enum类型失败 Error: %s", valString, e.getMessage());
            System.err.println(msg);
        }
        return result;
    }

    public <T extends Enum<T>> T getEnum(String name, Class<T> enumClazz, T defaultVal){
        return convertEnum(getString(name, null), enumClazz);
    }

    public <T extends Enum<T>> List<T> getEnumArray(String name, Class<T> enumClazz, String splitRegex, List<T> defaultVal){
        return getEnumArray(name, enumClazz, Arrays.asList(splitRegex), defaultVal);
    }

    public <T extends Enum<T>> List<T> getEnumArray(String name, Class<T> enumClazz, List<String> splitRegexList, List<T> defaultVal){
        List<String> stringArray = getStringArray(name, splitRegexList, new ArrayList<>());
        if (stringArray == null || stringArray.isEmpty()) {
            return defaultVal;
        }
        return stringArray.stream().filter(StringUtils::isNotBlank)
                .map(item -> convertEnum(item, enumClazz))
                .filter(item -> item != null)
                .collect(Collectors.toList());
    }



    public <T> T getParameter(String name, Function<String, T> function, T defaultVal) {
        String valString = StringUtils.trimToNull(request.getParameter(name));
        if (valString == null) {
            return defaultVal;
        }
        T val = function == null ? null : function.apply(valString);
        return val == null ? defaultVal : val;
    }

    private <T> List<T> getParameterArray(String name, List<String> splitRegex, List<T> defaultVal, Function<String, T> convertFun) {
        if (splitRegex != null) {
            splitRegex = splitRegex.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        }
        String valString = getString(name, null);
        if (valString == null || splitRegex == null || splitRegex.isEmpty()) {
            return defaultVal;
        }

        Iterator<String> splitIterator = splitRegex.iterator();
        String firstSplitRegex = splitIterator.next();
        List<String> stringList = Arrays.asList(valString.split(firstSplitRegex));
        while (splitIterator.hasNext()) {
            String regex = splitIterator.next();
            List<String> list = new ArrayList<>();
            for (String str : stringList) {
                list.addAll(Arrays.asList(str.split(regex)));
            }
            stringList = list;
        }
        List<T> result = new ArrayList<>();
        for (String item : stringList) {
            T itemVal = convertFun.apply(item);
            if (itemVal == null) {
                continue;
            }
            result.add(itemVal);
        }
        return result.isEmpty() ? defaultVal : result;
    }

}
