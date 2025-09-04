package br.com.training.inventory_control_system.adapter.out.mappers;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;
import br.com.training.inventory_control_system.domain.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    Product toEntity(ProductRequest request);

    GetProductResponse toGetProductResponse(Product entity);

    List<GetProductResponse> toGetProductResponseList(List<Product> entities);

    @Mapping(target = "registrationDate", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product entity);

}
