package acme.warehouse.demo.web.warehouse;

import acme.warehouse.demo.business.warehouse.domain.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import acme.warehouse.demo.business.warehouse.dto.PositionDto;
import acme.warehouse.demo.business.warehouse.dto.PositionModificationDto;

import java.util.UUID;

public interface PositionService {
    void saveNewPosition(PositionDto position, String authentication);

    Page<Position> getPositions(Pageable pageable);

    Position getPosition(UUID uuid);

    void modifyPosition(PositionModificationDto position, String authentication);
}
