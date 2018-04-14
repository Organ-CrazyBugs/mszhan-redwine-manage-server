package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.core.Service;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface ProductService extends Service<Product> {

    PaginateResult<Product> queryForPage(ProductQuery query);

    void addProduct(Product product);

    void updateProduct(Product product);

    Map<String, String> upProductPic(Integer id, String sku, MultipartFile file, Boolean large);

    void removeProduct(Integer id);

    void removeByIds(String ids);

    void removePic(Integer id, Boolean large);

    Product queryById(Integer id);
}
