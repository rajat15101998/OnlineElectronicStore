package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;


public interface CartService {

    //add items to cart
    //If cart is not available for a User --> then create cart first and then add cart items
    //Else fetch cart and directly add cart Items into it

    CartDto addItemsToCart(String userId, AddItemToCartRequest addItemToCartRequest);

    //remove Item from cart
    void removeItemFromCart(String userId, int cartItemId);

    //remove all items from cart
    void clearCart(String userId);

    CartDto getCartByUser(String userId);

}
