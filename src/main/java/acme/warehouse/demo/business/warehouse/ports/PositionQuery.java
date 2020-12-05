package acme.warehouse.demo.business.warehouse.ports;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import acme.warehouse.demo.business.warehouse.domain.Position;

import java.util.UUID;

public interface PositionQuery {

    Page<Position> getPositions(Pageable pageable);

    Position getPosition(UUID uuid);
}
