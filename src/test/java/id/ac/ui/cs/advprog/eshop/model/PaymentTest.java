package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
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
    void testCreatePayment() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);

        assertNotNull(payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testSetStatus() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);

        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testSetMethod() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);

        payment.setMethod("CASH_ON_DELIVERY");

        assertEquals("CASH_ON_DELIVERY", payment.getMethod());
    }

    @Test
    void testSetPaymentData() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);
        Map<String, String> newData = new HashMap<>();
        newData.put("address", "Depok");
        newData.put("deliveryFee", "15000");

        payment.setPaymentData(newData);

        assertEquals(newData, payment.getPaymentData());
    }

    @Test
    void testSetOrder() {
        Payment payment = new Payment(order, "VOUCHER_CODE", paymentData);

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("2");
        product.setProductName("Sabun");
        product.setProductQuantity(1);
        products.add(product);

        Order newOrder = new Order("order-2", products, 1708570000L, "Kamali");
        payment.setOrder(newOrder);

        assertEquals(newOrder, payment.getOrder());
    }
}