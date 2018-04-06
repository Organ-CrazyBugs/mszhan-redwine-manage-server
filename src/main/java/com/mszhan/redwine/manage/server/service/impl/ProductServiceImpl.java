package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.service.ProductService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


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

}
