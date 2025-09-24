package com.khoahd7621.youngblack.services.impl;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.product.ProductResponse;
import com.khoahd7621.youngblack.entities.Category;
import com.khoahd7621.youngblack.entities.Product;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.ProductMapper;
import com.khoahd7621.youngblack.repositories.CategoryRepository;
import com.khoahd7621.youngblack.repositories.ProductRepository;
import com.khoahd7621.youngblack.services.ProductService;
import com.khoahd7621.youngblack.utils.PageableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PageableUtil pageableUtil;

    @Override
    public SuccessResponse<ListProductWithPaginateResponse> getAllProductsWithPaginateAndSort(Integer offset, Integer limit, String sortBase, String sorType)
            throws BadRequestException {
        Pageable pageable = pageableUtil.getPageable(offset, limit, sortBase, sorType);
        Page<Product> productPage = productRepository.findAllByIsDeletedFalseAndIsVisibleTrue(pageable);
        List<ProductResponse> productResponseList =
                productPage.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
        ListProductWithPaginateResponse listProductWithPaginateResponse =
                ListProductWithPaginateResponse.builder()
                        .products(productResponseList)
                        .totalRows(productPage.getTotalElements())
                        .totalPages(productPage.getTotalPages()).build();
        return new SuccessResponse<>(listProductWithPaginateResponse, "Get list products success.");
    }

    @Override
    public SuccessResponse<ListProductWithPaginateResponse> getAllProductsByCategoryNameWithPaginateAndSort(
            String categoryName, Integer offset, Integer limit, String sortBase, String sorType) throws BadRequestException {
        Optional<Category> categoryOptional = categoryRepository.findByNameAndIsDeletedFalse(categoryName.trim().toUpperCase());
        if (categoryOptional.isEmpty()) {
            throw new BadRequestException("Category \"" + categoryName + "\" does not exist.");
        }
        Pageable pageable = pageableUtil.getPageable(offset, limit, sortBase, sorType);
        Page<Product> productPage = productRepository.findAllByIsDeletedFalseAndIsVisibleTrueAndCategoryId(categoryOptional.get().getId(), pageable);
        List<ProductResponse> productResponseList =
                productPage.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
        ListProductWithPaginateResponse listProductWithPaginateResponse =
                ListProductWithPaginateResponse.builder()
                        .products(productResponseList)
                        .totalRows(productPage.getTotalElements())
                        .totalPages(productPage.getTotalPages()).build();
        return new SuccessResponse<>(listProductWithPaginateResponse, "Get list products success.");
    }

    @Override
    public SuccessResponse<ListProductWithPaginateResponse> getAllProductsSearchByNameWithPaginate(String query, Integer offset, Integer limit) {
        Page<Product> productPage = productRepository.findAllByIsDeletedFalseAndIsVisibleTrueAndNameLikeIgnoreCase("%" + query + "%", PageRequest.of(offset, limit));
        List<ProductResponse> productResponseList =
                productPage.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
        ListProductWithPaginateResponse listProductWithPaginateResponse =
                ListProductWithPaginateResponse.builder()
                        .products(productResponseList)
                        .totalRows(productPage.getTotalElements())
                        .totalPages(productPage.getTotalPages()).build();
        return new SuccessResponse<>(listProductWithPaginateResponse, "Get list products success.");
    }

    @Override
    public SuccessResponse<ListProductResponse> getNRelatedProductByCategoryId(int categoryId, int numberElements) throws NotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndIsDeletedFalse(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException("Don't exist category with this id.");
        }
        List<Product> productList = productRepository.findAllByCategoryId(categoryId);
        List<ProductResponse> productResponseList = new ArrayList<>();
        if (productList.isEmpty()) {
            return new SuccessResponse<>(ListProductResponse.builder().products(productResponseList).build(), "Related products is empty");
        }
        if (productList.size() <= numberElements) {
            productResponseList = productList.stream().map(productMapper::toProductResponse).collect(Collectors.toList());
            return new SuccessResponse<>(ListProductResponse.builder().products(productResponseList).build(), "Get related products success.");
        }
        Random random = new Random();
        for (int i = 0; i < numberElements; i++) {
            int randomIndex = random.nextInt(productList.size());
            productResponseList.add(productMapper.toProductResponse(productList.get(randomIndex)));
            productList.remove(randomIndex);
        }
        return new SuccessResponse<>(ListProductResponse.builder().products(productResponseList).build(), "Get related products success.");
    }

}
