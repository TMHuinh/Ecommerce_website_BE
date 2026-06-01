package com.example.orderservice.repository.httpClient;

import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.VoucherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "voucher-client", url = "http://localhost:8085")
public interface VoucherClient {

    @PutMapping("/use-voucher/{voucherID}")
    ApiResponse<VoucherResponse> updateVoucher(@PathVariable("voucherID") String voucherID);
}
