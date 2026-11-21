package com.essential.essential.controller;

import com.essential.essential.service.CartService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Pune numarul de produse din cos in modelul TUTUROR paginilor (pentru "BAG (n)"). */
@ControllerAdvice
public class GlobalModelAttributes {

    private final CartService cartService;

    public GlobalModelAttributes(CartService cartService) {
        this.cartService = cartService;
    }

    @ModelAttribute("cartCount")
    public int cartCount() {
        return cartService.getCount();
    }
}
