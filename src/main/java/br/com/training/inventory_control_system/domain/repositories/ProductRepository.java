package br.com.training.inventory_control_system.domain.repositories;

import br.com.training.inventory_control_system.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p JOIN p.category c WHERE p.productId = :productId")
    Optional<Product> findProductWithCategory(@Param("productId") Integer productId);
}
