package acme.warehouse.demo.eventstream.warehouse.events;

import acme.warehouse.demo.business.warehouse.domain.PositionModification;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ModifyPositionEvent {
    PositionModification modification;
    String user;
}
