package com.example.orderservice.vnpay.controller;

import com.example.orderservice.vnpay.config.Config;
import com.example.orderservice.vnpay.dto.PaymentInfo;
import com.example.orderservice.vnpay.dto.PaymentResponse;
import com.example.orderservice.vnpay.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping(
            value = "/create_payment",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String createPayment(
            HttpServletRequest req,
            @RequestParam("amount") long amount,
            @RequestParam("orderID") String orderID
    ) throws UnsupportedEncodingException {

        String orderType = "billpayment";

        long vnpAmount = amount * 100;

        String vnp_TxnRef = orderID;
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();

        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnpAmount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", Config.getIpAddress(req));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);

        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        Iterator<String> itr = fieldNames.iterator();

        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName);
                hashData.append("=");
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append("=");
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));

                if (itr.hasNext()) {
                    hashData.append("&");
                    query.append("&");
                }
            }
        }

        String queryUrl = query.toString();

        String vnp_SecureHash = Config.hmacSHA512(
                Config.vnp_HashSecret,
                hashData.toString()
        );

        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        return Config.vnp_PayUrl + "?" + queryUrl;
    }

    @GetMapping(
            value = "/payment-info",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RedirectView paymentInfo(
            @RequestParam String vnp_Amount,
            @RequestParam(required = false) String vnp_BankCode,
            @RequestParam String vnp_TxnRef,
            @RequestParam(required = false) String vnp_CardType,
            @RequestParam String vnp_ResponseCode
    ) {
        PaymentInfo paymentInfo = PaymentInfo.builder()
                .vnp_Amount(vnp_Amount)
                .vnp_BankCode(vnp_BankCode)
                .vnp_TxnRef(vnp_TxnRef)
                .vnp_CardType(vnp_CardType)
                .vnp_ResponseCode(vnp_ResponseCode)
                .build();

        PaymentResponse response = paymentService.handlePaymentInfo(paymentInfo);

        if (response.isResult()) {

            String redirectUrl = UriComponentsBuilder
                    .fromUriString("http://localhost:3000/payment-success")
                    .queryParam("orderId", vnp_TxnRef)
                    .queryParam("amount", Long.parseLong(vnp_Amount) / 100)
                    .build()
                    .encode()
                    .toUriString();

            return new RedirectView(redirectUrl);
        }

        return new RedirectView(
                "http://localhost:3000/payment-fail?orderID=" + vnp_TxnRef
        );
    }
}