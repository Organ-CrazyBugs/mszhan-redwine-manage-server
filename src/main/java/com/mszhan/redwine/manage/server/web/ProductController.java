package com.mszhan.redwine.manage.server.web;

import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Product;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.ProductQuery;
import com.mszhan.redwine.manage.server.service.ProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

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
    public Object addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return Responses.newInstance().succeed();
    }

    @GetMapping("/upload_file_index")
    public ModelAndView uploadFile(Integer id, Boolean basic) {
        ModelAndView view = new ModelAndView("product/upload_file");
        Product product = productService.queryById(id);
        view.addObject("id", product.getId());
        view.addObject("filePath", product.getFilePath());
        view.addObject("fileName", product.getProductFileName());
        return view;
    }

    @PostMapping(value = "/upload_file")
    public Object uploadFile(Integer id, String sku, @RequestParam("file") MultipartFile file, Boolean large) {
        Map<String, String> pathMap = productService.upProductPic(id, sku, file, large);
        return Responses.newInstance().succeed(pathMap);
    }
//    @PostMapping(value = "/delete_pic")
//    public Object uploadFile(Integer id) {
//        productService.upProductPic(id, file);
//        return Responses.newInstance().succeed();
//    }

//    @PutMapping(value = "/update_product_pic/{id}")
//    public Object updateProductPic(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile file) {
//        productService.upProductPic(id, file);
//        return Responses.newInstance().succeed();
//    }
    @GetMapping(value = "/index")
    public ModelAndView productIndex() {
        ModelAndView view = new ModelAndView("product");
        return view;
    }
    @PutMapping(value = "/update_product")
    public Object updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
        return Responses.newInstance().succeed();
    }
    @PostMapping(value = "/delete_pic")
    public Object deletePic(Integer id, Boolean large) {
        productService.removePic(id, large);
        return Responses.newInstance().succeed();
    }


    @DeleteMapping(value = "/updateProduct/{id}")
    public Object del(@PathVariable("id") Integer id) {
        productService.removeProduct(id);
        return Responses.newInstance().succeed();
    }
}
