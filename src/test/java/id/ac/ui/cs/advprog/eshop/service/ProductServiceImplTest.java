package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    ProductRepository productRepository;

    @Test
    void testCreate() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo");
        product.setProductQuantity(2);

        doReturn(product).when(productRepository).create(product);

        Product result = productService.create(product);

        assertEquals(product.getProductId(), result.getProductId());
        verify(productRepository, times(1)).create(product);
    }

    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> iterator = Collections.<Product>emptyList().iterator();
        doReturn(iterator).when(productRepository).findAll();

        List<Product> result = productService.findAll();

        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindAllIfNotEmpty() {
        Product product1 = new Product();
        product1.setProductId("1");
        product1.setProductName("A");
        product1.setProductQuantity(1);

        Product product2 = new Product();
        product2.setProductId("2");
        product2.setProductName("B");
        product2.setProductQuantity(2);

        Iterator<Product> iterator = Arrays.asList(product1, product2).iterator();
        doReturn(iterator).when(productRepository).findAll();

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getProductId());
        assertEquals("2", result.get(1).getProductId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdFound() {
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("A");
        product.setProductQuantity(1);

        doReturn(product).when(productRepository).findById("1");

        Product result = productService.findById("1");

        assertNotNull(result);
        assertEquals("1", result.getProductId());
        verify(productRepository, times(1)).findById("1");
    }

    @Test
    void testFindByIdNotFound() {
        doReturn(null).when(productRepository).findById("999");

        Product result = productService.findById("999");

        assertNull(result);
        verify(productRepository, times(1)).findById("999");
    }

    @Test
    void testEditProductFound() {
        Product edited = new Product();
        edited.setProductName("Updated");
        edited.setProductQuantity(10);

        Product resultProduct = new Product();
        resultProduct.setProductId("1");
        resultProduct.setProductName("Updated");
        resultProduct.setProductQuantity(10);

        doReturn(resultProduct).when(productRepository).edit("1", edited);

        Product result = productService.editProduct("1", edited);

        assertNotNull(result);
        assertEquals("Updated", result.getProductName());
        assertEquals(10, result.getProductQuantity());
        verify(productRepository, times(1)).edit("1", edited);
    }

    @Test
    void testEditProductNotFound() {
        Product edited = new Product();
        edited.setProductName("Updated");
        edited.setProductQuantity(10);

        doReturn(null).when(productRepository).edit("999", edited);

        Product result = productService.editProduct("999", edited);

        assertNull(result);
        verify(productRepository, times(1)).edit("999", edited);
    }

    @Test
    void testDeleteProductSuccess() {
        doReturn(true).when(productRepository).deleteProduct("1");

        boolean result = productService.deleteProduct("1");

        assertTrue(result);
        verify(productRepository, times(1)).deleteProduct("1");
    }

    @Test
    void testDeleteProductNotFound() {
        doReturn(false).when(productRepository).deleteProduct("999");

        boolean result = productService.deleteProduct("999");

        assertFalse(result);
        verify(productRepository, times(1)).deleteProduct("999");
    }
}