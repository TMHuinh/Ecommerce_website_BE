package com.example.orderservice.vnpay.service;

import com.example.orderservice.enums.PaymentStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.vnpay.dto.PaymentInfo;
import com.example.orderservice.vnpay.dto.PaymentResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {

    OrderRepository orderRepository;

    public PaymentResponse handlePaymentInfo(PaymentInfo paymentInfo) {
        String orderID = paymentInfo.getVnp_TxnRef();
        String code = paymentInfo.getVnp_ResponseCode();
        if(code.equals("00")){
            var order = orderRepository.findById(orderID).orElse(null);
            if(order != null){
                order.setPayment(PaymentStatus.SUCCESSFUL_PAYMENT_WITH_VNPAY);
            }
            orderRepository.save(order);
            return PaymentResponse.builder()
                    .status(code)
                    .result(true)
                    .message("Payment successful")
                    .build();
        }else{
            var order = orderRepository.findById(orderID).orElse(null);
            orderRepository.delete(order);
            return PaymentResponse.builder()
                    .status(code)
                    .result(false)
                    .message("Payment failed")
                    .build();
        }

    }
}
