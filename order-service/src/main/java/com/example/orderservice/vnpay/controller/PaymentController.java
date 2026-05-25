package com.example.orderservice.vnpay.controller;

import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.vnpay.config.Config;
import com.example.orderservice.vnpay.dto.PaymentInfo;
import com.example.orderservice.vnpay.dto.PaymentRequest;
import com.example.orderservice.vnpay.dto.PaymentResDto;
import com.example.orderservice.vnpay.dto.PaymentResponse;
import com.example.orderservice.vnpay.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/create_payment")
    public ApiResponse<?> createPayment(HttpServletRequest req, HttpServletResponse resp, @RequestBody PaymentRequest paymentRequest) throws UnsupportedEncodingException {

        String orderType = "billpayment";

        long amount = paymentRequest.getAmount()*100;

        String vnp_TxnRef = paymentRequest.getOrderID();
        String vnp_IpAddr = Config.getIpAddress(req);
        String vnp_TmnCode = Config.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", Config.vnp_Version);
        vnp_Params.put("vnp_Command", Config.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");


        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");

//        String locate = req.getParameter("language");
//        if (locate != null && !locate.isEmpty()) {
//            vnp_Params.put("vnp_Locale", locate);
//        } else {
//            vnp_Params.put("vnp_Locale", "vn");
//        }
        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", Config.getIpAddress(req));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;

        PaymentResDto paymentResDto = PaymentResDto.builder()
                .status("1000")
                .message("Request payment success")
                .url(paymentUrl)
                .build();

        return ApiResponse.builder().result(paymentResDto).build();
    }

    @GetMapping("/payment-info")
    public PaymentResponse paymentInfo(
        @RequestParam String vnp_Amount,
        @RequestParam String vnp_BankCode,
        @RequestParam String vnp_TxnRef,
        @RequestParam String vnp_CardType,
        @RequestParam String vnp_ResponseCode
    ){
        var a = PaymentInfo.builder()
                .vnp_Amount(vnp_Amount)
                .vnp_BankCode(vnp_BankCode)
                .vnp_TxnRef(vnp_TxnRef)
                .vnp_CardType(vnp_CardType)
                .vnp_ResponseCode(vnp_ResponseCode)
                .build();
        System.out.println(a);
        return paymentService.handlePaymentInfo(a);
    }
}
