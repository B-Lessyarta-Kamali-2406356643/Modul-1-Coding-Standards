package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderController orderController;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    private Order order;
    private Payment payment;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-1", products, 1708560000L, "Kamali");

        orders = new ArrayList<>();
        orders.add(order);

        Map<String, String> voucherData = new HashMap<>();
        voucherData.put("voucherCode", "ESHOP1234ABC5678");

        payment = new Payment(order, "VOUCHER_CODE", voucherData);
        payment.setStatus("SUCCESS");
    }

    @Test
    void testCreateOrderPage() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createOrder"));
    }

    @Test
    void testHistoryOrderPage() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistoryForm"));
    }

    @Test
    void testHistoryOrderByAuthor() throws Exception {
        doReturn(orders).when(orderService).findAllByAuthor("Kamali");

        mockMvc.perform(post("/order/history")
                        .param("author", "Kamali"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistoryList"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attribute("author", "Kamali"));
    }

    @Test
    void testGetPayOrderPage() throws Exception {
        doReturn(order).when(orderService).findById("order-1");

        mockMvc.perform(get("/order/pay/order-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentForm"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attribute("order", order));
    }

    @Test
    void testPostPayOrderVoucher() throws Exception {
        doReturn(order).when(orderService).findById("order-1");
        doReturn(payment).when(paymentService)
                .addPayment(eq(order), eq("VOUCHER_CODE"), anyMap());

        mockMvc.perform(post("/order/pay/order-1")
                        .param("method", "VOUCHER_CODE")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentCreated"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attribute("payment", payment));
    }

    @Test
    void testPostPayOrderCashOnDelivery() throws Exception {
        Map<String, String> codData = new HashMap<>();
        codData.put("address", "Depok");
        codData.put("deliveryFee", "15000");

        Payment codPayment = new Payment(order, "CASH_ON_DELIVERY", codData);
        codPayment.setStatus("SUCCESS");

        doReturn(order).when(orderService).findById("order-1");
        doReturn(codPayment).when(paymentService)
                .addPayment(eq(order), eq("CASH_ON_DELIVERY"), anyMap());

        mockMvc.perform(post("/order/pay/order-1")
                        .param("method", "CASH_ON_DELIVERY")
                        .param("address", "Depok")
                        .param("deliveryFee", "15000"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentCreated"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attribute("payment", codPayment));
    }

    @Test
    void testBuildPaymentDataVoucher() {
        Map<String, String> result = ReflectionTestUtils.invokeMethod(
                orderController,
                "buildPaymentData",
                "VOUCHER_CODE",
                "ESHOP1234ABC5678",
                null,
                null
        );

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ESHOP1234ABC5678", result.get("voucherCode"));
    }

    @Test
    void testBuildPaymentDataCod() {
        Map<String, String> result = ReflectionTestUtils.invokeMethod(
                orderController,
                "buildPaymentData",
                "CASH_ON_DELIVERY",
                null,
                "Jl. Mawar No. 1",
                "15000"
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Jl. Mawar No. 1", result.get("address"));
        assertEquals("15000", result.get("deliveryFee"));
    }

    @Test
    void testBuildPaymentDataUnknownMethodReturnsEmptyMap() {
        Map<String, String> result = ReflectionTestUtils.invokeMethod(
                orderController,
                "buildPaymentData",
                "BANK_TRANSFER",
                "ESHOP1234ABC5678",
                "Jl. Mawar No. 1",
                "15000"
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}