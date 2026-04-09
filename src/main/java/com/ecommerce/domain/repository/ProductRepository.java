package com.ecommerce.domain.repository;

import com.ecommerce.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findByCategoryAndActiveTrue(String category);

    // Paginación
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);

    // Búsqueda avanzada
    @Query("SELECT p FROM Product p WHERE p.active = true " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:category IS NULL OR p.category = :category) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Product> search(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
}
