package br.com.siecola.productsservice.products.controllers;

import br.com.siecola.productsservice.dto.*;
import br.com.siecola.productsservice.products.models.*;
import br.com.siecola.productsservice.repositories.*;
import com.amazonaws.xray.spring.aop.*;
import org.apache.logging.log4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/products")
@XRayEnabled
public class ProductsController {
    private static final Logger LOG = LogManager.getLogger(ProductsController.class);
    private final ProductsRepository productsRepository;

    public ProductsController(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        LOG.info("Get All Products");
        List<ProductDto> productsDto = new ArrayList<>();

        productsRepository.getAll().items().subscribe(product -> {
            productsDto.add(new ProductDto(product));
        }).join();

        return new ResponseEntity<>(productsDto, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") String id) {
       Product product = productsRepository.getById(id).join();
       if(product != null ) {
            return new ResponseEntity<>(new ProductDto(product), HttpStatus.OK);
       } else {
           return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
       }
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Product productCreated = ProductDto.toProduct(productDto);
        productCreated.setId(UUID.randomUUID().toString());
        productsRepository.create(productCreated).join();

        LOG.info("Product created - ID: {} ", productCreated.getId());
        return new ResponseEntity<>(new ProductDto(productCreated), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable("id") String id) {
        Product productDeleted = productsRepository.deleteById(id).join();
        if(productDeleted != null) {
            LOG.info("Product deleted - ID {} ", productDeleted.getId());
                return new ResponseEntity<>(new ProductDto(productDeleted), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Product Not Found!", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDto productDto,

                                           @PathVariable("id") String id) {
       try{
        Product updatedProduct = productsRepository
               .update(ProductDto.toProduct(productDto),"id").join();
       LOG.info("Product updated - ID {} " + updatedProduct.getId());
       return new ResponseEntity<>(new ProductDto(updatedProduct), HttpStatus.OK);
        } catch (CompletionException e) {
           return new ResponseEntity<>("Product not found ", HttpStatus.NOT_FOUND);
       }
    }

}








