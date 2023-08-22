package com.lcwd.electronic.store.services.Implementation;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImplementation implements ProductService {

    //logging purpose
    Logger log = LoggerFactory.getLogger(UserServiceImplementation.class);

    //to use JPA methods for Database operation
    @Autowired
    private ProductRepository productRepository;

    //to convert entity and Dto into each other
    @Autowired
    private ModelMapper mapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        //generate Id for Product
        String productId = UUID.randomUUID().toString();
        productDto.setProductId(productId);

        //generate AddedDate
        productDto.setAddedDate(new Date());

        log.info("isLive:" + productDto.isLive());

        //convert dto to entity
        Product product = mapper.map(productDto, Product.class);

        //call JPA methods
        Product createdProduct = productRepository.save(product);

        //convert createdProduct to Dto
        ProductDto createdProductDto = mapper.map(createdProduct, ProductDto.class);
        return createdProductDto;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String productId) {
        //fetch product using Id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No Product Found with given Id"));

        //update the details
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setLive(productDto.isLive());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setStock(product.isStock());

        //update in Database
        Product updatedProduct = productRepository.save(product);

        //convert into Dto
        ProductDto updatedProductDto = mapper.map(updatedProduct, ProductDto.class);
        return updatedProductDto;
    }

    @Override
    public void deleteProduct(String productId) {
        //fetch product using Id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No Product Found with given Id"));

        //delete
        productRepository.delete(product);
    }

    @Override
    public ProductDto getProductById(String productId) {
        //fetch product using Id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("No Product Found with given Id"));

        //convert into Dto
        ProductDto productDto = mapper.map(product, ProductDto.class);
        return productDto;
    }

    @Override
    public PageableResponse<ProductDto> getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        //create object of sort
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> page = productRepository.findAll(pageable);
        PageableResponse<ProductDto> pageableResponse = Helper.getPageableResponse(page, ProductDto.class, mapper);
        return pageableResponse;
    }

    @Override
    public List<ProductDto> searchByTitle(String subTitle) {
        //fetch all Products based on title
        List<Product> byTitleContaining = productRepository.findByTitleContaining(subTitle);

        //convert the entity list into Dto List
        List<ProductDto> productDtoList = byTitleContaining.stream().map(product -> mapper.map(product, ProductDto.class)).collect(Collectors.toList());
        return productDtoList;
    }

    @Override
    public List<ProductDto> getAllLiveProducts() {
        //fetch all Live Products
        List<Product> byLiveTrue = productRepository.findByLiveTrue();

        //convert the entity list into Dto List
        List<ProductDto> productDtoList = byLiveTrue.stream().map(product -> mapper.map(product, ProductDto.class)).collect(Collectors.toList());
        return productDtoList;
    }
}
