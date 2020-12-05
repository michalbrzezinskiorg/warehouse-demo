package acme.warehouse.demo.eventstream.products;

import acme.warehouse.demo.eventstream.DomainEventsPublisher;
import acme.warehouse.demo.eventstream.products.events.CreateProductEvent;
import acme.warehouse.demo.eventstream.products.events.ProductRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import acme.warehouse.demo.business.products.ports.ProductQuery;
import acme.warehouse.demo.business.products.domain.Product;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductEventListener {
    private final ProductCommandHandler productCommandHandler;
    private final ProductQuery productQuery;
    private final DomainEventsPublisher domainEventsPublisher;

    @Async
    @EventListener
    public void handleCreateProductEvent(CreateProductEvent createProductEvent) {
        Product product = createProductEvent.getProduct();
        try {
            if(getProduct(product)!=null) throw new RuntimeException("Product already exists with id: "+product.getId());
            log.info("handleCreateProductEvent [{}]", createProductEvent);
            productCommandHandler.save(product);
        } catch (Exception e) {
            log.error("handleCreateProductEvent [{}] throws [{}]", createProductEvent, e.getMessage());
            ProductRejectedEvent event = ProductRejectedEvent.builder()
                    .position(product)
                    .messageTo(createProductEvent.getUser())
                    .build();
            domainEventsPublisher.publishProductRejectedEvent(event);
            log.error("ProductRejectedEvent [{}] ", event);
        }
    }

    private Product getProduct(Product product) {
        Product o = null;
        try {
            o = productQuery.getProductBy(product.getId());
        } catch (RuntimeException e) {
            log.info("success PRODUCT id [{}] does not exists by", product.getId());
        }
        return o;
    }
}
