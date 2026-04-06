package org.example.product_service.product;

import org.example.product_service.product.dto.ProductRequestDTO;
import org.example.product_service.product.dto.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private ProductRequestDTO productRequestDTO;
    private ProductResponseDTO productResponseDTO;
    private Product savedProduct;
    private Product product;

    @BeforeEach
    void setUp() {
        productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setName("product A");
        productRequestDTO.setPrice(BigDecimal.valueOf(15.50));
        productRequestDTO.setStockQuantity(5);

        savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName("Product A");
        savedProduct.setPrice(BigDecimal.valueOf(15.50));
        savedProduct.setStockQuantity(5);

        product = new Product();
        product.setName("Product A");
        product.setPrice(BigDecimal.valueOf(15.50));
        product.setStockQuantity(5);

        productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setId(savedProduct.getId());
        productResponseDTO.setName(savedProduct.getName());
        productResponseDTO.setPrice(savedProduct.getPrice());
        productResponseDTO.setStockQuantity(savedProduct.getStockQuantity());
    }

    @Test
    void shouldCreateProduct_whenRequestIsValid() {
        when(productMapper.toEntity(productRequestDTO)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toDTO(any(Product.class))).thenReturn(productResponseDTO);

        // create
        ProductResponseDTO result = productService.createProduct(productRequestDTO);

        // then
        assertNotNull(result);
        assertEquals(savedProduct.getName(), result.getName());
        assertEquals(savedProduct.getPrice(), result.getPrice());
        assertEquals(savedProduct.getStockQuantity(), result.getStockQuantity());

        verify(productMapper).toEntity(productRequestDTO);
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toDTO(any(Product.class));
    }
}