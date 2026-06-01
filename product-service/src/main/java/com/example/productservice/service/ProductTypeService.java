package com.example.productservice.service;

import com.example.productservice.dto.request.ProductTypeCreateRequest;
import com.example.productservice.dto.request.ProductTypeUpdateRequest;
import com.example.productservice.dto.response.ProductTypeResponse;
import com.example.productservice.entity.ProductType;
import com.example.productservice.enums.ErrorCode;
import com.example.productservice.exception.AppException;
import com.example.productservice.mapper.ProductTypeMapper;
import com.example.productservice.repository.ProductTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class ProductTypeService {
    ProductTypeRepository productTypeRepository;
    ProductTypeMapper productTypeMapper;

    public ProductTypeResponse createProductType(ProductTypeCreateRequest request){
        ProductType productType = productTypeMapper.toProductType(request);
        return productTypeMapper.toProductTypeResponse(productTypeRepository.save(productType));
    }
    public List<ProductTypeResponse> getAllProductTypes(){
        return productTypeRepository.findAll().stream().map(productTypeMapper::toProductTypeResponse).toList();
    }
    public ProductTypeResponse updateProductType(String id, ProductTypeUpdateRequest request){
        ProductType productType = productTypeRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));
       productTypeMapper.updateProductType(request,productType);
       return productTypeMapper.toProductTypeResponse(productTypeRepository.save(productType));
    }
    public void deleteProductType(String id){
        productTypeRepository.deleteById(id);
    }
    public ProductTypeResponse getProductTypeByID(String id){
        return productTypeMapper.toProductTypeResponse(productTypeRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND)));
    }
}
