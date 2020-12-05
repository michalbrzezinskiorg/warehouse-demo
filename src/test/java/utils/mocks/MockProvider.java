package utils.mocks;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.business.warehouse.dto.PositionDto;
import acme.warehouse.demo.web.products.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public class MockProvider {
    public static Product getSavedProduct(UUID id) {
        return Product.builder()
                .id(id)
                .name("eutanazol")
                .description("raz a dobrze")
                .build();
    }

    public static ProductDto getProductDto() {
        return ProductDto.builder()
                .description("raz a dobrze")
                .name("eutanazol")
                .build();
    }

    public static Page<Product> getProducts() {
        return new PageImpl<>(List.of(
                Product.builder()
                        .id(UUID.randomUUID())
                        .name("atopiryna")
                        .description("jak swędzi")
                        .build(),
                Product.builder()
                        .id(UUID.randomUUID())
                        .name("somnambucol")
                        .description("na nocny spacer")
                        .build(),
                Product.builder()
                        .id(UUID.randomUUID())
                        .name("yolo")
                        .description("cheez!")
                        .build(),
                Product.builder()
                        .id(UUID.randomUUID())
                        .name("eutanazol")
                        .description("raz a dobrze")
                        .build()
        ));
    }

    public static PositionDto getPositionForProductId(UUID productId) {
        return PositionDto.builder().productId(productId).quantity(100L).build();
    }
}
