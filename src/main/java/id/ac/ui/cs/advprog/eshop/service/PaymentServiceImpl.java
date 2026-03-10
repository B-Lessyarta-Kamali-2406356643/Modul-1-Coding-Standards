package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private static final String SUCCESS = "SUCCESS";
    private static final String REJECTED = "REJECTED";
    private static final String VOUCHER = "VOUCHER_CODE";
    private static final String COD = "CASH_ON_DELIVERY";

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(order, method, paymentData);

        if (VOUCHER.equals(method)) {
            payment.setStatus(validateVoucher(paymentData) ? SUCCESS : REJECTED);
        } else if (COD.equals(method)) {
            payment.setStatus(validateCod(paymentData) ? SUCCESS : REJECTED);
        } else {
            throw new IllegalArgumentException("Unsupported payment method");
        }

        if (SUCCESS.equals(payment.getStatus())) {
            order.setStatus(OrderStatus.SUCCESS.getValue());
        } else if (REJECTED.equals(payment.getStatus())) {
            order.setStatus(OrderStatus.FAILED.getValue());
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        if (!SUCCESS.equals(status) && !REJECTED.equals(status)) {
            throw new IllegalArgumentException("Invalid payment status");
        }

        payment.setStatus(status);

        if (SUCCESS.equals(status)) {
            payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        } else {
            payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private boolean validateVoucher(Map<String, String> paymentData) {
        if (paymentData == null) return false;

        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode == null) return false;
        if (voucherCode.length() != 16) return false;
        if (!voucherCode.startsWith("ESHOP")) return false;

        int digitCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }
        return digitCount == 8;
    }

    private boolean validateCod(Map<String, String> paymentData) {
        if (paymentData == null) return false;

        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");

        return !isBlank(address) && !isBlank(deliveryFee);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}