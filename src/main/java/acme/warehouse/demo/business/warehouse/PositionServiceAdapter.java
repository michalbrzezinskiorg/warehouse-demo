package acme.warehouse.demo.business.warehouse;

import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.domain.PositionModification;
import acme.warehouse.demo.business.warehouse.ports.PositionQuery;
import acme.warehouse.demo.eventstream.warehouse.events.CreatePositionEvent;
import acme.warehouse.demo.eventstream.warehouse.events.ModifyPositionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import acme.warehouse.demo.eventstream.DomainEventsPublisher;
import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.business.warehouse.dto.PositionDto;
import acme.warehouse.demo.business.warehouse.dto.PositionModificationDto;
import acme.warehouse.demo.business.warehouse.ports.ProductQueryForWarehouse;
import acme.warehouse.demo.web.warehouse.PositionService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class PositionServiceAdapter implements PositionService {

    private final PositionQuery positionQuery;
    private final ProductQueryForWarehouse productQuery;
    private final DomainEventsPublisher domainEventsPublisher;

    @Override
    public void saveNewPosition(PositionDto position, String authentication) {
        Product product = productQuery.getProduct(position.getProductId());
        Position newPosition = Position.builder()
                .productName(product.getName())
                .productId(product.getId())
                .quantity(position.getQuantity())
                .build();
        domainEventsPublisher.publishCreatePositionEvent(
                CreatePositionEvent.builder()
                        .position(newPosition)
                        .user(authentication)
                        .build());
        log.info("published CreatePositionEvent");
    }

    @Override
    public Page<Position> getPositions(Pageable pageable) {
        Page<Position> products = positionQuery.getPositions(pageable);
        log.info("found [{}] products", products.getSize());
        return products;
    }

    @Override
    public Position getPosition(UUID uuid) {
        Position product = positionQuery.getPosition(uuid);
        log.info("found [{}]", product);
        return product;
    }

    @Override
    public void modifyPosition(PositionModificationDto position, String authentication) {
        domainEventsPublisher.publishModifyPositionEvent(
                ModifyPositionEvent.builder()
                        .modification(
                                PositionModification.builder()
                                        .modification(position.getModification())
                                        .productId(position.getProductId())
                                        .productName(position.getProductName())
                                        .build()
                        )
                        .user(authentication)
                        .build());
        log.info("published ModifyPositionEvent");
    }

}
