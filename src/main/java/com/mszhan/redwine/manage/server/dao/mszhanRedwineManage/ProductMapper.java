package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper extends Mapper<Product> {

    Integer queryCount(ProductQuery query);

    List<Product> queryForPage(ProductQuery query);

    void updatePic(@Param("id") Integer id, @Param("url") String url);

    void removePicById(@Param("id") Integer id);
}