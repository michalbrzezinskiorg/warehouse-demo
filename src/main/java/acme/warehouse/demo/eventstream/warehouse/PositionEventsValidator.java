package acme.warehouse.demo.eventstream.warehouse;

import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.domain.PositionModification;
import acme.warehouse.demo.eventstream.warehouse.events.CreatePositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.ModifyPositionEvent;

public interface PositionEventsValidator {
    void sendPositionRejectedEvent(CreatePositionEvent createProductEvent, Position position, Exception e);
    void sendPositionModificationRejectedEvent(ModifyPositionEvent createProductEvent, PositionModification modification, Exception e);
    void validatePositionDoesNotExists(Position position);
}
