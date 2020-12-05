package acme.warehouse.demo.eventstream.warehouse.events;

import acme.warehouse.demo.business.warehouse.domain.Position;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PositionRejectedEvent {
    Position position;
    String message;
    String messageTo;
}
