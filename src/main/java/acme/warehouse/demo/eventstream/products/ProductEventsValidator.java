package acme.warehouse.demo.eventstream.products;

import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.eventstream.products.events.CreateProductEvent;

public interface ProductEventsValidator {
    void validateProductDoesNotExists(Product product);

    void sendProductRejectedEvent(CreateProductEvent createProductEvent, Product product);
}
