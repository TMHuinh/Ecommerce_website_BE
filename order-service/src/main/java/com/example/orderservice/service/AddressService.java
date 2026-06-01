package com.example.orderservice.service;

import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.repository.AddressRepository;
import com.example.orderservice.entity.Commune;
import com.example.orderservice.entity.District;
import com.example.orderservice.entity.Province;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressService {
    AddressRepository addressRepository;

    public ApiResponse<List<Province>> getAllProvince(){
        var address = getAddressData();

        return ApiResponse.<List<Province>>builder()
                .result(address == null ? Collections.emptyList() : address.getProvince())
                .build();

    }

    public ApiResponse<List<District>> getDistrictsByProvinceId(String provinceId){
        var address = getAddressData();
        var listDistrict = address == null ? Collections.<District>emptyList() : address.getDistrict();

        return ApiResponse.<List<District>>builder()
                .result(listDistrict.stream()
                        .filter(district->district.getIdProvince()
                                .equals(provinceId)).toList())
                .build();
    }

    public ApiResponse<List<Commune>> getCommuneByDistrictID(String districtId){
        var address = getAddressData();
        var listCommune = address == null ? Collections.<Commune>emptyList() : address.getCommune();

        return ApiResponse.<List<Commune>>builder()
                .result(listCommune.stream()
                        .filter(commune->commune.getIdDistrict()
                                .equals(districtId)).toList())
                .build();
    }

    private com.example.orderservice.entity.Address getAddressData() {
        return addressRepository.findAll().stream().findFirst().orElse(null);
    }
}
