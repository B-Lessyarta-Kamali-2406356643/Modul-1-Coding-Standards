package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ProductRepositoryTest {

    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        productRepository.productData.clear();
    }


    @Test
    void testFindAllIfEmpty() {
        Iterator<Product> it = productRepository.findAll();
        assertFalse(it.hasNext());
    }

    @Test
    void testCreateAndFindAll_Single() {
        Product p = new Product();
        p.setProductId("id-1");
        p.setProductName("A");
        p.setProductQuantity(10);

        productRepository.create(p);

        Iterator<Product> it = productRepository.findAll();
        assertTrue(it.hasNext());
        Product saved = it.next();
        assertEquals("id-1", saved.getProductId());
        assertEquals("A", saved.getProductName());
        assertEquals(10, saved.getProductQuantity());
        assertFalse(it.hasNext());
    }

    @Test
    void testFindAllIfMoreThanOneProduct() {
        Product p1 = new Product();
        p1.setProductId("id-1");
        p1.setProductName("A");
        p1.setProductQuantity(1);
        productRepository.create(p1);

        Product p2 = new Product();
        p2.setProductId("id-2");
        p2.setProductName("B");
        p2.setProductQuantity(2);
        productRepository.create(p2);

        Iterator<Product> it = productRepository.findAll();
        assertTrue(it.hasNext());
        assertEquals("id-1", it.next().getProductId());
        assertTrue(it.hasNext());
        assertEquals("id-2", it.next().getProductId());
        assertFalse(it.hasNext());
    }


    @Test
    void testCreate_GenerateIdWhenNull() {
        Product existing = new Product();
        existing.setProductId("1");
        existing.setProductName("X");
        existing.setProductQuantity(1);
        productRepository.create(existing);

        Product p = new Product();
        p.setProductId(null);
        p.setProductName("Y");
        p.setProductQuantity(2);

        Product created = productRepository.create(p);

        assertEquals("2", created.getProductId());
        assertEquals("Y", created.getProductName());
        assertEquals(2, created.getProductQuantity());
    }

    @Test
    void testCreate_GenerateIdWhenBlank() {
        Product existing = new Product();
        existing.setProductId("10");
        existing.setProductName("X");
        existing.setProductQuantity(1);
        productRepository.create(existing);

        Product p = new Product();
        p.setProductId("   ");
        p.setProductName("Y");
        p.setProductQuantity(2);

        Product created = productRepository.create(p);

        assertEquals("11", created.getProductId());
    }

    @Test
    void testCreate_NegativeQuantityBecomesZero() {
        Product p = new Product();
        p.setProductId("id-1");
        p.setProductName("Neg");
        p.setProductQuantity(-5);

        Product created = productRepository.create(p);

        assertEquals(0, created.getProductQuantity());
    }

    @Test
    void testCreate_NullQuantityDoesNotChange() {
        Product p = new Product();
        p.setProductId("id-1");
        p.setProductName("NullQty");
        p.setProductQuantity(null);

        Product created = productRepository.create(p);

        assertNull(created.getProductQuantity());
    }


    @Test
    void testFindById_Found() {
        Product p = new Product();
        p.setProductId("42");
        p.setProductName("FindMe");
        p.setProductQuantity(1);
        productRepository.create(p);

        Product found = productRepository.findById("42");
        assertNotNull(found);
        assertEquals("42", found.getProductId());
    }

    @Test
    void testFindById_NotFound() {
        Product p = new Product();
        p.setProductId("1");
        p.setProductName("A");
        p.setProductQuantity(1);
        productRepository.create(p);

        assertNull(productRepository.findById("999"));
    }

    @Test
    void testFindById_SkipsNullIdInList() {

        Product nullId = new Product();
        nullId.setProductId(null);
        nullId.setProductName("NullId");
        nullId.setProductQuantity(1);
        productRepository.productData.add(nullId);

        assertNull(productRepository.findById("anything"));
    }


    @Test
    void testEdit_ReturnsNullIfNotExists() {
        Product edited = new Product();
        edited.setProductName("New");
        edited.setProductQuantity(5);

        assertNull(productRepository.edit("missing", edited));
    }

    @Test
    void testEdit_UpdatesNameAndQuantity_Positive() {
        Product p = new Product();
        p.setProductId("9");
        p.setProductName("Old");
        p.setProductQuantity(1);
        productRepository.create(p);

        Product edited = new Product();
        edited.setProductName("Updated");
        edited.setProductQuantity(99);

        Product result = productRepository.edit("9", edited);

        assertNotNull(result);
        assertEquals("Updated", result.getProductName());
        assertEquals(99, result.getProductQuantity());
    }

    @Test
    void testEdit_QuantityNullBecomesZero() {
        Product p = new Product();
        p.setProductId("9");
        p.setProductName("Old");
        p.setProductQuantity(1);
        productRepository.create(p);

        Product edited = new Product();
        edited.setProductName("Updated");
        edited.setProductQuantity(null);

        Product result = productRepository.edit("9", edited);

        assertNotNull(result);
        assertEquals(0, result.getProductQuantity());
    }

    @Test
    void testEdit_QuantityNegativeBecomesZero() {
        Product p = new Product();
        p.setProductId("9");
        p.setProductName("Old");
        p.setProductQuantity(1);
        productRepository.create(p);

        Product edited = new Product();
        edited.setProductName("Updated");
        edited.setProductQuantity(-1);

        Product result = productRepository.edit("9", edited);

        assertNotNull(result);
        assertEquals(0, result.getProductQuantity());
    }


    @Test
    void testGenerateId_EmptyListReturns1() {
        assertEquals("1", productRepository.generateId());
    }

    @Test
    void testGenerateId_IgnoresNullAndNonNumericIds() {
        Product nullId = new Product();
        nullId.setProductId(null);
        productRepository.productData.add(nullId);

        Product nonNumeric = new Product();
        nonNumeric.setProductId("abc");
        productRepository.productData.add(nonNumeric);

        Product num3 = new Product();
        num3.setProductId("3");
        productRepository.productData.add(num3);

        Product num2 = new Product();
        num2.setProductId("2");
        productRepository.productData.add(num2);

        assertEquals("4", productRepository.generateId());
    }


    @Test
    void testDeleteProduct_Success() {
        Product p1 = new Product();
        p1.setProductId("1");
        p1.setProductName("A");
        p1.setProductQuantity(1);
        productRepository.create(p1);

        Product p2 = new Product();
        p2.setProductId("2");
        p2.setProductName("B");
        p2.setProductQuantity(2);
        productRepository.create(p2);

        assertTrue(productRepository.deleteProduct("1"));
        assertNull(productRepository.findById("1"));
        assertNotNull(productRepository.findById("2"));
    }

    @Test
    void testDeleteProduct_NotFound() {
        Product p = new Product();
        p.setProductId("5");
        p.setProductName("X");
        p.setProductQuantity(1);
        productRepository.create(p);

        assertFalse(productRepository.deleteProduct("999"));
        assertNotNull(productRepository.findById("5"));
    }

    @Test
    void testDeleteProduct_SkipsNullIdInList() {
        Product nullId = new Product();
        nullId.setProductId(null);
        productRepository.productData.add(nullId);

        assertFalse(productRepository.deleteProduct("anything"));
        assertEquals(1, productRepository.productData.size());
    }
}
