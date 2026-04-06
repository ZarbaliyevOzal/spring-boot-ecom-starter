package org.example.product_service.product;

import org.example.product_service.product.dto.ProductRequestDTO;
import org.example.product_service.product.dto.ProductResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO productRequestDTO) {
        Product product = new Product();
        product.setName(productRequestDTO.getName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());
        product.setStockQuantity(productRequestDTO.getStockQuantity());
        product.setIsActive(productRequestDTO.getIsActive() != null ? productRequestDTO.getIsActive() : true);
        return product;
    }

    public ProductResponseDTO toDTO(Product product) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(product.getId());
        productResponseDTO.setName(product.getName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setStockQuantity(product.getStockQuantity());
        productResponseDTO.setIsActive(product.getIsActive());
        return  productResponseDTO;
    }
}
