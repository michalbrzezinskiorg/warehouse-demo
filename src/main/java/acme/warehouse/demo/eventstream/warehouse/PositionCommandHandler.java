package acme.warehouse.demo.eventstream.warehouse;

import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.domain.PositionModification;

public interface PositionCommandHandler {
    void save(Position position);

    void modifyPositionIfPresent(PositionModification modification);
}
