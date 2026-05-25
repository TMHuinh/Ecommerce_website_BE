package com.example.productservice.controller;

import com.example.productservice.dto.request.ProductTypeCreateRequest;
import com.example.productservice.dto.request.ProductTypeUpdateRequest;
import com.example.productservice.dto.response.ApiResponse;
import com.example.productservice.dto.response.ProductTypeResponse;
import com.example.productservice.entity.ProductType;
import com.example.productservice.service.ProductTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-type")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductTypeController {
    ProductTypeService productTypeService;
    @GetMapping
    public ApiResponse<List<ProductTypeResponse>> getAllProductTypes() {
        return ApiResponse.<List<ProductTypeResponse>>builder()
                .result(productTypeService.getAllProductTypes())
                .build();
    }
    @PostMapping
    public ApiResponse<ProductTypeResponse> createProductType(@RequestBody ProductTypeCreateRequest request){
        return ApiResponse.<ProductTypeResponse>builder()
                .result(productTypeService.createProductType(request))
                .build();
    }
    @GetMapping("/productTypeID")
    public ApiResponse<ProductTypeResponse> getProductTypeById(@PathVariable String productTypeID){
        return ApiResponse.<ProductTypeResponse>builder()
                .result(productTypeService.getProductTypeByID(productTypeID))
                .build();
    }
    @PutMapping("/{productTypeID}")
    public ApiResponse<ProductTypeResponse> updateProductType(@PathVariable String productTypeID, @RequestBody ProductTypeUpdateRequest request){
        return ApiResponse.<ProductTypeResponse>builder()
                .result(productTypeService.updateProductType(productTypeID, request))
                .build();
    }
    @DeleteMapping("/{productTypeID}")
    public ApiResponse<?> deleteProductType(@PathVariable String productTypeID){
        productTypeService.deleteProductType(productTypeID);
        return ApiResponse.builder()
                .message("ProductType with ID " + productTypeID + " was deleted")
                .build();
    }
}
