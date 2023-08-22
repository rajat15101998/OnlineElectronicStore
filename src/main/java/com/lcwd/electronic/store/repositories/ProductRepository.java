package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    //search by title
    List<Product> findByTitleContaining(String subTitle);

    //find live products
    List<Product> findByLiveTrue();
}
