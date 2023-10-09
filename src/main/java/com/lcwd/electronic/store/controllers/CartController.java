package com.lcwd.electronic.store.controllers;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.services.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    //logging purpose
    Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    //add Items to Cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(
            @PathVariable String userId,
            @RequestBody AddItemToCartRequest cartRequest
            ) {
        CartDto cartDto = cartService.addItemsToCart(userId, cartRequest);
        log.info("cartDto List size:" + cartDto.getCartItemsList().size());
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @DeleteMapping("/{userId}/item/{cartItemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(
            @PathVariable String userId,
            @PathVariable int cartItemId
    ) {
            cartService.removeItemFromCart(userId, cartItemId);

            ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                    .message("Item is removed from cart")
                    .status(HttpStatus.OK)
                    .success(true)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> getCartByUser(
            @PathVariable String userId
    ) {
        CartDto cartDto = cartService.getCartByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(cartDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(
            @PathVariable String userId
    ) {
        cartService.clearCart(userId);

        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message("Cart is cleared")
                .status(HttpStatus.OK)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
