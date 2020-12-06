package acme.warehouse.demo.eventstream.products;

import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.eventstream.products.events.CreateProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductEventListener {
    private final ProductCommandHandler productCommandHandler;
    private final ProductEventsValidator productEventsValidator;

    @Async
    @EventListener
    public void handleCreateProductEvent(CreateProductEvent createProductEvent) {
        Product product = createProductEvent.getProduct();
        try {
            productEventsValidator.validateProductDoesNotExists(product);
            log.info("handleCreateProductEvent [{}]", createProductEvent);
            productCommandHandler.save(product);
        } catch (Exception e) {
            log.error("handleCreateProductEvent [{}] throws [{}]", createProductEvent, e.getMessage());
            productEventsValidator.sendProductRejectedEvent(createProductEvent, product);
        }
    }
}
