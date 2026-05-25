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

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AddressService {
    AddressRepository addressRepository;

    public ApiResponse<List<Province>> getAllProvince(){
        var listProvince = addressRepository.findAll().get(0).getProvince();

        return ApiResponse.<List<Province>>builder()
                .result(listProvince)
                .build();

    }

    public ApiResponse<List<District>> getDistrictsByProvinceId(String provinceId){
        var listDistrict = addressRepository.findAll().get(0).getDistrict();

        return ApiResponse.<List<District>>builder()
                .result(listDistrict.stream()
                        .filter(district->district.getIdProvince()
                                .equals(provinceId)).toList())
                .build();
    }

    public ApiResponse<List<Commune>> getCommuneByDistrictID(String districtId){
        var listCommune = addressRepository.findAll().get(0).getCommune();

        return ApiResponse.<List<Commune>>builder()
                .result(listCommune.stream()
                        .filter(commune->commune.getIdDistrict()
                                .equals(districtId)).toList())
                .build();
    }
}
