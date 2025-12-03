package com.codewithmosh.store.services;


import com.codewithmosh.store.dtos.CartDTO;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRespository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private CartMapper cartMapper;
    private CartRespository cartRespository;
    private ProductRepository productRepository;

    public CartDTO createCart(){
        var cart = new Cart();
        cartRespository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addtoCart(UUID cartId, Long productId){
        var cart = cartRespository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        var product = productRepository.findById(productId).orElse(null);
        if(product == null){
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);
        cartRespository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDTO getCart(UUID cartId){
        var cart = cartRespository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        return cartMapper.toDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity){
        var cart = cartRespository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        var cartItem = cart.getItem(productId);

        if(cartItem == null){
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRespository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void removeItem(UUID cartId, Long productId){
        var cart = cartRespository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        cart.removeItem(productId);
        cartRespository.save(cart);
    }
    public void clearCart(UUID cartId){
        var cart = cartRespository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }
        cart.clear();
        cartRespository.save(cart);
    }

}
