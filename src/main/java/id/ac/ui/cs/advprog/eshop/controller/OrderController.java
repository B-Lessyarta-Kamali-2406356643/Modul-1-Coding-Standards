package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final String VOUCHER = "VOUCHER_CODE";
    private static final String COD = "CASH_ON_DELIVERY";

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam("author") String author,
                                  @RequestParam("productName") String productName,
                                  @RequestParam("productQuantity") int productQuantity,
                                  Model model) {
        Product product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setProductName(productName);
        product.setProductQuantity(productQuantity);

        List<Product> products = new ArrayList<>();
        products.add(product);

        Order order = new Order(
                UUID.randomUUID().toString(),
                products,
                System.currentTimeMillis(),
                author
        );

        orderService.createOrder(order);
        model.addAttribute("createdOrder", order);
        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "orderHistoryForm";
    }

    @PostMapping("/history")
    public String historyOrderByAuthor(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "orderHistoryList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "paymentForm";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrder(@PathVariable String orderId,
                           @RequestParam("method") String method,
                           @RequestParam(value = "voucherCode", required = false) String voucherCode,
                           @RequestParam(value = "address", required = false) String address,
                           @RequestParam(value = "deliveryFee", required = false) String deliveryFee,
                           Model model) {
        Order order = orderService.findById(orderId);

        if (order == null) {
            model.addAttribute("errorMessage", "Order not found.");
            return "paymentCreated";
        }

        Map<String, String> paymentData = buildPaymentData(method, voucherCode, address, deliveryFee);
        Payment payment = paymentService.addPayment(order, method, paymentData);

        model.addAttribute("payment", payment);
        model.addAttribute("order", order);
        return "paymentCreated";
    }

    private Map<String, String> buildPaymentData(String method,
                                                 String voucherCode,
                                                 String address,
                                                 String deliveryFee) {
        Map<String, String> paymentData = new HashMap<>();

        if (VOUCHER.equals(method)) {
            paymentData.put("voucherCode", voucherCode);
        } else if (COD.equals(method)) {
            paymentData.put("address", address);
            paymentData.put("deliveryFee", deliveryFee);
        }

        return paymentData;
    }
}