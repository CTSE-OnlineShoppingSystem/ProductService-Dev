package com.example.ctseproductservice.Service;

import com.example.ctseproductservice.Model.Product;
import com.example.ctseproductservice.Repository.ProductRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final GridFsTemplate gridFsTemplate;

    private final GridFsOperations operations;

    @Autowired
    public ProductService(ProductRepository productRepository, GridFsTemplate gridFsTemplate, GridFsOperations operations) {
        this.productRepository = productRepository;
        this.gridFsTemplate = gridFsTemplate;
        this.operations = operations;
    }

    //insert product
    public void addProduct(String productName, float productPrice, float availability, String unit, String category, MultipartFile productImg)throws IOException {

        Object fileId = gridFsTemplate.store(productImg.getInputStream(),productImg.getOriginalFilename(), productImg.getContentType());

        Product product = new Product();

        //get all the existing products
        List<Product> products = getAllProducts();
        String productId;

        //if there is no existing products
        if (products.isEmpty()){
            productId = "PR" + 1;
        }
        else {
            //take the last object of the products array
            Product item = products.stream().reduce((first, second) -> second).orElse(null);
            String lastId = item.getProductId();
            //take from string 2 in products id
            int lastIdNum = Integer.parseInt(lastId.substring(2));
            int size = lastIdNum+1;
            productId = "PR" + size;
        }


        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductPrice(productPrice);
        product.setAvailability(availability);
        product.setUnit(unit);
        product.setCategory(category);

        product.setImg_fileId(fileId.toString());
        product.setImg_filename(productImg.getOriginalFilename());


        productRepository.save(product);
    }

    //update product
    public void updateProduct(String productId,String productName, float productPrice, float availability, String unit, String category,String img_fileId, String img_filename)throws IOException {

        //Object fileId = gridFsTemplate.store(productImg.getInputStream(),productImg.getOriginalFilename(), productImg.getContentType());

        Product product = new Product();


        product.setProductId(productId);
        product.setProductName(productName);
        product.setProductPrice(productPrice);
        product.setAvailability(availability);
        product.setUnit(unit);
        product.setCategory(category);

        product.setImg_fileId(img_fileId);
        product.setImg_filename(img_filename);

        productRepository.save(product);
    }

    //insert product list
    public void addProducts(Product[] products){
        for (Product product : products){
            productRepository.save(product);
        }
    }

    //delete product
    public void deleteProduct(String productId){
        productRepository.deleteById(productId);
    }

    //get All Products
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    //create product Id
    public String createProductId(){
        //get all the existing products
        List<Product> products = getAllProducts();
        String productId;

        //if there is no existing products
        if (products.isEmpty()){
            productId = "PR" + 1;
        }
        else {
            //take the last object of the products array
            Product item = products.stream().reduce((first, second) -> second).orElse(null);
            String lastId = item.getProductId();
            //take from string 2 in products id
            int lastIdNum = Integer.parseInt(lastId.substring(2));
            int size = lastIdNum+1;
            productId = "PR" + size;
        }

        return productId;
    }

    //decrement availability
    public void decrementAvailability(String itemId, float quantity){
        List<Product> products = productRepository.findAll();
        for (Product item : products){
            if (item.getProductId().equals(itemId)){
                item.setAvailability(item.getAvailability()-quantity);
                productRepository.save(item);
            }
        }
    }

    public byte[] downloadImage(String img_fileId) throws IOException {

        //find file from DB
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(img_fileId)));

        //setting data to byte array
        byte[] file = new byte[0];

        if (gridFSFile != null) {
            file = IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream());
        }

        return file;
    }

    public HashMap<String, String> getDetailsOfImage(String id) {
        //find file from DB
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

        HashMap<String, String> image = new HashMap<>();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
            image.put("contentType", gridFSFile.getMetadata().get("_contentType").toString());
            image.put("filename", gridFSFile.getFilename());
        }

        return image;
    }



    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
}
