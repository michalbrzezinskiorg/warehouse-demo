package acme.warehouse.demo.database.warehouse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import acme.warehouse.demo.config.exceptions.ResourceNotFoundException;
import acme.warehouse.demo.eventstream.warehouse.PositionCommandHandler;
import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.domain.PositionModification;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class PositionCommandHandlerAdapter implements PositionCommandHandler {
    private final PositionRepository positionRepository;
    private final ModelMapper modelMapper;

    @Override
    public void save(Position product) {
        PositionEntity map = modelMapper.map(product, PositionEntity.class);
        positionRepository.save(map);
    }

    @Override
    public void modifyPositionIfPresent(final PositionModification modification) {
        Optional<PositionEntity> positionEntity = positionRepository.findById(modification.getProductId());
        PositionEntity d = positionEntity.orElseThrow(()-> new ResourceNotFoundException("could not find such POSITION by id: "+modification.getProductId()));
        log.info("before modification [{}]", d);
        modifyPosition(d, modification);
        log.info("after modification [{}]", d);
    }

    private void modifyPosition(PositionEntity p, PositionModification modification) {
        long newState = p.getQuantity() + modification.getModification();
        if (newState >= 0) {
            p.setQuantity(newState);
        } else {
            throw new IllegalStateException("cannot modify value to " + newState);
        }
    }
}




