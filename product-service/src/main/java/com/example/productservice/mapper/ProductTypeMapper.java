package com.example.productservice.mapper;

import com.example.productservice.dto.request.ProductCreateRequest;
import com.example.productservice.dto.request.ProductTypeCreateRequest;
import com.example.productservice.dto.request.ProductTypeUpdateRequest;
import com.example.productservice.dto.response.ProductTypeResponse;
import com.example.productservice.entity.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    ProductType toProductType(ProductTypeCreateRequest request);
    ProductTypeResponse toProductTypeResponse(ProductType productType);
    void updateProductType(ProductTypeUpdateRequest request, @MappingTarget ProductType productType);
}
