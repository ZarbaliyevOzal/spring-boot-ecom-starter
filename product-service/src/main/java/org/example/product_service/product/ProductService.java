package org.example.product_service.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.product_service.exception.EntityNotFoundException;
import org.example.product_service.product.dto.ProductFilterDTO;
import org.example.product_service.product.dto.ProductRequestDTO;
import org.example.product_service.product.dto.ProductResponseDTO;
import org.example.product_service.product.dto.UpdateProductRequestDTO;
import org.example.product_service.product.specification.ProductSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGE')")
    public ProductResponseDTO createProduct(@Valid ProductRequestDTO productRequestDTO) {
        // create product
        Product product = productMapper.toEntity(productRequestDTO);

        // save product
        productRepository.save(product);

        return productMapper.toDTO(product);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGE')")
    public void updateProduct(Long id, @Valid UpdateProductRequestDTO productRequestDTO) {
        // find product
        Product product = productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        boolean hasUpdateField = false;

        if (productRequestDTO.getName() != null) {
            product.setName(productRequestDTO.getName());
            hasUpdateField = true;
        }

        if (productRequestDTO.getDescription() != null) {
            product.setDescription(productRequestDTO.getDescription());
            hasUpdateField = true;
        }

        if (productRequestDTO.getPrice() != null) {
            product.setPrice(productRequestDTO.getPrice());
            hasUpdateField = true;
        }

        if (productRequestDTO.getStockQuantity() != null) {
            product.setStockQuantity(productRequestDTO.getStockQuantity());
            hasUpdateField = true;
        }

        if (productRequestDTO.getIsActive() != null) {
            product.setIsActive(productRequestDTO.getIsActive());
            hasUpdateField = true;
        }

        if (hasUpdateField) productRepository.save(product);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGE','PRODUCT_VIEW')")
    public ProductResponseDTO findProduct(Long id) {
        return productMapper.toDTO(
                productRepository.findByIdAndDeletedAtIsNull(id)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id))
        );
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGE','PRODUCT_VIEW')")
    public Page<ProductResponseDTO> getProducts(Pageable pageable, ProductFilterDTO productFilterDTO) {
//        System.out.println("pageable: " + pageable);
        Page<Product> products = productRepository.findAll(
                ProductSpecs.withFilters(productFilterDTO),
                pageable
        );
        return products.map(productMapper::toDTO);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGE')")
    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        product.setDeletedAt(Instant.now());
        productRepository.save(product);
    }
}
