package com.ecommerce.application.service;

import com.ecommerce.application.dto.ProductRequest;
import com.ecommerce.application.dto.ProductResponse;
import com.ecommerce.domain.model.Product;
import com.ecommerce.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    public Page<ProductResponse> getAllActivePaged(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable).map(this::toResponse);
    }

    public Page<ProductResponse> search(String name, String category,
                                        BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.search(name, category, minPrice, maxPrice, pageable).map(this::toResponse);
    }
    public List<ProductResponse> getAllActive() {
        return productRepository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    public List<ProductResponse> getByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category).stream().map(this::toResponse).toList();
    }

    public ProductResponse getById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        return toResponse(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        var product = Product.builder()
                .name(request.name()).description(request.description())
                .price(request.price()).stock(request.stock())
                .imageUrl(request.imageUrl()).category(request.category())
                .build();
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStock(request.stock());
        product.setImageUrl(request.imageUrl());
        product.setCategory(request.category());
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        product.setActive(false);
        productRepository.save(product);
    }

    public List<ProductResponse> getAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getDescription(),
                p.getPrice(), p.getStock(), p.getImageUrl(),
                p.getCategory(), p.getActive(), p.getCreatedAt());
    }
}
