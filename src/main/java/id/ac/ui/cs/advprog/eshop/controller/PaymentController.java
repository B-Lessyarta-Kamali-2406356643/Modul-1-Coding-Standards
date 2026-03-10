package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/detail")
    public String paymentDetailForm() {
        return "paymentDetailForm";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "paymentDetail";
    }

    @GetMapping("/admin/list")
    public String adminPaymentList(Model model) {
        List<Payment> payments = paymentService.getAllPayments();
        model.addAttribute("payments", payments);
        return "paymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String adminPaymentDetail(@PathVariable String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "paymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String adminSetStatus(@PathVariable String paymentId,
                                 @RequestParam("status") String status,
                                 Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        Payment updatedPayment = paymentService.setStatus(payment, status);
        model.addAttribute("payment", updatedPayment);
        return "paymentAdminDetail";
    }
}