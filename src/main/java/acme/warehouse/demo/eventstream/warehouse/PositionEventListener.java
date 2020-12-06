package acme.warehouse.demo.eventstream.warehouse;

import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.domain.PositionModification;
import acme.warehouse.demo.eventstream.warehouse.events.CreatePositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.ModifyPositionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class PositionEventListener {
    private final PositionCommandHandler positionCommandHandler;
    private final PositionEventsValidator positionEventsValidator;

    @Async
    @EventListener
    public void handleCreatePositionEvent(CreatePositionEvent createProductEvent) {
        Position position = createProductEvent.getPosition();
        try {
            positionEventsValidator.validatePositionDoesNotExists(position);
            log.info("handleCreatepositionEvent [{}]", createProductEvent);
            positionCommandHandler.save(position);
        } catch (Exception e) {
            log.error("handleCreatepositionEvent [{}] throws [{}]", createProductEvent, e.getMessage());
            positionEventsValidator.sendPositionRejectedEvent(createProductEvent, position, e);
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
            positionEventsValidator.sendPositionModificationRejectedEvent(createProductEvent, modification, e);
        }
    }

}
