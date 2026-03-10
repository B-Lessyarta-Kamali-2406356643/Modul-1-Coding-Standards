package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
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

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private Payment payment;
    private List<Payment> payments;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("1");
        product.setProductName("Sampo");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order("order-1", products, 1708560000L, "Kamali");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payment = new Payment(order, "VOUCHER_CODE", paymentData);
        payment.setStatus("SUCCESS");

        payments = new ArrayList<>();
        payments.add(payment);
    }

    @Test
    void testGetPaymentDetailForm() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetailForm"));
    }

    @Test
    void testGetPaymentDetailById() throws Exception {
        doReturn(payment).when(paymentService).getPayment(payment.getId());

        mockMvc.perform(get("/payment/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attributeExists("payment"))
                .andExpect(model().attribute("payment", payment));
    }

    @Test
    void testGetAdminPaymentList() throws Exception {
        doReturn(payments).when(paymentService).getAllPayments();

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void testGetAdminPaymentDetail() throws Exception {
        doReturn(payment).when(paymentService).getPayment(payment.getId());

        mockMvc.perform(get("/payment/admin/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void testPostAdminSetStatus() throws Exception {
        Payment updatedPayment = payment;
        updatedPayment.setStatus("REJECTED");

        doReturn(payment).when(paymentService).getPayment(payment.getId());
        doReturn(updatedPayment).when(paymentService).setStatus(payment, "REJECTED");

        mockMvc.perform(post("/payment/admin/set-status/" + payment.getId())
                        .param("status", "REJECTED"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }
}