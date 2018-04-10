package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.core.Responses;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

    @Override
    public PaginateResult<Product> queryForPage(ProductQuery query) {
        Integer count = productMapper.queryCount(query);
        if (count == null || count.equals(0)){
            PaginateResult.newInstance(0, new ArrayList<>());
        }
        List<Product> list = productMapper.queryForPage(query);
        return PaginateResult.newInstance(count, list);
    }

    @Override
    public void addProduct(Product product, MultipartFile file) {
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

        List<Product> checkProList = productMapper.queryProductBySku(product.getSku());
        if (!CollectionUtils.isEmpty(checkProList)){
            throw BasicException.newInstance().error("已经存在相同的SKU，不能重复添加", 500);
        }

        String filePath = uploadLoadProPe(file);
        if (StringUtils.isNotBlank(filePath)) {
            product.setProductUrl(filePath);
        }
        //todo:用户信息
        Date date = new Date();
        product.setCreateDate(date);
        product.setUpdateDate(date);
        productMapper.insert(product);
    }
    private void trimToNullValue(Product product){
        product.setBackRemark(StringUtils.trimToNull(product.getBackRemark()));
        product.setBackRemark(StringUtils.trimToNull(product.getBrandName()));
        product.setBackRemark(StringUtils.trimToNull(product.getProductionArea()));
        product.setBackRemark(StringUtils.trimToNull(product.getLevel()));
        product.setBackRemark(StringUtils.trimToNull(product.getSku()));
        product.setBackRemark(StringUtils.trimToNull(product.getSpecification()));
        product.setBackRemark(StringUtils.trimToNull(product.getUnit()));
    }

    private String uploadLoadProPe(MultipartFile file){
        if (file.isEmpty()) {
            return null;
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        List<String> checkTypeList = new ArrayList<>();
        checkTypeList.add("bmp");
        checkTypeList.add("jpg");
        checkTypeList.add("png");
        checkTypeList.add("tiff");
        checkTypeList.add("gif");
        if (!checkTypeList.contains(suffixName.toLowerCase())){
            throw BasicException.newInstance().error("图片格式必须是bmp,jpg,png,tiff,gif", 500);
        }

        // 文件上传后的路径
        String filePath = checkPath();
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        try {
            file.transferTo(dest);
            return dest.getPath();
        } catch (Exception e) {
            throw BasicException.newInstance().error("未知异常，请联系管理员", 500).originEx(e);
        }
    }

    @Override
    public void updateProduct(Product upProduct, MultipartFile file) {
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
        trimToNullValue(upProduct);
        Date date = new Date();
        product.setUpdateDate(date);
        product.setUpdator(1);
        product.setBackRemark(upProduct.getBackRemark());
        product.setAlcoholContent(upProduct.getAlcoholContent());
        product.setBrandName(upProduct.getBrandName());
        product.setCost(upProduct.getCost());
        product.setGeneralGentPrice(upProduct.getGeneralGentPrice());
        product.setGentPrice(upProduct.getGentPrice());
        product.setLevel(upProduct.getLevel());
        product.setRetailPrice(upProduct.getRetailPrice());
        product.setUnit(upProduct.getUnit());
        product.setWholesalePrice(upProduct.getWholesalePrice());
        product.setSpecification(upProduct.getSpecification());
        product.setProductionArea(upProduct.getProductionArea());
        product.setProductName(upProduct.getProductName());
        productMapper.updateByPrimaryKey(product);
    }

    @Override
    public void upProductPic(Integer id, MultipartFile file) {
        if (id == null){
            throw BasicException.newInstance().error("id不能为空", 500);
        }
        String path = uploadLoadProPe(file);
        if (StringUtils.isBlank(path)){
            throw BasicException.newInstance().error("请上传图片", 500);
        }
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null){
            throw BasicException.newInstance().error("没有找到对应产品信息", 500);
        }
        productMapper.updatePic(id, path);
    }

    @Override
    public void removeProduct(Integer id) {
        productMapper.removePicById(id);
    }

    @Override
    public Product queryById(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    private String checkPath(){
        File directory = new File("..");
        try {
            String path = String.format("%s%s", directory.getCanonicalPath(), picturePath);
            File file = new File(path);
            file.mkdirs();
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
