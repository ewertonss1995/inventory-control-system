package br.com.training.inventory_control_system.adapter.out.mappers;

import br.com.training.inventory_control_system.adapter.in.requests.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.domain.entities.Category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

//    @Mapping(target = "categotyId", ignore = true)
    Category toEntity(CategoryRequest request);

    GetCategoryResponse toGetCategoryResponse(Category entity);

    List<GetCategoryResponse> toGetCategoryResponseList(List<Category> entities);

//    @Mapping(target = "registrationDate", ignore = true)
//    @Mapping(target = "category.categoryId", source = "categoryId")
    void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category entity);
}
