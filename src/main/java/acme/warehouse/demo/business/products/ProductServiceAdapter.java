package acme.warehouse.demo.business.products;

import acme.warehouse.demo.eventstream.DomainEventsPublisher;
import acme.warehouse.demo.eventstream.products.events.CreateProductEvent;
import acme.warehouse.demo.web.products.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.business.products.ports.ProductQuery;
import acme.warehouse.demo.web.products.dto.ProductDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class ProductServiceAdapter implements ProductService {
    private final ProductQuery productQuery;
    private final DomainEventsPublisher domainEventsPublisher;

    @Override
    public void saveNewProduct(ProductDto productDto, String authentication) {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .build();
        domainEventsPublisher.publishCreateProductEvent(CreateProductEvent.builder()
                .product(product)
                .user(authentication)
                .build());
    }

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productQuery.getProducts(pageable);
    }

    @Override
    public Product getProductBy(UUID uuid) {
        return productQuery.getProductBy(uuid);
    }
}
