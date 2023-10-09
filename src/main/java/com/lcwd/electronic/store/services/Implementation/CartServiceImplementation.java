package com.lcwd.electronic.store.services.Implementation;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;
import lombok.Builder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("lombok")
public class CartServiceImplementation implements CartService {

    //logging purpose
    Logger log = LoggerFactory.getLogger(CartServiceImplementation.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemsToCart(String userId, AddItemToCartRequest addItemToCartRequest) {
        String productId = addItemToCartRequest.getProductId();
        int quantity = addItemToCartRequest.getQuantity();

        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not found with given Id"));
        //fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found ith given Id"));
        //declare cart reference
        Cart cart = null;
        try {
            //now find the cart of a user
            cart = cartRepository.findByUser(user).get();
        }
        catch (NoSuchElementException e) {
            cart = new Cart();
            //set cartId and createdAt fields
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //fetch existing cartItems list
        List<CartItem> cartItemsList = cart.getCartItemsList();
        //check if current product already present in cartItems List
        AtomicBoolean isCartItemExisting = new AtomicBoolean(false);
        List<CartItem> updatedCartItemsList = cartItemsList.stream().map(item -> {
             if(item.getProduct().getProductId().equals(productId)) {
                 item.setQuantity(quantity);
                 item.setTotalPrice(quantity * product.getDiscountedPrice());
                 isCartItemExisting.set(true);
             }
             return item;
        }).collect(Collectors.toList());

        //update the list linked with Cart
        cart.setCartItemsList(updatedCartItemsList);

        if (!isCartItemExisting.get()) {
            log.info("New Cart Item created");
            //perform cart operation
            //If cartItem is a new one
            //create cartItem
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .build();

            //add this newly created cartItem into cartItem list
            cart.getCartItemsList().add(cartItem);
            log.info("cartItemId:" + cartItem.getCartItemId());
            log.info("Size:" + cartItemsList.size());
        }

        //set User for the newly created cart
        cart.setUser(user);

        //update the cart table in Database
        Cart updatedCart = cartRepository.save(cart);
        log.info("Cart Created Successfully");
        //convert the cart entity into Dto
        CartDto updatedCartDto = mapper.map(updatedCart, CartDto.class);
        log.info("Conversion done");

        return updatedCartDto;
    }

    @Override
    public void removeItemFromCart(String userId, int cartItemId) {
           CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item not found"));
           //delete this item from cart
           cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        //fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found ith given Id"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart for User not found"));

        //clear the list associated with this cart
        cart.getCartItemsList().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        //fetch the user
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found ith given Id"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart for User not found"));

        CartDto cartDto = mapper.map(cart, CartDto.class);
        return cartDto;
    }
}
