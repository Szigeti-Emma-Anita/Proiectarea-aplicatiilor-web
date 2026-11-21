package com.essential.essential.service;

import com.essential.essential.model.Product;
import com.essential.essential.repository.ProductRepository;
import com.essential.essential.service.CartService.AddResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CartServiceTest {

    private ProductRepository repository;
    private CartService cart;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ProductRepository.class);
        cart = new CartService(repository);

        Product tshirt = product(1L, "T-Shirt", "79.99", 2);
        Product hoodie = product(2L, "Hoodie", "159.99", 5);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(tshirt));
        Mockito.when(repository.findById(2L)).thenReturn(Optional.of(hoodie));
        Mockito.when(repository.findById(99L)).thenReturn(Optional.empty());
    }

    @Test
    void addingSameProductTwiceIncreasesQuantity() {
        assertThat(cart.add(1L)).isEqualTo(AddResult.ADDED);
        assertThat(cart.add(1L)).isEqualTo(AddResult.ADDED);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getCount()).isEqualTo(2);
    }

    @Test
    void cannotAddMoreThanStock() {
        cart.add(1L);
        cart.add(1L);

        assertThat(cart.add(1L)).isEqualTo(AddResult.OUT_OF_STOCK);
        assertThat(cart.getCount()).isEqualTo(2);
    }

    @Test
    void unknownProductIsNotAdded() {
        assertThat(cart.add(99L)).isEqualTo(AddResult.NOT_FOUND);
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    void totalIsSumOfSubtotals() {
        cart.add(1L);
        cart.add(1L);
        cart.add(2L);

        // 2 * 79.99 + 159.99 = 319.97
        assertThat(cart.getTotal()).isEqualByComparingTo(new BigDecimal("319.97"));
    }

    @Test
    void removeDeletesTheLine() {
        cart.add(1L);

        assertThat(cart.remove(1L)).isTrue();
        assertThat(cart.remove(1L)).isFalse();
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    void updateQuantityIsCappedByStock() {
        cart.add(1L);

        assertThat(cart.updateQuantity(1L, 10)).isEqualTo(2);
        assertThat(cart.getCount()).isEqualTo(2);
    }

    @Test
    void updateToZeroRemovesTheLine() {
        cart.add(2L);

        assertThat(cart.updateQuantity(2L, 0)).isEqualTo(0);
        assertThat(cart.isEmpty()).isTrue();
    }

    @Test
    void clearEmptiesTheCart() {
        cart.add(1L);
        cart.add(2L);

        cart.clear();

        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    private static Product product(Long id, String name, String price, int stock) {
        Product p = new Product(name, "desc", new BigDecimal(price), "Cat", "Men", "M", "Black", "http://img", stock);
        ReflectionTestUtils.setField(p, "id", id);
        return p;
    }
}
