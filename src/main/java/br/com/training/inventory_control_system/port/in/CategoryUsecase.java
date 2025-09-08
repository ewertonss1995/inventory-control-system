package br.com.training.inventory_control_system.port.in;

import br.com.training.inventory_control_system.adapter.in.requests.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;

import java.util.List;

public interface CategoryUsecase {
    void saveCategory(CategoryRequest request);
    GetCategoryResponse getCategory(Integer categoryId);
    List<GetCategoryResponse> getCategories();
    void updateCategory(Integer categoryId, CategoryRequest request);
    void deleteCategory(Integer categoryId);
}
