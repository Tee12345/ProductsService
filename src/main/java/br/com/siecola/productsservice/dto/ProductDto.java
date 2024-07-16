package br.com.siecola.productsservice.dto;

import br.com.siecola.productsservice.products.models.*;
import com.fasterxml.jackson.annotation.*;

public record ProductDto(

        String id, String name, String code, float price, String model,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String url
) {

        public ProductDto(Product product) {
            this(product.getId(), product.getProductName(), product.getCode(), product.getPrice(),
                    product.getModel(), product.getProductUrl());
        }

        static public Product toProduct(ProductDto productDto) {
            Product product = new Product();
            product.setId(product.getId());
            product.setProductName(productDto.name);
            product.setCode(productDto.code);
            product.setModel(productDto.model);
            product.setPrice(productDto.price);
            product.setProductUrl(productDto.url);
            return product;
        }
}
