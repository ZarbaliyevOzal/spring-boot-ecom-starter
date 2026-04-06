package org.example.product_service.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.product_service.product.dto.ProductRequestDTO;
import org.example.product_service.product.dto.ProductResponseDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO createProduct(@Valid ProductRequestDTO productRequestDTO) {
        // create product
        Product product = productMapper.toEntity(productRequestDTO);

        // save product
        productRepository.save(product);

        return productMapper.toDTO(product);
    }
}
