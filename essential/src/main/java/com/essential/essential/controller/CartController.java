package com.essential.essential.controller;

import com.essential.essential.service.CartService;
import com.essential.essential.service.CartService.AddResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /** Consultarea cosului. */
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("items", cartService.getItems());
        model.addAttribute("total", cartService.getTotal());
        return "cart";
    }

    /** Adaugarea in cos. */
    @PostMapping("/add/{id}")
    public String add(@PathVariable Long id,
                      @RequestParam(defaultValue = "/shop") String returnUrl,
                      RedirectAttributes redirectAttributes) {

        AddResult result = cartService.add(id);
        switch (result) {
            case ADDED -> redirectAttributes.addFlashAttribute("message", "Product added to your bag.");
            case OUT_OF_STOCK -> redirectAttributes.addFlashAttribute("error",
                    "No more stock available for this product.");
            case NOT_FOUND -> redirectAttributes.addFlashAttribute("error", "This product no longer exists.");
        }
        return "redirect:" + safeLocalPath(returnUrl);
    }

    /** Schimbarea cantitatii unei linii din cos. */
    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam int quantity,
                         RedirectAttributes redirectAttributes) {

        int finalQuantity = cartService.updateQuantity(id, quantity);
        if (finalQuantity == 0) {
            redirectAttributes.addFlashAttribute("message", "Product removed from your bag.");
        } else if (finalQuantity < quantity) {
            redirectAttributes.addFlashAttribute("error",
                    "Only " + finalQuantity + " in stock - quantity adjusted.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Quantity updated.");
        }
        return "redirect:/cart";
    }

    /** Stergerea unui produs din cos. */
    @PostMapping("/remove/{id}")
    public String remove(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (cartService.remove(id)) {
            redirectAttributes.addFlashAttribute("message", "Product removed from your bag.");
        }
        return "redirect:/cart";
    }

    /** Golirea cosului. */
    @PostMapping("/clear")
    public String clear(RedirectAttributes redirectAttributes) {
        cartService.clear();
        redirectAttributes.addFlashAttribute("message", "Your bag is now empty.");
        return "redirect:/cart";
    }

    /** Accepta doar cai locale (ex: /shop?gender=Men), ca sa nu putem fi redirectati catre alt site. */
    private static String safeLocalPath(String path) {
        if (path == null || !path.startsWith("/") || path.startsWith("//") || path.contains("\\")) {
            return "/shop";
        }
        return path;
    }
}
