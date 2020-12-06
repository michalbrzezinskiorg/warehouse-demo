package acme.warehouse.demo.web.warehouse;

import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.dto.PositionDto;
import acme.warehouse.demo.business.warehouse.dto.PositionModificationDto;
import acme.warehouse.demo.config.beans.CurrentUser;
import acme.warehouse.demo.web.warehouse.ports.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("positions")
@RequiredArgsConstructor
@Slf4j
class PositionController {

    private final PositionService service;
    private final CurrentUser user;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void addNewPosition(@RequestBody PositionDto position) {
        log.info("addNewPosition [{}]", position);
        user.getCurrentUser().ifPresent(u -> service.saveNewPosition(position, u));
    }

    @PostMapping("modify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void modifyPosition(@RequestBody PositionModificationDto position) {
        log.info("modifyPosition [{}]", position);
        user.getCurrentUser().ifPresent(u->service.modifyPosition(position, u));
    }

    @GetMapping("all")
    Page<Position> getPositions(Pageable pageable) {
        log.info("getPositions [{}]", pageable);
        return service.getPositions(pageable);
    }

    @GetMapping("position/{uuid}")
    Position getPosition(@PathVariable UUID uuid) {
        log.info("getPosition [{}]", uuid);
        return service.getPosition(uuid);
    }
}
