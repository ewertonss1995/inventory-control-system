package br.com.training.inventory_control_system.domain.repositories;

import br.com.training.inventory_control_system.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByCategoryName(String categoryName);
}
