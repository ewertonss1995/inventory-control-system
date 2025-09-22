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

    Category toEntity(CategoryRequest request);

    GetCategoryResponse toGetCategoryResponse(Category entity);

    List<GetCategoryResponse> toGetCategoryResponseList(List<Category> entities);

    void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category entity);
}
