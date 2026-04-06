package org.example.product_service.product.specification;

import org.example.product_service.product.Product;
import org.example.product_service.product.dto.ProductFilterDTO;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecs {

    public static Specification<Product> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) {
                return cb.between(root.get("price"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), min);
            } else {
                return cb.lessThanOrEqualTo(root.get("price"), max);
            }
        };
    }

    public static Specification<Product> stockBetween(Integer min, Integer max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min != null && max != null) {
                return cb.between(root.get("stockQuantity"), min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("stockQuantity"), min);
            } else {
                return cb.lessThanOrEqualTo(root.get("stockQuantity"), max);
            }
        };
    }

    public static Specification<Product> withFilters(ProductFilterDTO filter) {
        Specification<Product> spec = Specification.where(notDeleted());
        spec = spec.and(nameContains(filter.getName()));
        spec = spec.and(priceBetween(filter.getMinPrice(), filter.getMaxPrice()));
        spec = spec.and(stockBetween(filter.getMinStock(), filter.getMaxStock()));
        return spec;
    }
}
