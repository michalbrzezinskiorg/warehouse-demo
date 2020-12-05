package acme.warehouse.demo.database.warehouse;

import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.ports.PositionQuery;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import acme.warehouse.demo.config.exceptions.ResourceNotFoundException;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
class PositionRepositoryQueryAdapter implements PositionQuery {

    private final PositionRepository positionRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Position> getPositions(Pageable pageable) {
        return positionRepository.findAll(pageable).map(e -> modelMapper.map(e, Position.class));
    }

    @Override
    public Position getPosition(UUID uuid) {
        return positionRepository.findById(uuid).map(e -> modelMapper.map(e, Position.class))
                .orElseThrow(()->new ResourceNotFoundException("could not found POSITION by id: "+uuid));
    }
}
