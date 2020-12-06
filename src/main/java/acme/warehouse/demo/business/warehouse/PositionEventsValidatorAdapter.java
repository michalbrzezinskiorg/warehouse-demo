package acme.warehouse.demo.business.warehouse;

import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.domain.PositionModification;
import acme.warehouse.demo.business.warehouse.ports.PositionQuery;
import acme.warehouse.demo.eventstream.EventPublisherFacade;
import acme.warehouse.demo.eventstream.warehouse.PositionEventsValidator;
import acme.warehouse.demo.eventstream.warehouse.events.CreatePositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.ModifyPositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.PositionModificationRejectedEvent;
import acme.warehouse.demo.eventstream.warehouse.events.PositionRejectedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class PositionEventsValidatorAdapter implements PositionEventsValidator {

    private final EventPublisherFacade eventPublisher;
    private final PositionQuery positionQuery;

    public void sendPositionRejectedEvent(CreatePositionEvent createProductEvent, Position position, Exception e) {
        PositionRejectedEvent event = PositionRejectedEvent.builder()
                .position(position)
                .message(e.getMessage())
                .messageTo(createProductEvent.getUser())
                .build();
        log.error("PositionModificationRejectedEvent [{}] ", event);
        eventPublisher.publishPositionRejectedEvent(
                event);
    }

    public void sendPositionModificationRejectedEvent(ModifyPositionEvent createProductEvent, PositionModification modification, Exception e) {
        PositionModificationRejectedEvent event = PositionModificationRejectedEvent.builder()
                .modification(modification)
                .message(e.getMessage())
                .messageTo(createProductEvent.getUser())
                .build();
        log.error("PositionModificationRejectedEvent [{}] ", event);
        eventPublisher.publishPositionModificationRejectedEvent(event);
    }

    public void validatePositionDoesNotExists(Position position) {
        if(getPosition(position)!=null)
            throw new RuntimeException("Product already exists with id: "+ position.getProductId());
    }

    private Position getPosition(Position position) {
        Position o = null;
        try {
            o = positionQuery.getPosition(position.getProductId());
        } catch (Exception e) {
            log.info("success: Position for product [{}] not exists", position.getProductId());
        }
        return o;
    }
}



