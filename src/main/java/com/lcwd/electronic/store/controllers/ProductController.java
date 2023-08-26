package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.services.Implementation.UserServiceImplementation;
import com.lcwd.electronic.store.services.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    //logging purpose
    Logger log = LoggerFactory.getLogger(UserServiceImplementation.class);

    @Autowired
    private ProductService productService;

    //create
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @Valid
            @RequestBody ProductDto productDto) {
        ProductDto createdProductDto = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductDto);
    }

    //update
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable String productId,
            @Valid
            @RequestBody ProductDto productDto
    ) {
        ProductDto updatedProductDto = productService.updateProduct(productDto, productId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProductDto);
    }

    //delete
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponseMessage> deleteProduct(
            @PathVariable String productId
    ) {
        productService.deleteProduct(productId);
        ApiResponseMessage apiResponseMessage
                = ApiResponseMessage
                .builder()
                .message("Product Deleted Successfully")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponseMessage);
    }

    //getProductById
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable String productId
    ) {
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }

    //getAllProducts
    @GetMapping
    public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir

    ) {
        PageableResponse<ProductDto> allProducts = productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(allProducts);
    }

    //search products by title
    @GetMapping("/search/{subTitle}")
    public ResponseEntity<List<ProductDto>> getProductsByTitle(
            @PathVariable String subTitle
    ) {
        List<ProductDto> productDtos = productService.searchByTitle(subTitle);
        return ResponseEntity.status(HttpStatus.OK).body(productDtos);
    }

    //get all Live Products
    @GetMapping("/live")
    public ResponseEntity<List<ProductDto>> getAllLiveProducts() {
        List<ProductDto> allLiveProducts = productService.getAllLiveProducts();
        return ResponseEntity.status(HttpStatus.OK).body(allLiveProducts);
    }

    //create product with Category
    @PostMapping("/category/{categoryId}")
    public ResponseEntity<ProductDto> createProductWithCategory(
            @PathVariable String categoryId,
            @Valid
            @RequestBody ProductDto productDto) {
        ProductDto productWithCategory = productService.createWithCategory(productDto, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED).body(productWithCategory);
    }

}
