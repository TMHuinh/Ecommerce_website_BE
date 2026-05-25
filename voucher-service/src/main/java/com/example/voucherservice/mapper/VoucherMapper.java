package com.example.voucherservice.mapper;

import com.example.voucherservice.dto.request.UpdateVoucherRequest;
import com.example.voucherservice.dto.request.VoucherRequest;
import com.example.voucherservice.dto.response.VoucherResponse;
import com.example.voucherservice.entity.Voucher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface VoucherMapper {
    Voucher toVoucher(VoucherRequest request);

    VoucherResponse toVoucherResponse(Voucher voucher);

    void updateVoucher(@MappingTarget Voucher voucher, UpdateVoucherRequest request);
}
