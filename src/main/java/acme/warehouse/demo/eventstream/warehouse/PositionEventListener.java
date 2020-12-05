package acme.warehouse.demo.eventstream.warehouse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import acme.warehouse.demo.business.warehouse.ports.PositionQuery;
import acme.warehouse.demo.eventstream.DomainEventsPublisher;
import acme.warehouse.demo.eventstream.warehouse.events.CreatePositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.ModifyPositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.PositionModificationRejectedEvent;
import acme.warehouse.demo.eventstream.warehouse.events.PositionRejectedEvent;
import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.domain.PositionModification;

@Component
@RequiredArgsConstructor
@Slf4j
class PositionEventListener {
    private final PositionCommandHandler positionCommandHandler;
    private final PositionQuery positionQuery;
    private final DomainEventsPublisher domainEventsPublisher;

    @Async
    @EventListener
    public void handleCreatePositionEvent(CreatePositionEvent createProductEvent) {
        Position position = createProductEvent.getPosition();
        try {
            if(getPosition(position)!=null) throw new RuntimeException("Product already exists with id: "+position.getProductId());
            log.info("handleCreatepositionEvent [{}]", createProductEvent);
            positionCommandHandler.save(position);
        } catch (Exception e) {
            log.error("handleCreatepositionEvent [{}] throws [{}]", createProductEvent, e.getMessage());
            domainEventsPublisher.publishPositionRejectedEvent(
                    PositionRejectedEvent.builder()
                            .position(position)
                            .message(e.getMessage())
                            .messageTo(createProductEvent.getUser())
                            .build());
        }
    }

    @Async
    @EventListener
    public void handleModifyPositionEvent(ModifyPositionEvent createProductEvent) {
        PositionModification modification = createProductEvent.getModification();
        try {
            log.info("handleModifyPositionEvent [{}]", createProductEvent);
            positionCommandHandler.modifyPositionIfPresent(modification);
        } catch (Exception e) {
            log.error("handleModifyPositionEvent [{}] throws [{}]", createProductEvent, e.getMessage());
            PositionModificationRejectedEvent event = PositionModificationRejectedEvent.builder()
                    .modification(modification)
                    .message(e.getMessage())
                    .messageTo(createProductEvent.getUser())
                    .build();
            log.error("PositionModificationRejectedEvent [{}] ", event);
            domainEventsPublisher.publishPositionModificationRejectedEvent(event);
        }
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
