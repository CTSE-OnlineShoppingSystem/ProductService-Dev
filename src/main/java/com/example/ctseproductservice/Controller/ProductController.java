package com.example.ctseproductservice.Controller;

import com.example.ctseproductservice.Model.Product;
import com.example.ctseproductservice.Repository.ProductRepository;
import com.example.ctseproductservice.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private final ProductService productService;

    private final ProductRepository productRepository;

    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }


    @PostMapping("/products")
    public ResponseEntity<?> addProducts(@RequestBody Product[] products){
        try {
            productService.addProducts(products);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //adding a new product
    @PostMapping("/addProduct")
    public ResponseEntity<?> addProducts(@RequestParam("productName") String productName,@RequestParam("productPrice") Float productPrice,@RequestParam("availability") Float availability,@RequestParam("unit") String unit,@RequestParam("category") String category,@RequestParam("productImg") MultipartFile productImg)throws IOException {
        productService.addProduct(productName, productPrice, availability, unit, category, productImg);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestParam("productId") String productId,@RequestParam("productName") String productName,@RequestParam("productPrice") Float productPrice,@RequestParam("availability") Float availability,@RequestParam("unit") String unit,@RequestParam("category") String category,@RequestParam("img_fileId") String img_fileId,@RequestParam("img_filename") String img_filename){
        try {
            productService.updateProduct(productId,productName, productPrice, availability, unit, category, img_fileId, img_filename);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //getting all products
    @GetMapping("/")
    public ResponseEntity<?> getAllProducts(){
        try {
            List<Product> products = productService.getAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //deleting products
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //creating product id
    @GetMapping("/id")
    public ResponseEntity<?> createProductId(){
        try {
            return new ResponseEntity<>(productService.createProductId(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //select products by product id
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productRepository.findById(id));
    }


    @GetMapping("/getbycategory/{category}")
    public ResponseEntity<?> getProductByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable String id) throws IOException {
        byte[] image = productService.downloadImage(id);

        //get filename and content type
        HashMap<String, String> imgData = productService.getDetailsOfImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imgData.get("contentType")))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imgData.get("filename") + "\"")
                .body(new ByteArrayResource(image));
    }
}
