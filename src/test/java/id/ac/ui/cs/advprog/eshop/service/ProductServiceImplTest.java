package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() throws Exception {
        productService = new ProductServiceImpl();
        productRepository = mock(ProductRepository.class);

        // inject mock into @Autowired field without changing production code
        Field repoField = ProductServiceImpl.class.getDeclaredField("productRepository");
        repoField.setAccessible(true);
        repoField.set(productService, productRepository);
    }

    @Test
    void create_callsRepository_andReturnsSameProduct() {
        Product p = new Product();
        p.setProductId("1");
        p.setProductName("A");
        p.setProductQuantity(10);

        Product result = productService.create(p);

        assertSame(p, result);
        verify(productRepository).create(p);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findAll_returnsListFromIterator() {
        Product p1 = new Product();
        p1.setProductId("1");
        Product p2 = new Product();
        p2.setProductId("2");

        Iterator<Product> it = List.of(p1, p2).iterator();
        when(productRepository.findAll()).thenReturn(it);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertSame(p1, result.get(0));
        assertSame(p2, result.get(1));
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findAll_whenEmptyIterator_returnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.<Product>of().iterator());

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findById_delegatesToRepository() {
        Product p = new Product();
        p.setProductId("42");

        when(productRepository.findById("42")).thenReturn(p);

        Product result = productService.findById("42");

        assertSame(p, result);
        verify(productRepository).findById("42");
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void editProduct_delegatesToRepository() {
        Product edited = new Product();
        edited.setProductName("Updated");

        when(productRepository.edit("9", edited)).thenReturn(edited);

        Product result = productService.editProduct("9", edited);

        assertSame(edited, result);
        verify(productRepository).edit("9", edited);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void deleteProduct_delegatesToRepository() {
        when(productRepository.deleteProduct("1")).thenReturn(true);

        boolean result = productService.deleteProduct("1");

        assertTrue(result);
        verify(productRepository).deleteProduct("1");
        verifyNoMoreInteractions(productRepository);
    }
}