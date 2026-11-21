package com.essential.essential.controller;

import com.essential.essential.model.Product;
import com.essential.essential.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Controller
public class ShopController {

    private final ProductRepository productRepository;

    public ShopController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/shop")
    public String shop(@RequestParam(required = false) String gender,
                       @RequestParam(required = false) String category,
                       Model model) {

        String selectedGender = blankToNull(gender);
        String selectedCategory = blankToNull(category);

        List<Product> products;
        if (selectedGender != null && selectedCategory != null) {
            products = productRepository.findByGenderIgnoreCaseAndCategoryIgnoreCase(selectedGender, selectedCategory);
        } else if (selectedGender != null) {
            products = productRepository.findByGenderIgnoreCase(selectedGender);
        } else if (selectedCategory != null) {
            products = productRepository.findByCategoryIgnoreCase(selectedCategory);
        } else {
            products = productRepository.findAll();
        }

        // URL-ul curent (cu filtre) - dupa "Add to bag" ne intoarcem exact aici
        String returnUrl = UriComponentsBuilder.fromPath("/shop")
                .queryParamIfPresent("gender", Optional.ofNullable(selectedGender))
                .queryParamIfPresent("category", Optional.ofNullable(selectedCategory))
                .build()
                .encode()
                .toUriString();

        model.addAttribute("products", products);
        model.addAttribute("genders", productRepository.findDistinctGenders());
        model.addAttribute("categories", productRepository.findDistinctCategories());
        model.addAttribute("selectedGender", selectedGender);
        model.addAttribute("selectedCategory", selectedCategory);
        model.addAttribute("returnUrl", returnUrl);
        return "shop";
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
