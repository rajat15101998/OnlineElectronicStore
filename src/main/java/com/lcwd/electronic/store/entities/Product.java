package com.lcwd.electronic.store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "products")
public class Product {
     @Id
     private String productId;
     private String title;

     @Column(length = 1000)
     private String description;

     private int price;
     private int discountedPrice;
     private int quantity;
     private Date addedDate;
     private boolean live;
     private boolean stock;

     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "category_id")
     private Category category;

}
