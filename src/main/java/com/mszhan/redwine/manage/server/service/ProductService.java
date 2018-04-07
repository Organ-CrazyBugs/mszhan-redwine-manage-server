package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.core.Service;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import com.mszhan.redwine.manage.server.util.ResponseUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface ProductService extends Service<Product> {

    Integer queryCount(ProductQuery query);

    ResponseUtils.ResponseVO queryForPage(ProductQuery query);

    ResponseUtils.ResponseVO addProduct(Product product, MultipartFile file);

    ResponseUtils.ResponseVO updateProduct(Product product, MultipartFile file);

    ResponseUtils.ResponseVO upProductPic(Integer id, MultipartFile file);

    ResponseUtils.ResponseVO removeProduct(Integer id);

    ResponseUtils.ResponseVO queryById(Integer id);
}
