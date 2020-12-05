package acme.warehouse.demo.business.warehouse;

import acme.warehouse.demo.business.warehouse.ports.PositionQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import acme.warehouse.demo.business.warehouse.ports.ProductQueryForWarehouse;
import acme.warehouse.demo.eventstream.DomainEventsPublisher;

import static org.mockito.Mockito.mock;

class PositionServiceAdapterTest {

    private PositionQuery positionQuery;
    private ProductQueryForWarehouse productQuery;
    private DomainEventsPublisher domainEventsPublisher;
    private PositionServiceAdapter positionServiceAdapter;

    @BeforeEach
    void setup() {
        positionQuery = mock(PositionQuery.class);
        productQuery = mock(ProductQueryForWarehouse.class);
        domainEventsPublisher = mock(DomainEventsPublisher.class);
        positionServiceAdapter = new PositionServiceAdapter(positionQuery, productQuery, domainEventsPublisher);
    }

    @Test
    void saveNewPosition() {
        // given
        // when
        // then
    }

    @Test
    void getPositions() {
        // given
        // when
        // then
    }

    @Test
    void getPosition() {
        // given
        // when
        // then
    }

    @Test
    void modifyPosition() {
        // given
        // when
        // then
    }
}