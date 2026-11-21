package com.essential.essential.service;

import com.essential.essential.model.CartItem;
import com.essential.essential.model.Product;
import com.essential.essential.repository.ProductRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Cosul de cumparaturi. Este @SessionScope: fiecare vizitator are propriul cos,
 * care traieste cat timp ii e valabila sesiunea.
 */
@Component
@SessionScope
public class CartService {

    public enum AddResult {
        ADDED, OUT_OF_STOCK, NOT_FOUND
    }

    private final ProductRepository productRepository;
    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public CartService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /** Adauga o bucata din produs in cos (daca mai este stoc). */
    public AddResult add(Long productId) {
        Optional<Product> found = productRepository.findById(productId);
        if (found.isEmpty()) {
            return AddResult.NOT_FOUND;
        }
        Product product = found.get();

        CartItem existing = items.get(productId);
        int currentQuantity = existing == null ? 0 : existing.getQuantity();
        if (currentQuantity + 1 > product.getStock()) {
            return AddResult.OUT_OF_STOCK;
        }

        if (existing == null) {
            items.put(productId, new CartItem(
                    product.getId(), product.getName(), product.getImageUrl(), product.getPrice(), 1));
        } else {
            existing.setQuantity(currentQuantity + 1);
            existing.setUnitPrice(product.getPrice());
        }
        return AddResult.ADDED;
    }

    /**
     * Seteaza cantitatea unei linii. Cantitate <= 0 sterge linia.
     * Cantitatea nu poate depasi stocul. Returneaza cantitatea finala (0 = linia nu mai exista).
     */
    public int updateQuantity(Long productId, int quantity) {
        CartItem item = items.get(productId);
        if (item == null) {
            return 0;
        }
        if (quantity <= 0) {
            items.remove(productId);
            return 0;
        }
        Optional<Product> found = productRepository.findById(productId);
        if (found.isEmpty()) {
            items.remove(productId);
            return 0;
        }
        int finalQuantity = Math.min(quantity, found.get().getStock());
        if (finalQuantity <= 0) {
            items.remove(productId);
            return 0;
        }
        item.setQuantity(finalQuantity);
        return finalQuantity;
    }

    /** Sterge complet un produs din cos. Returneaza true daca produsul era in cos. */
    public boolean remove(Long productId) {
        return items.remove(productId) != null;
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public BigDecimal getTotal() {
        return items.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Numarul total de bucati din cos (afisat la "BAG (n)"). */
    public int getCount() {
        return items.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
