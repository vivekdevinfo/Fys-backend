package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.size.SizeRequest;
import com.khoahd7621.youngblack.dtos.response.variantsize.VariantSizeResponse;
import com.khoahd7621.youngblack.entities.ProductVariant;
import com.khoahd7621.youngblack.entities.Size;
import com.khoahd7621.youngblack.entities.VariantSize;
import com.khoahd7621.youngblack.utils.SkuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VariantSizeMapper {

    @Autowired
    private SizeMapper sizeMapper;
    @Autowired
    private SkuUtil skuUtil;

    public List<VariantSize> toListVariantSizeWithProductVariant(List<SizeRequest> sizes, ProductVariant productVariant, String rootSku, String colorName) {
        return sizes.stream().map(sizeRequest ->
                        VariantSize.builder()
                                .sku(skuUtil.getProductSku(rootSku, colorName, sizeRequest.getSize()))
                                .isInStock(true)
                                .size(sizeMapper.toSize(sizeRequest))
                                .productVariant(productVariant).build())
                .collect(Collectors.toList());
    }

    public List<VariantSize> toListVariantSizeWithProductVariant(Size size, Set<ProductVariant> productVariants, String rootSku) {
        return productVariants.stream().map(productVariant ->
                        VariantSize.builder()
                                .sku(skuUtil.getProductSku(rootSku, productVariant.getColor().getName(), size.getSize()))
                                .isInStock(true)
                                .size(size)
                                .productVariant(productVariant).build())
                .collect(Collectors.toList());
    }

    public VariantSizeResponse toVariantSizeResponse(VariantSize variantSize) {
        return VariantSizeResponse.builder()
                .variantSizeId(variantSize.getId())
                .sku(variantSize.getSku())
                .isInStock(variantSize.isInStock())
                .size(sizeMapper.toSizeResponse(variantSize.getSize())).build();
    }

    public List<VariantSizeResponse> toListVariantSizeResponse(Set<VariantSize> variantSizes) {
        return variantSizes.stream().map(this::toVariantSizeResponse).collect(Collectors.toList());
    }
}
