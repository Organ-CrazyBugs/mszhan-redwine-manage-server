package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.config.security.User;
import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.core.SecurityUtils;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import com.mszhan.redwine.manage.server.service.ProductService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
@Service
@Transactional
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Value("${productPath}")
    private String picturePath;

    @Value("${largePath}")
    private String largePath;

    @Value("${fileLocation}")
    private String fileLocation;


    @Override
    public PaginateResult<Product> queryForPage(ProductQuery query) {
        Integer count = productMapper.queryCount(query);
        if (count == null || count.equals(0)){
            PaginateResult.newInstance(0, new ArrayList<>());
        }
        List<Product> list = productMapper.queryForPage(query);
        User user = SecurityUtils.getAuthenticationUser();
        if (user == null){
            throw BasicException.newInstance().error("请先登录", 500);
        }
        String type = user.getAgentType();
        for (Product pro : list){
            if ("AGENT".equals(type)){
                pro.setGeneralGentPrice(BigDecimal.ZERO);
            }
        }
        return PaginateResult.newInstance(count, list);
    }

    @Override
    public void addProduct(Product product) {
        if (StringUtils.isBlank(product.getSku())){
            throw BasicException.newInstance().error("条形码不能为空", 500);
        }
        if (StringUtils.isBlank(product.getUnit())){
            throw BasicException.newInstance().error("单位不能为空", 500);
        }
        if (StringUtils.isBlank(product.getSpecification())){
            throw BasicException.newInstance().error("规格不能为空", 500);
        }
        if (StringUtils.isBlank(product.getProductName())){
            throw BasicException.newInstance().error("商品名称不能为空", 500);
        }
        if (product.getCost() == null){
            throw BasicException.newInstance().error("成本不能为空", 500);
        }
        if (product.getGeneralGentPrice() == null){
            throw BasicException.newInstance().error("总代理价格不能为空", 500);
        }
        if (product.getGentPrice() == null){
            throw BasicException.newInstance().error("代理价格不能为空", 500);
        }
        if (product.getWholesalePrice() == null){
            throw BasicException.newInstance().error("批发价格不能为空", 500);
        }
        if (product.getRetailPrice() == null){
            throw BasicException.newInstance().error("零售价格不能为空", 500);
        }
        trimToNullValue(product);
        User user = SecurityUtils.getAuthenticationUser();
        if (user == null){
            throw BasicException.newInstance().error("请先登录", 500);
        }
        List<Product> checkProList = productMapper.queryProductBySku(product.getSku());
        if (!CollectionUtils.isEmpty(checkProList)){
            throw BasicException.newInstance().error("已经存在相同的SKU，不能重复添加", 500);
        }

//        String filePath = uploadLoadProPe(file);
//        if (StringUtils.isNotBlank(filePath)) {
//            product.setProductUrl(filePath);
//        }
        //todo:用户信息
        Date date = new Date();
        product.setCreateDate(date);
        product.setUpdateDate(date);
        product.setCreator(user.getUserLoginId());
        product.setCreatorName(user.getAgentName());
        product.setUpdatorName(user.getAgentName());
        product.setRemove("N");
        productMapper.insert(product);
    }
    private void trimToNullValue(Product product){
        product.setBrandName(StringUtils.trimToNull(product.getBrandName()));
        product.setProductionArea(StringUtils.trimToNull(product.getProductionArea()));
        product.setLevel(StringUtils.trimToNull(product.getLevel()));
        product.setSku(StringUtils.trimToNull(product.getSku()));
        product.setSpecification(StringUtils.trimToNull(product.getSpecification()));
        product.setUnit(StringUtils.trimToNull(product.getUnit()));

        product.setOriginCountry(StringUtils.trimToNull(product.getOriginCountry()));
        product.setTreeAge(StringUtils.trimToNull(product.getTreeAge()));
        product.setStorageMethod(StringUtils.trimToNull(product.getStorageMethod()));
        product.setWithFood(StringUtils.trimToNull(product.getWithFood()));
        product.setCategory(StringUtils.trimToNull(product.getCategory()));
        product.setAge(StringUtils.trimToNull(product.getAge()));
        product.setMakingTime(StringUtils.trimToNull(product.getMakingTime()));
        product.setTastingRecords(StringUtils.trimToNull(product.getTastingRecords()));
        product.setRecommendedReason(StringUtils.trimToNull(product.getRecommendedReason()));
        product.setBrandBackgroud(StringUtils.trimToNull(product.getBrandBackgroud()));
    }

    private Map<String, String> uploadLoadProPe(String sku, MultipartFile file, Boolean largePath){
        if (file.isEmpty()) {
            throw BasicException.newInstance().error("未找到图片，格式必须是jpeg,jpg,png,gif", 500);
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        List<String> checkTypeList = new ArrayList<>();
        checkTypeList.add("gif");
        checkTypeList.add("jpg");
        checkTypeList.add("png");
        checkTypeList.add("jpeg");
        if (!checkTypeList.contains(suffixName.toLowerCase().replace(".",""))){
            throw BasicException.newInstance().error("图片格式必须是jpeg,jpg,png,gif", 500);
        }
        String newFileName = String.format("%s%s", sku, suffixName);
        // 文件上传后的路径
        String filePath = checkPath(largePath);
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(String.format("%s/%s",filePath, newFileName));
        // 检测是否存在目录
        try {
            file.transferTo(dest);
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("path", filePath);
            resultMap.put("fileName", newFileName);
            return resultMap;
        } catch (Exception e) {
            throw BasicException.newInstance().error("未知异常，请联系管理员", 500).originEx(e);
        }
    }

    @Override
    public void updateProduct(Product upProduct) {
        if (upProduct.getId() == null){
            throw BasicException.newInstance().error("id不能为空", 500);
        }
        if (StringUtils.isBlank(upProduct.getSku())){
            throw BasicException.newInstance().error("条形码不能为空", 500);
        }
        if (StringUtils.isBlank(upProduct.getUnit())){
            throw BasicException.newInstance().error("单位不能为空", 500);
        }
        if (StringUtils.isBlank(upProduct.getSpecification())){
            throw BasicException.newInstance().error("规格不能为空", 500);
        }
        if (StringUtils.isBlank(upProduct.getProductName())){
            throw BasicException.newInstance().error("商品名称不能为空", 500);
        }
        if (upProduct.getCost() == null){
            throw BasicException.newInstance().error("成本不能为空", 500);
        }
        if (upProduct.getGeneralGentPrice() == null){
            throw BasicException.newInstance().error("总代理价格不能为空", 500);
        }
        if (upProduct.getGentPrice() == null){
            throw BasicException.newInstance().error("代理价格不能为空", 500);
        }
        if (upProduct.getWholesalePrice() == null){
            throw BasicException.newInstance().error("批发价格不能为空", 500);
        }
        if (upProduct.getRetailPrice() == null){
            throw BasicException.newInstance().error("零售价格不能为空", 500);
        }
        Product product = productMapper.selectByPrimaryKey(upProduct.getId());
        if (product == null){
            throw BasicException.newInstance().error("没有找到对应商品信息", 500);
        }
        User user = SecurityUtils.getAuthenticationUser();
        if (user == null){
            throw BasicException.newInstance().error("请先登录", 500);
        }
        if (!upProduct.getSku().equals(product.getSku())){
            throw BasicException.newInstance().error("sku不能修改", 500);
        }
        trimToNullValue(upProduct);
        Date date = new Date();
        upProduct.setUpdatorName(user.getAgentName());
        upProduct.setUpdator(user.getAgentId());
        upProduct.setUpdateDate(date);
        upProduct.setCreateDate(product.getCreateDate());
        upProduct.setCreatorName(product.getCreatorName());
        upProduct.setCreator(product.getCreator());
        upProduct.setRemove(product.getRemove());
        productMapper.updateByPrimaryKey(upProduct);
    }

//    @Override
//    public void upProductPic(Integer id, MultipartFile file) {
//        if (id == null){
//            throw BasicException.newInstance().error("id不能为空", 500);
//        }
//        Product product = productMapper.selectByPrimaryKey(id);
//        if (product == null){
//            throw BasicException.newInstance().error("没有找到对应产品信息", 500);
//        }
//        String sku = product.getSku();
//        String path = uploadLoadProPe(file, sku);
//        if (StringUtils.isBlank(path)){
//            throw BasicException.newInstance().error("请上传图片", 500);
//        }
//        productMapper.updatePic(id, path);
//    }

    @Override
    public Map<String, String> upProductPic(Integer id, String sku, MultipartFile file, Boolean large) {
        if (large == null){
            throw BasicException.newInstance().error("上传类型不能为空", 500);
        }
        if (StringUtils.isBlank(sku)){
            throw BasicException.newInstance().error("sku不能为空", 500);
        }
        Map<String, String> pathMap = null;
        if (id == null){
            List<Product> product = productMapper.queryProductBySku(sku);
            if (!CollectionUtils.isEmpty(product)){
                throw BasicException.newInstance().error("已经存在该SKU，不可操作上传", 500);
            }
            pathMap = uploadLoadProPe(sku, file, large);
        } else {
            pathMap = uploadLoadProPe(sku, file, large);
            productMapper.updatePic(id, pathMap.get("filePath"), pathMap.get("fileName"), large);
        }
        return pathMap;

    }
    @Override
    public void removeProduct(Integer id) {
    }

    @Override
    public void removeByIds(String ids) {
        if (StringUtils.isBlank(ids)){
            throw BasicException.newInstance().error("请先勾选数据再操作删除", 500);
        }
        List<Integer> idList = new ArrayList<>();
        String str[] = StringUtils.trimToNull(ids).split(",");
        for (String id : str){
            idList.add(Integer.parseInt(id));
        }
        productMapper.removeByIdList(idList);
    }

    @Override
    public void removePic(Integer id, Boolean large) {
        productMapper.updatePic(id, null, null, large);
    }


    @Override
    public Product queryById(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public Object queryForAll() {

        List<Product> productList = productMapper.queryForAll();
        User user = SecurityUtils.getAuthenticationUser();
        if (user == null){
            throw BasicException.newInstance().error("请先登录", 500);
        }
        String type = user.getAgentType();
        for (Product pro : productList){
            if ("AGENT".equals(type)){
                pro.setGeneralGentPrice(BigDecimal.ZERO);
            }
        }
        return Responses.newInstance().data(productList);
    }

    private String checkPath(Boolean large){
        File directory = new File("..");
        try {
            String path = null;
            if (large){
                path = String.format("%s%s", fileLocation, largePath);
            } else {
                path = String.format("%s%s", fileLocation, picturePath);
            }
            File file = new File(path);
            file.mkdirs();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
