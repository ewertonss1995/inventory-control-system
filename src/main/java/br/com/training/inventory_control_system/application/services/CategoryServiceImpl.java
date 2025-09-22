package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.controllers.category.request.CategoryRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.CategoryMapper;
import br.com.training.inventory_control_system.adapter.out.responses.GetCategoryResponse;
import br.com.training.inventory_control_system.application.exception.GeneralCustomException;
import br.com.training.inventory_control_system.application.exception.category.CategoryNotFoundException;
import br.com.training.inventory_control_system.domain.entities.Category;
import br.com.training.inventory_control_system.domain.repositories.CategoryRepository;
import br.com.training.inventory_control_system.port.in.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryServiceImpl(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void saveCategory(CategoryRequest request) {
        try {
            Category entity = mapper.toEntity(request);
            repository.save(entity);
        } catch (Exception e) {
            LOGGER.error("[CategoryServiceImpl] - Unexpected error posting category: {}", e.getMessage());
            throw new GeneralCustomException(
                    String.format("Unable to save category: %s", e.getMessage()), e);
        }
    }

    @Override
    public GetCategoryResponse getCategory(Integer categoryId) {
        Category entity = repository.findById(categoryId).
                orElseThrow(() -> new CategoryNotFoundException(
                        String.format("Category with ID: %s was not found.", categoryId)));

        return mapper.toGetCategoryResponse(entity);
    }

    @Override
    public List<GetCategoryResponse> getCategories() {
        try {
            List<Category> entities = repository.findAll();
            return mapper.toGetCategoryResponseList(entities);

        } catch (Exception e) {
            LOGGER.error("[CategoryServiceImpl] - Unexpected error retrieving categories: {}", e.getMessage());
            throw new EmptyResultDataAccessException(
                    String.format("Unable to get categories: %s", e.getMessage()), 1);
        }
    }

    @Override
    public void updateCategory(Integer categoryId, CategoryRequest request) {
        try {
            Category entity = repository.findById(categoryId).
                    orElseThrow(() -> new CategoryNotFoundException(
                            String.format("Category with ID %s was not found.", categoryId)));

            mapper.updateEntityFromRequest(request, entity);

            entity.setUpdateDate(LocalDateTime.now());
            repository.save(entity);

        } catch (Exception e) {
            LOGGER.error("[CategoryServiceImpl] - Unexpected error updating categories: {}", e.getMessage());
            throw new GeneralCustomException(
                    String.format("Unable to update category: %s", e.getMessage()), e);
        }

        LOGGER.info("[CategoryServiceImpl] - Category with ID: {} was updated successfully.", categoryId);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        Category entity = repository.findById(categoryId).
                orElseThrow(() -> new CategoryNotFoundException(
                        String.format("Category with ID %s was not found.", categoryId)));

        repository.delete(entity);

        LOGGER.info("[CategoryServiceImpl] - Category with ID: {} was deleted successfully.", categoryId);
    }
}
