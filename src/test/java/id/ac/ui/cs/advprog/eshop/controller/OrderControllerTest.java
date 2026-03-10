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
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-1", products, 1708560000L, "Kamali");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        payment = new Payment(order, "VOUCHER_CODE", paymentData);
        payment.setStatus("SUCCESS");
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
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPostPayOrderCod() throws Exception {
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
                .andExpect(model().attributeExists("payment"));
    }
}