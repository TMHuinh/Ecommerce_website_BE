package com.example.voucherservice.controller;

import com.example.voucherservice.dto.response.ApiResponse;
import com.example.voucherservice.dto.request.UpdateVoucherQuantityRequest;
import com.example.voucherservice.dto.request.UpdateVoucherRequest;
import com.example.voucherservice.dto.request.VoucherRequest;
import com.example.voucherservice.dto.response.VoucherResponse;
import com.example.voucherservice.service.VoucherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherController {
    VoucherService voucherService;

    @GetMapping("/getAll")
    ApiResponse<List<VoucherResponse>> getAll(){
        return voucherService.getAll();
    }

    @GetMapping("/valid-vouchers")
    ApiResponse<List<VoucherResponse>> getValidVouchers(){
        return voucherService.getAllValidVouchers();
    }

    @GetMapping("/vouchers-freeship")
    ApiResponse<List<VoucherResponse>> getAllVouchersFreeShip(){
        return voucherService.getAllVouchersFreeShip();
    }

    @PostMapping("/create")
    ApiResponse<VoucherResponse> createVoucher(@RequestBody VoucherRequest voucherRequest){
        return voucherService.createVoucher(voucherRequest);
    }

    @PutMapping("/{voucherID}")
    ApiResponse<VoucherResponse> updateVoucher(@PathVariable String voucherID, @RequestBody UpdateVoucherRequest voucherRequest){
        return voucherService.updateVoucher(voucherID, voucherRequest);
    }

    @PutMapping("/use-voucher/{voucherID}")
    ApiResponse<?> updateVoucher(@PathVariable String voucherID){
        return voucherService.useVoucher(voucherID);
    }

    @PutMapping("/update-quantity/{voucherID}")
    ApiResponse<VoucherResponse> updateVoucher(@PathVariable String voucherID, @RequestBody UpdateVoucherQuantityRequest request){
        return voucherService.updateVoucherQuantity(voucherID, request);
    }

    @DeleteMapping("/{voucherId}")
    ApiResponse<?> delete(@PathVariable String voucherId){
        return voucherService.deleteVoucher(voucherId);
    }

}
