package acme.warehouse.demo.eventstream;

import acme.warehouse.demo.eventstream.products.events.CreateProductEvent;
import acme.warehouse.demo.eventstream.products.events.ProductRejectedEvent;
import acme.warehouse.demo.eventstream.warehouse.events.CreatePositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.ModifyPositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.PositionModificationRejectedEvent;
import acme.warehouse.demo.eventstream.warehouse.events.PositionRejectedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisherFacade {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishCreateProductEvent(CreateProductEvent e) {
        applicationEventPublisher.publishEvent(e);
    }

    public void publishProductRejectedEvent(ProductRejectedEvent productRejectedEvent) {
        applicationEventPublisher.publishEvent(productRejectedEvent);
    }

    public void publishCreatePositionEvent(CreatePositionEvent e) {
        applicationEventPublisher.publishEvent(e);
    }

    public void publishModifyPositionEvent(ModifyPositionEvent e) {
        applicationEventPublisher.publishEvent(e);
    }

    public void publishPositionModificationRejectedEvent(PositionModificationRejectedEvent e) {
        applicationEventPublisher.publishEvent(e);
    }

    public void publishPositionRejectedEvent(PositionRejectedEvent e) {
        applicationEventPublisher.publishEvent(e);
    }
}
