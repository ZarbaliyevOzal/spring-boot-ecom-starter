package org.example.product_service.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.product_service.product.dto.ProductFilterDTO;
import org.example.product_service.product.dto.ProductRequestDTO;
import org.example.product_service.product.dto.ProductResponseDTO;
import org.example.product_service.product.dto.UpdateProductRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGER')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        ProductResponseDTO productResponseDTO = productService.createProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponseDTO);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGER')")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id,
                                                             @RequestBody @Valid UpdateProductRequestDTO productRequestDTO) {
        productService.updateProduct(id, productRequestDTO);
        return ResponseEntity.ok().body(Map.of("message", "Product successfully updated"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGER','PRODUCT_VIEW')")
    public ResponseEntity<ProductResponseDTO> findProduct(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.findProduct(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGER','PRODUCT_VIEW')")
    public ResponseEntity<Page<ProductResponseDTO>> getProducts(ProductFilterDTO productFilterDTO,
                                                                Pageable pageable) {
        return ResponseEntity.ok().body(productService.getProducts(pageable, productFilterDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','PRODUCT_MANAGER')")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().body(Map.of("message", "Product successfully deleted"));
    }
}
