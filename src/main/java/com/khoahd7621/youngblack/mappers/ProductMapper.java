package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.product.CreateNewProductRequest;
import com.khoahd7621.youngblack.dtos.response.product.ProductAdminResponse;
import com.khoahd7621.youngblack.dtos.response.product.ProductResponse;
import com.khoahd7621.youngblack.entities.Product;
import com.khoahd7621.youngblack.utils.SlugUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProductMapper {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SlugUtil slugUtil;

    public Product toProduct(CreateNewProductRequest createNewProductRequest) {
        return Product.builder()
                .name(createNewProductRequest.getName())
                .description(createNewProductRequest.getDescription())
                .slug(slugUtil.getSlug(createNewProductRequest.getName()))
                .price(createNewProductRequest.getPrice())
                .primaryImageName(createNewProductRequest.getPrimaryImageName())
                .primaryImageUrl(createNewProductRequest.getPrimaryImageUrl())
                .secondaryImageName(createNewProductRequest.getSecondaryImageName())
                .secondaryImageUrl(createNewProductRequest.getSecondaryImageUrl())
                .createdAt(new Date())
                .updatedAt(new Date())
                .isVisible(createNewProductRequest.getIsVisible())
                .isDeleted(false)
                .category(categoryMapper.toCategory(createNewProductRequest.getCategory()))
                .build();
    }

    public ProductAdminResponse toProductAdminResponse(Product product) {
        Date currentDate = new Date();
        boolean isPromotion = product.getEndDateDiscount() != null
                && product.getStartDateDiscount() != null
                && currentDate.after(product.getStartDateDiscount())
                && currentDate.before(product.getEndDateDiscount());
        return ProductAdminResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .startDateDiscount(product.getStartDateDiscount())
                .endDateDiscount(product.getEndDateDiscount())
                .slug(product.getSlug())
                .primaryImageName(product.getPrimaryImageName())
                .primaryImageUrl(product.getPrimaryImageUrl())
                .secondaryImageName(product.getSecondaryImageName())
                .secondaryImageUrl(product.getSecondaryImageUrl())
                .isVisible(product.isVisible())
                .isPromotion(isPromotion)
                .category(categoryMapper.toCategoryResponse(product.getCategory())).build();
    }

    public ProductResponse toProductResponse(Product product) {
        long discountPrice = 0;
        Date startDateDiscount = null;
        Date endDateDiscount = null;
        boolean isPromotion = false;
        Date currentDate = new Date();
        if (product.getEndDateDiscount() != null
                && product.getStartDateDiscount() != null
                && currentDate.after(product.getStartDateDiscount())
                && currentDate.before(product.getEndDateDiscount())) {
            discountPrice = product.getDiscountPrice();
            startDateDiscount = product.getStartDateDiscount();
            endDateDiscount = product.getEndDateDiscount();
            isPromotion = true;
        }
        return ProductResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPrice(discountPrice)
                .startDateDiscount(startDateDiscount)
                .endDateDiscount(endDateDiscount)
                .slug(product.getSlug())
                .primaryImageName(product.getPrimaryImageName())
                .primaryImageUrl(product.getPrimaryImageUrl())
                .secondaryImageName(product.getSecondaryImageName())
                .secondaryImageUrl(product.getSecondaryImageUrl())
                .isVisible(product.isVisible())
                .isPromotion(isPromotion)
                .category(categoryMapper.toCategoryResponse(product.getCategory()))
                .build();
    }
}
