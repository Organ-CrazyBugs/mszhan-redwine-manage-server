package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.FetchProductSelectPopupDataRMQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper extends Mapper<Product> {

    Integer queryCount(ProductQuery query);

    List<FetchProductSelectPopupDataRMQuery> fetchProductSelectPopupData(
            @Param("sku") String sku,
            @Param("productName") String productName
    );

    List<Product> queryForPage(ProductQuery query);

    void removeByIdList(List<Integer> idList);

    void updatePic(@Param("id") Integer id, @Param("filePath") String filePath, @Param("fileName") String fileName, @Param("large") Boolean large);



    List<Product> queryProductBySku(@Param("sku") String sku);


}