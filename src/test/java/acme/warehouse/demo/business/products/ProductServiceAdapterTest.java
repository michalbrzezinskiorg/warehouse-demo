package acme.warehouse.demo.business.products;

import acme.warehouse.demo.eventstream.DomainEventsPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.business.products.ports.ProductQuery;
import utils.mocks.MockProvider;
import acme.warehouse.demo.web.products.dto.ProductDto;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.modelmapper.internal.util.Assert.isTrue;

class ProductServiceAdapterTest {

    private ProductQuery productQuery;
    private DomainEventsPublisher domainEventsPublisher;
    private ProductServiceAdapter productServiceAdapter;
    private Pageable pageableMock = PageRequest.of(0, 10);

    @BeforeEach
    void setup() {
        productQuery = mock(ProductQuery.class);
        domainEventsPublisher = mock(DomainEventsPublisher.class);
        productServiceAdapter = new ProductServiceAdapter(productQuery, domainEventsPublisher);
    }

    @Test
    void saveNewProduct() {
        // given
        ProductDto product = MockProvider.getProductDto();
        //when
        productServiceAdapter.saveNewProduct(product, null);
        //then
        verify(domainEventsPublisher).publishCreateProductEvent(any());
    }

    @Test
    void getProducts() {
        //given
        final Page<Product> expected = MockProvider.getProducts();
        when(productQuery.getProducts(pageableMock))
                .thenReturn(expected);
        //when
        final Page<Product> returned = productServiceAdapter.getProducts(pageableMock);
        //then
        isTrue(returned.getTotalElements() == expected.getTotalElements());
        returned.forEach(r -> isTrue(expected.toList().contains(r)));
        expected.forEach(r -> isTrue(returned.toList().contains(r)));
    }

    @Test
    void getProduct() {
        //given
        UUID id = UUID.randomUUID();
        Product expected = MockProvider.getSavedProduct(id);
        when(productQuery.getProductBy(id))
                .thenReturn(expected);
        // when
        Product product = productServiceAdapter.getProductBy(id);
        //then
        isTrue(product.equals(expected));
    }
}