package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-1", products, 1708560000L, "Kamali");

        paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testSaveCreate() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);

        Payment result = paymentRepository.save(payment);

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), paymentRepository.findById(payment.getId()).getId());
    }

    @Test
    void testSaveUpdate() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);
        paymentRepository.save(payment);

        payment.setStatus("SUCCESS");
        Payment result = paymentRepository.save(payment);

        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", paymentRepository.findById(payment.getId()).getStatus());
    }

    @Test
    void testSaveUpdateKeepsSizeOne() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);
        paymentRepository.save(payment);

        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        assertEquals(1, paymentRepository.findAll().size());
        assertEquals("SUCCESS", paymentRepository.findById(payment.getId()).getStatus());
    }

    @Test
    void testFindByIdFound() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);
        paymentRepository.save(payment);

        Payment result = paymentRepository.findById(payment.getId());

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testFindByIdNotFound() {
        Payment result = paymentRepository.findById("not-found");

        assertNull(result);
    }

    @Test
    void testFindByIdIfRepositoryEmpty() {
        Payment result = paymentRepository.findById("payment-x");

        assertNull(result);
    }

    @Test
    void testFindByIdWithNullId() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);
        paymentRepository.save(payment);

        Payment result = paymentRepository.findById(null);

        assertNull(result);
    }

    @Test
    void testFindAllIfEmpty() {
        List<Payment> result = paymentRepository.findAll();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllIfNotEmpty() {
        Payment payment1 = new Payment(order, "VOUCHER_CODE", paymentData);
        Payment payment2 = new Payment(order, "VOUCHER_CODE", paymentData);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> result = paymentRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(payment1.getId(), result.get(0).getId());
        assertEquals(payment2.getId(), result.get(1).getId());
    }

    @Test
    void testFindAllReturnsNewList() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);
        paymentRepository.save(payment);

        List<Payment> result = paymentRepository.findAll();
        result.clear();

        assertEquals(1, paymentRepository.findAll().size());
    }
}