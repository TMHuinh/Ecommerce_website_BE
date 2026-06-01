package com.example.voucherservice.service;

import com.example.voucherservice.dto.response.ApiResponse;
import com.example.voucherservice.dto.request.UpdateVoucherQuantityRequest;
import com.example.voucherservice.dto.request.UpdateVoucherRequest;
import com.example.voucherservice.dto.request.VoucherRequest;
import com.example.voucherservice.dto.response.VoucherResponse;
import com.example.voucherservice.entity.Voucher;
import com.example.voucherservice.exception.AppException;
import com.example.voucherservice.enums.ErrorCode;
import com.example.voucherservice.mapper.VoucherMapper;
import com.example.voucherservice.repository.VoucherRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class VoucherService {
    VoucherRepository voucherRepository;
    VoucherMapper voucherMapper;

    final ExecutorService executorService = Executors.newSingleThreadExecutor();

     public ApiResponse<VoucherResponse> createVoucher(VoucherRequest request){
        var voucher = voucherMapper.toVoucher(request);
        return ApiResponse.<VoucherResponse>builder()
                .result(voucherMapper.toVoucherResponse(voucherRepository.save(voucher)))
                .build();
    }

    public ApiResponse<List<VoucherResponse>> getAllVouchersFreeShip(){

        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherRepository.findByFreeShipTrue().stream().map(voucherMapper::toVoucherResponse).toList())
                .build();
    }

    public ApiResponse<VoucherResponse> updateVoucher(String voucherID, UpdateVoucherRequest request){
        var voucher = voucherRepository.findById(voucherID)
                .orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        voucherMapper.updateVoucher(voucher, request);

        return ApiResponse.<VoucherResponse>builder()
                .result(voucherMapper.toVoucherResponse(voucherRepository.save(voucher)))
                .build();
    }

    public ApiResponse<?> useVoucher(String voucherID) {
        synchronized (voucherID.intern()){
            // Tìm voucher, nếu không có thì trả về response với thông báo lỗi
            Optional<Voucher> optionalVoucher = voucherRepository.findById(voucherID);
            if (optionalVoucher.isEmpty()) {
                return ApiResponse.builder()
                        .code(404) // Mã lỗi phù hợp, ví dụ 404 Not Found
                        .result("Voucher not found")
                        .build();
            }

            Voucher voucher = optionalVoucher.get();

            // Kiểm tra nếu voucher đã hết số lượng
            if (voucher.getQuantity() <= 0) {
                return ApiResponse.builder()
                        .code(4001) // Mã lỗi tùy chỉnh
                        .result("Voucher out of stock")
                        .build();
            }

            // Giảm số lượng voucher và lưu lại
            voucher.setQuantity(voucher.getQuantity() - 1);
            voucher = voucherRepository.save(voucher);

            // Trả về thông tin voucher đã cập nhật
            return ApiResponse.<VoucherResponse>builder()
                    .result(voucherMapper.toVoucherResponse(voucher))
                    .build();
        }
    }


    public ApiResponse<VoucherResponse> updateVoucherQuantity(String voucherID, UpdateVoucherQuantityRequest request){
        var voucher = voucherRepository.findById(voucherID)
                .orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        voucher.setQuantity(request.getQuantity());

        return ApiResponse.<VoucherResponse>builder()
                .result(voucherMapper.toVoucherResponse(voucherRepository.save(voucher)))
                .build();
    }

    public ApiResponse<?> deleteVoucher(String id){
        var voucher = voucherRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

        voucherRepository.delete(voucher);

        return ApiResponse.builder()
                .result("Voucher deleted successfully")
                .build();
    }

    public ApiResponse<List<VoucherResponse>> getAll(){
        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherRepository.findAll().stream()
                        .map(voucherMapper::toVoucherResponse).toList())
                .build();
    }



    public ApiResponse<List<VoucherResponse>> getAllValidVouchers(){
        LocalDate localDate = LocalDate.now();
        List<Voucher> voucherList = voucherRepository
                .findAllByQuantityGreaterThanAndExpirationDateGreaterThanEqual(0,localDate);

        return ApiResponse.<List<VoucherResponse>>builder()
                .result(voucherList.stream().map(voucherMapper::toVoucherResponse).toList())
                .build();
    }
}
