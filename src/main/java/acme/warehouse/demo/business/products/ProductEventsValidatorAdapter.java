package acme.warehouse.demo.business.products;

import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.business.products.ports.ProductQuery;
import acme.warehouse.demo.eventstream.EventPublisherFacade;
import acme.warehouse.demo.eventstream.products.ProductEventsValidator;
import acme.warehouse.demo.eventstream.products.events.CreateProductEvent;
import acme.warehouse.demo.eventstream.products.events.ProductRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductEventsValidatorAdapter implements ProductEventsValidator {


    private final EventPublisherFacade eventPublisher;
    private ProductQuery productQuery;

    public void validateProductDoesNotExists(Product product) {
        if (getProduct(product) != null)
            throw new RuntimeException("Product already exists with id: " + product.getId());
    }

    public void sendProductRejectedEvent(CreateProductEvent createProductEvent, Product product) {
        ProductRejectedEvent event = ProductRejectedEvent.builder()
                .position(product)
                .messageTo(createProductEvent.getUser())
                .build();
        eventPublisher.publishProductRejectedEvent(event);
        log.error("ProductRejectedEvent [{}] ", event);
    }

    public Product getProduct(Product product) {
        Product o = null;
        try {
            o = productQuery.getProductBy(product.getId());
        } catch (RuntimeException e) {
            log.info("success PRODUCT id [{}] does not exists by", product.getId());
        }
        return o;
    }
}
