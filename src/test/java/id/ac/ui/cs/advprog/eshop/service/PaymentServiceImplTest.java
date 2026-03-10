package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;
    Map<String, String> validVoucherData;
    Map<String, String> invalidVoucherData;
    Map<String, String> validCodData;
    Map<String, String> invalidCodData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-1", products, 1708560000L, "Kamali");

        validVoucherData = new HashMap<>();
        validVoucherData.put("voucherCode", "ESHOP1234ABC5678");

        invalidVoucherData = new HashMap<>();
        invalidVoucherData.put("voucherCode", "SALAH");

        validCodData = new HashMap<>();
        validCodData.put("address", "Depok");
        validCodData.put("deliveryFee", "15000");

        invalidCodData = new HashMap<>();
        invalidCodData.put("address", "");
        invalidCodData.put("deliveryFee", "15000");
    }

    @Test
    void testAddPaymentVoucherSuccess() {
        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", validVoucherData);

        assertEquals("VOUCHER_CODE", result.getMethod());
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherRejected() {
        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", invalidVoucherData);

        assertEquals("REJECTED", result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCodSuccess() {
        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", validCodData);

        assertEquals("CASH_ON_DELIVERY", result.getMethod());
        assertEquals("SUCCESS", result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentCodRejected() {
        Payment result = paymentService.addPayment(order, "CASH_ON_DELIVERY", invalidCodData);

        assertEquals("REJECTED", result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentInvalidMethod() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.addPayment(order, "BANK_TRANSFER", validVoucherData));
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = new Payment(order, "VOUCHER_CODE", validVoucherData);

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = new Payment(order, "VOUCHER_CODE", validVoucherData);

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), payment.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = new Payment(order, "VOUCHER_CODE", validVoucherData);

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.setStatus(payment, "MEOW"));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void testGetPaymentFound() {
        Payment payment = new Payment(order, "VOUCHER_CODE", validVoucherData);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        doReturn(null).when(paymentRepository).findById("xxx");
        assertNull(paymentService.getPayment("xxx"));
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(order, "VOUCHER_CODE", validVoucherData));
        payments.add(new Payment(order, "CASH_ON_DELIVERY", validCodData));

        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> results = paymentService.getAllPayments();
        assertEquals(2, results.size());
    }
}