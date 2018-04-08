package com.mszhan.redwine.manage.server.web;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import com.mszhan.redwine.manage.server.service.ProductService;
import com.mszhan.redwine.manage.server.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/search")
    public ResponseUtils.ResponseVO search(ProductQuery query) {
        return productService.queryForPage(query);
    }

    @GetMapping(value = "/query_by_id")
    public ResponseUtils.ResponseVO queryById(Integer id) {
        return productService.queryById(id);
    }
    @PostMapping(value = "/add_product")
    public ResponseUtils.ResponseVO addProduct(@RequestBody Product product, @RequestParam("file") MultipartFile file) {
        return productService.addProduct(product, file);
    }
    @PutMapping(value = "/update_product_pic/{id}")
    public ResponseUtils.ResponseVO updateProductPic(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) {
        return productService.upProductPic(id, file);
    }
    @GetMapping(value = "/index")
    public ModelAndView productIndex() {
        ModelAndView view = new ModelAndView("product");
        return view;
    }
    @PutMapping(value = "/updateProduct")
    public ResponseUtils.ResponseVO updateProduct(@RequestBody Product product, @RequestParam("file") MultipartFile file) {
        return productService.updateProduct(product, file);
    }



    @DeleteMapping(value = "/updateProduct/{id}")
    public ResponseUtils.ResponseVO del(@PathVariable("id") Integer id) {
        return productService.removeProduct(id);
    }
}
