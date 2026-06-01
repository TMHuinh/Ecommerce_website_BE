package com.example.orderservice.controller;

import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.service.AddressService;
import com.example.orderservice.entity.Commune;
import com.example.orderservice.entity.District;
import com.example.orderservice.entity.Province;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressService addressService;

    @GetMapping("/province")
    ApiResponse<List<Province>> getAllProvinces(){
        return addressService.getAllProvince();
    }

    @GetMapping("/district/{ProvinceID}")
    ApiResponse<List<District>> getDistrictByProvinceID(@PathVariable String ProvinceID){
        return addressService.getDistrictsByProvinceId(ProvinceID);
    }

    @GetMapping("/commune/{DistrictID}")
    ApiResponse<List<Commune>> getCommuneByDistrictID(@PathVariable String DistrictID){
        return addressService.getCommuneByDistrictID(DistrictID);
    }
}
