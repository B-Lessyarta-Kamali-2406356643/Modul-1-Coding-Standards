package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentServiceImpl paymentService;
    private Order order;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl();
        ReflectionTestUtils.setField(paymentService, "paymentRepository", paymentRepository);

        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Laptop");
        product.setProductQuantity(1);

        List<Product> products = new ArrayList<>();
        products.add(product);

        order = new Order("order-1", products, 10000L, "customer-1");
    }

    @Test
    void testAddPaymentVoucherValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertNotNull(payment);
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentVoucherNullPaymentData() {
        Payment payment = paymentService.addPayment(order, "VOUCHER_CODE", null);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentVoucherNullVoucherCode() {
        Map<String, String> paymentData = new HashMap<>();

        Payment payment = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentVoucherWrongLength() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP123");

        Payment payment = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentVoucherWrongPrefix() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ABCDE123ABC45678");

        Payment payment = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentVoucherWrongDigitCount() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOPABCDEFGH123");

        Payment payment = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentCodValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Mawar No. 1");
        paymentData.put("deliveryFee", "15000");

        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        assertNotNull(payment);
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentCodNullPaymentData() {
        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", null);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentCodBlankAddress() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "   ");
        paymentData.put("deliveryFee", "15000");

        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentCodBlankDeliveryFee() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Mawar No. 1");
        paymentData.put("deliveryFee", "   ");

        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentUnsupportedMethod() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.addPayment(order, "BANK_TRANSFER", new HashMap<>())
        );

        assertEquals("Unsupported payment method", exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void testSetStatusSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "VOUCHER_CODE", data);

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertSame(payment, result);
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), result.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Jl. Mawar No. 1");
        data.put("deliveryFee", "15000");
        Payment payment = new Payment(order, "CASH_ON_DELIVERY", data);

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertSame(payment, result);
        assertEquals("REJECTED", result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), result.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testSetStatusInvalid() {
        Map<String, String> data = new HashMap<>();
        data.put("address", "Jl. Mawar No. 1");
        data.put("deliveryFee", "15000");
        Payment payment = new Payment(order, "CASH_ON_DELIVERY", data);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.setStatus(payment, "PENDING")
        );

        assertEquals("Invalid payment status", exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void testGetPayment() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "VOUCHER_CODE", data);

        when(paymentRepository.findById("payment-1")).thenReturn(payment);

        Payment result = paymentService.getPayment("payment-1");

        assertSame(payment, result);
        verify(paymentRepository).findById("payment-1");
    }

    @Test
    void testGetAllPayments() {
        Map<String, String> data1 = new HashMap<>();
        data1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment(order, "VOUCHER_CODE", data1);

        Map<String, String> data2 = new HashMap<>();
        data2.put("address", "Jl. Mawar No. 1");
        data2.put("deliveryFee", "15000");
        Payment payment2 = new Payment(order, "CASH_ON_DELIVERY", data2);

        List<Payment> payments = List.of(payment1, payment2);
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        assertSame(payments, result);
        verify(paymentRepository).findAll();
    }

    @Test
    void testAddPaymentCodNullAddress() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("deliveryFee", "15000");

        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testAddPaymentCodNullDeliveryFee() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("address", "Jl. Mawar No. 1");

        Payment payment = paymentService.addPayment(order, "CASH_ON_DELIVERY", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testSyncOrderStatusDoesNothingForOtherStatus() {
        Map<String, String> data = new HashMap<>();
        data.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "VOUCHER_CODE", data);

        ReflectionTestUtils.setField(payment, "status", "PENDING");

        String oldStatus = order.getStatus();

        ReflectionTestUtils.invokeMethod(paymentService, "syncOrderStatus", payment);

        assertEquals(oldStatus, order.getStatus());
    }

    @Test
    void testAddPaymentVoucherNoDigits() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOPABCDEFGHIJK");

        Payment payment = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertEquals("REJECTED", payment.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository).save(payment);
    }


}