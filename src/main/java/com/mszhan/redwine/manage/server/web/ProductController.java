package com.mszhan.redwine.manage.server.web;

import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import com.mszhan.redwine.manage.server.service.ProductService;
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
    public Object search(ProductQuery query) {
        PaginateResult<Product> result = productService.queryForPage(query);
        return Responses.newInstance().succeed(result);
    }

    @GetMapping(value = "/query_by_id")
    public Object queryById(Integer id) {
        Product product = productService.queryById(id);
        return Responses.newInstance().succeed(product);
    }
    @PostMapping(value = "/add_product")
    public Object addProduct(@RequestBody Product product, @RequestParam("file") MultipartFile file) {
        productService.addProduct(product, file);
        return Responses.newInstance().succeed();
    }
    @PutMapping(value = "/update_product_pic/{id}")
    public Object updateProductPic(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) {
        productService.upProductPic(id, file);
        return Responses.newInstance().succeed();
    }
    @GetMapping(value = "/index")
    public ModelAndView productIndex() {
        ModelAndView view = new ModelAndView("product");
        return view;
    }
    @PutMapping(value = "/updateProduct")
    public Object updateProduct(@RequestBody Product product, @RequestParam("file") MultipartFile file) {
        productService.updateProduct(product, file);
        return Responses.newInstance().succeed();
    }



    @DeleteMapping(value = "/updateProduct/{id}")
    public Object del(@PathVariable("id") Integer id) {
        productService.removeProduct(id);
        return Responses.newInstance().succeed();
    }
}
