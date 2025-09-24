package com.khoahd7621.youngblack.repositories;

import com.khoahd7621.youngblack.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Optional<Product> findByName(String name);

    public Page<Product> findAllByIsDeletedFalseAndIsVisibleTrue(Pageable pageable);

    public Page<Product> findAllByIsDeletedFalseAndIsVisibleTrueAndCategoryId(int categoryId, Pageable pageable);

    public Page<Product> findAllByIsDeletedFalseAndIsVisibleTrueAndNameLikeIgnoreCase(String name, Pageable pageable);

    public Optional<Product> findByIsDeletedFalseAndSlug(String slug);

    public List<Product> findAllByCategoryId(int categoryId);

    public Optional<Product> findByIsDeletedFalseAndId(int id);
}
