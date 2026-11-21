package com.essential.essential.config;

import com.essential.essential.model.Product;
import com.essential.essential.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    /** Pozele produselor sunt in src/main/resources/static/img/products/ */
    private static final String IMAGE_PATH = "/img/products/";

    @Bean
    CommandLineRunner initDatabase(ProductRepository repository) {
        return args -> {
            if (repository.count() > 0) {
                return;
            }

            List<Product> products = List.of(
                product("Essential White T-Shirt", "Tricou alb minimalist, din bumbac 100%.", "79.99",
                        "T-Shirts", "Women", "M", "White", "women-white-tshirt.jpg", 20),
                product("Essential Navy T-Shirt", "Tricou bleumarin din bumbac 100%, croială clasică.", "79.99",
                        "T-Shirts", "Men", "L", "Navy", "men-navy-tshirt.jpg", 25),
                product("Essential Beige Hoodie", "Hanorac bej, confortabil, cu croială oversized.", "179.99",
                        "Hoodies", "Women", "M", "Beige", "women-beige-hoodie.jpg", 12),
                product("Classic Black Hoodie", "Hanorac negru cu glugă și buzunar frontal.", "159.99",
                        "Hoodies", "Men", "L", "Black", "men-black-hoodie.jpg", 15),
                product("Minimalist Black Dress", "Rochie neagră elegantă, fără mâneci, cu design simplu.", "229.99",
                        "Dresses", "Women", "S", "Black", "women-black-dress.jpg", 8),
                product("Classic Blue Jeans", "Blugi clasici albaștri, cu croială dreaptă.", "199.99",
                        "Pants", "Men", "L", "Blue", "men-blue-jeans.jpg", 10),
                product("Oversized Grey Sweater", "Pulover gri oversized, din tricot moale.", "159.99",
                        "Sweaters", "Women", "M", "Grey", "women-grey-sweater.jpg", 14),
                product("Classic Field Jacket", "Geacă kaki-oliv cu fermoar și buzunare cu clapă.", "249.99",
                        "Jackets", "Men", "L", "Olive", "men-olive-jacket.jpg", 6),
                product("High-Waist Blue Jeans", "Blugi albaștri cu talie înaltă și croială dreaptă.", "189.99",
                        "Pants", "Women", "S", "Blue", "women-blue-jeans.jpg", 16),
                product("Oxford Light Blue Shirt", "Cămașă albastru deschis, cu guler și nasturi.", "149.99",
                        "Shirts", "Men", "M", "Light Blue", "men-lightblue-shirt.jpg", 18),
                product("Pleated Midi Skirt", "Fustă midi plisată, în nuanță camel.", "149.99",
                        "Skirts", "Women", "M", "Camel", "women-camel-skirt.jpg", 10),
                product("Grey Jogger Pants", "Pantaloni de trening gri, cu manșete elastice.", "129.99",
                        "Pants", "Men", "L", "Grey", "men-grey-joggers.jpg", 17),
                product("Linen Cream Shirt", "Cămașă crem din in, cu nasturi și croială lejeră.", "139.99",
                        "Shirts", "Women", "M", "Cream", "women-cream-shirt.jpg", 13),
                product("Khaki Chino Shorts", "Pantaloni scurți kaki, ideali pentru vară.", "119.99",
                        "Shorts", "Men", "M", "Khaki", "men-khaki-shorts.jpg", 12),
                product("Long Wool Coat", "Palton lung camel, din amestec de lână.", "399.99",
                        "Coats", "Women", "S", "Camel", "women-camel-coat.jpg", 5),
                product("Charcoal Crewneck Sweater", "Pulover gri închis, cu guler rotund.", "169.99",
                        "Sweaters", "Men", "L", "Charcoal", "men-charcoal-sweater.jpg", 9)
            );

            repository.saveAll(products);
            log.info("{} produse au fost adaugate in baza de date.", products.size());
        };
    }

    private static Product product(String name, String description, String price,
                                   String category, String gender, String size,
                                   String color, String imageFile, int stock) {
        return new Product(name, description, new BigDecimal(price),
                category, gender, size, color, IMAGE_PATH + imageFile, stock);
    }
}