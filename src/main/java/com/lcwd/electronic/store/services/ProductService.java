package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

import java.util.List;

public interface ProductService {

    //create
    ProductDto createProduct(ProductDto productDto);

    //update
    ProductDto updateProduct(ProductDto productDto, String productId);

    //delete
    void deleteProduct(String productId);

    //get by Id
    ProductDto getProductById(String productId);

    //get All Products
    PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir);

    //search Products By Title
    List<ProductDto> searchByTitle(String subTitle);

    //get All live products
    List<ProductDto> getAllLiveProducts();

    //create Product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);

}
