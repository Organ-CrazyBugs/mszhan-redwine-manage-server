package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import com.mszhan.redwine.manage.server.service.ProductService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import com.mszhan.redwine.manage.server.util.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public Integer queryCount(ProductQuery query) {

        return productMapper.queryCount(query);
    }

    @Override
    public ResponseUtils.ResponseVO queryForPage(ProductQuery query) {
        PaginateResult<Product> tableData = new PaginateResult();
        Integer count = productMapper.queryCount(query);
        if (count == null || count.equals(0)){
            tableData.setRows(new ArrayList<>());
            tableData.setTotal(0L);
            return ResponseUtils.newResponse().succeed(tableData);
        }
        tableData.setTotal(Long.valueOf(count));
        List<Product> list = productMapper.queryForPage(query);
        tableData.setRows(list);
        return ResponseUtils.newResponse().succeed(tableData);
    }

    @Override
    public ResponseUtils.ResponseVO addProduct(Product product, MultipartFile file) {
        if (StringUtils.isBlank(product.getSku())){
            return ResponseUtils.newResponse().failed(500, "条形码不能为空");
        }
        if (StringUtils.isBlank(product.getUnit())){
            return ResponseUtils.newResponse().failed(500, "单位不能为空");
        }
        if (StringUtils.isBlank(product.getSpecification())){
            return ResponseUtils.newResponse().failed(500, "规格不能为空");
        }
        if (StringUtils.isBlank(product.getProductName())){
            return ResponseUtils.newResponse().failed(500, "商品名称不能为空");
        }
        if (product.getCost() == null){
            return ResponseUtils.newResponse().failed(500, "成本不能为空");
        }
        if (product.getGeneralGentPrice() == null){
            return ResponseUtils.newResponse().failed(500, "总代理价格不能为空");
        }
        if (product.getGentPrice() == null){
            return ResponseUtils.newResponse().failed(500, "代理价格不能为空");
        }
        if (product.getWholesalePrice() == null){
            return ResponseUtils.newResponse().failed(500, "批发价格不能为空");
        }
        if (product.getRetailPrice() == null){
            return ResponseUtils.newResponse().failed(500, "零售价格不能为空");
        }
        trimToNullValue(product);
        ResponseUtils.ResponseVO res = uploadLoadProPe(file);
        if (!res.isSuccess()){
            return res;
        }
        //todo:用户信息
        String path = res.getData().toString();
        product.setProductUrl(path);
        Date date = new Date();
        product.setCreateDate(date);
        product.setUpdateDate(date);
        productMapper.insert(product);
        return ResponseUtils.newResponse().succeed();
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

    private ResponseUtils.ResponseVO uploadLoadProPe(MultipartFile file){
        if (file.isEmpty()) {
            return ResponseUtils.newResponse().succeed();
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
            return ResponseUtils.newResponse().failed(500, "图片格式必须是bmp,jpg,png,tiff,gif");
        }

        // 文件上传后的路径
        String filePath = checkPath();
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        try {
            file.transferTo(dest);
            ResponseUtils.ResponseVO result = ResponseUtils.newResponse();
            result.setData(dest.getPath());
            return result;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseUtils.newResponse().failed(500,"文件上传失败");
    }

    @Override
    public ResponseUtils.ResponseVO updateProduct(Product upProduct, MultipartFile file) {
        if (upProduct.getId() == null){
            return ResponseUtils.newResponse().failed(500, "id不能为空");
        }
        if (StringUtils.isBlank(upProduct.getSku())){
            return ResponseUtils.newResponse().failed(500, "条形码不能为空");
        }
        if (StringUtils.isBlank(upProduct.getUnit())){
            return ResponseUtils.newResponse().failed(500, "单位不能为空");
        }
        if (StringUtils.isBlank(upProduct.getSpecification())){
            return ResponseUtils.newResponse().failed(500, "规格不能为空");
        }
        if (StringUtils.isBlank(upProduct.getProductName())){
            return ResponseUtils.newResponse().failed(500, "商品名称不能为空");
        }
        if (upProduct.getCost() == null){
            return ResponseUtils.newResponse().failed(500, "成本不能为空");
        }
        if (upProduct.getGeneralGentPrice() == null){
            return ResponseUtils.newResponse().failed(500, "总代理价格不能为空");
        }
        if (upProduct.getGentPrice() == null){
            return ResponseUtils.newResponse().failed(500, "代理价格不能为空");
        }
        if (upProduct.getWholesalePrice() == null){
            return ResponseUtils.newResponse().failed(500, "批发价格不能为空");
        }
        if (upProduct.getRetailPrice() == null){
            return ResponseUtils.newResponse().failed(500, "零售价格不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(upProduct.getId());
        if (product == null){
            return ResponseUtils.newResponse().failed(500,"没有找到对应商品信息");
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
        return ResponseUtils.newResponse().succeed();
    }

    @Override
    public ResponseUtils.ResponseVO upProductPic(Integer id, MultipartFile file) {
        if (id == null){
            return ResponseUtils.newResponse().failed(500, "id不能为空");
        }
        ResponseUtils.ResponseVO vo = uploadLoadProPe(file);
        if (!vo.isSuccess()){
           return vo;
        }
        String path = vo.getData().toString();
        if (StringUtils.isBlank(path)){
            return ResponseUtils.newResponse().failed(500, "请上传图片");
        }
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null){
            return ResponseUtils.newResponse().failed(500, "没有找到对应产品信息");
        }
        productMapper.updatePic(id, path);
        return ResponseUtils.newResponse().succeed();
    }

    @Override
    public ResponseUtils.ResponseVO removeProduct(Integer id) {
        productMapper.removePicById(id);
        return ResponseUtils.newResponse().succeed();
    }

    @Override
    public ResponseUtils.ResponseVO queryById(Integer id) {
        ResponseUtils.ResponseVO vo = ResponseUtils.newResponse();
        vo.setData(productMapper.selectByPrimaryKey(id));
        return vo;
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
