package com.khoahd7621.youngblack.repositories;

import com.khoahd7621.youngblack.entities.VariantSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantSizeRepository extends JpaRepository<VariantSize, Long> {
    public List<VariantSize> findBySkuStartsWith(String sku);

    public List<VariantSize> findAllByIdIn(List<Long> idList);
}
