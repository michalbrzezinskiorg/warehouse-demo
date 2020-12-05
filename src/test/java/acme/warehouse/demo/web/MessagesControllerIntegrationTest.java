package acme.warehouse.demo.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.business.warehouse.domain.Position;
import acme.warehouse.demo.business.warehouse.dto.PositionDto;
import acme.warehouse.demo.business.warehouse.dto.PositionModificationDto;
import utils.mocks.MockProvider;
import acme.warehouse.demo.web.products.dto.ProductDto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;
import static utils.tools.PageableResponseParser.getContent;
import static utils.tools.PageableResponseParser.grabOneIdOfProduct;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MessagesControllerIntegrationTest {

    private static final String STOCK_VOLUME_CAN_NOT_ME_LESS_THAN_ZERO = "stock volume can not me less than zero";
    private static final String RESPONSE_CODE_SHOULD_BE_200 = "response code should be 200";
    private static final String EXPECTING_TO_HAVE_AT_LEAST_1_ITEM_ON_LIST = "expecting to have at least 1 item on list";
    private static final String PRODUCT_SHOULD_HAVE_THE_SAME_ID_AS_POSITION_IN_WAREHOUSE = "product should have the same id as position in warehouse";
    private static final String RESPONSE_SHOULD_CONTAIN_ERROR_MESSAGES = "response should contain error messages";
    private static final String RESPONSE_CODE_SHOULD_BE_2001 = "response code should be 200";

    private final String username = "admin";
    private final String password = "admin";

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void addNewProduct() throws InterruptedException {

        // given
        ProductDto productDto = MockProvider.getProductDto();

        // when creating product
        ResponseEntity<String> createdProductResponse = getCreateProductResponseEntity(productDto);

        // then
        assertEquals(ACCEPTED, createdProductResponse.getStatusCode());

        // and when querying for all products to get product id
        ResponseEntity<Object> allProductsResponse = getProductsResponseEntity();

        // then
        var content = verifyAtLeastOneItem(allProductsResponse);

        // and when verifying product by id
        UUID productId = grabOneIdOfProduct(content, 0);
        ResponseEntity<Product> productResopnse = getProductResponseEntity(productId);

        // then
        productResopnse.hasBody();

        // and when creating position
        ResponseEntity<String> createdPositionResopnse = getCreatePositionResponseEntity(
                MockProvider.getPositionForProductId(productId)
        );

        // then
        assertEquals(ACCEPTED, createdPositionResopnse.getStatusCode());

        // and when querying all positions to get position id
        ResponseEntity<Object> allPositionsResponse = getPositionsResponseEntity();

        // then
        verifyAtLeastOneItem(allPositionsResponse);
        verifyPositionHasTheSameIdToProduct(content, productId);

        // and when heavy random race condition to generate error messages
        var schedule = manyConcurrentModificationsResultsAfter5Seconds(productId);

        // then
        verifyResponseHasMoreThanMinusOneState(schedule);

        //and when querying for messages
        ResponseEntity<String> messagesResponse = getResponseForMessagesEndpoint();

        // then
        verifyResponseHasGeneratedMessages(messagesResponse);
    }

    private void verifyResponseHasGeneratedMessages(ResponseEntity<String> messagesResponse) {
        assertEquals(200, messagesResponse.getStatusCodeValue(), RESPONSE_CODE_SHOULD_BE_2001);
        assertTrue(Objects.requireNonNull(messagesResponse.getBody()).length() > 0, RESPONSE_SHOULD_CONTAIN_ERROR_MESSAGES);
    }

    private void verifyPositionHasTheSameIdToProduct(ArrayList content, UUID productId) {
        UUID positionId = grabOneIdOfProduct(content, 0);
        assertEquals(positionId, productId, PRODUCT_SHOULD_HAVE_THE_SAME_ID_AS_POSITION_IN_WAREHOUSE);
    }

    private ArrayList verifyAtLeastOneItem(ResponseEntity<Object> itemsResponse) {
        ArrayList content = getContent((LinkedHashMap) itemsResponse.getBody());
        int size = content.size();
        assertTrue(size > 0, EXPECTING_TO_HAVE_AT_LEAST_1_ITEM_ON_LIST);
        assertEquals(OK, itemsResponse.getStatusCode(), RESPONSE_CODE_SHOULD_BE_200);
        return content;
    }

    private void verifyResponseHasMoreThanMinusOneState(ScheduledFuture<ResponseEntity<Position>> schedule) {
        try {
            ResponseEntity<Position> positionResopnse = schedule.get();
            Long quantity = Objects.requireNonNull(positionResopnse.getBody()).getQuantity();
            assertTrue(quantity>-1, STOCK_VOLUME_CAN_NOT_ME_LESS_THAN_ZERO);
        } catch (Exception e) {
            throw new RuntimeException("Test failed!: " + e.getMessage());
        }
    }

    private ScheduledFuture<ResponseEntity<Position>> manyConcurrentModificationsResultsAfter5Seconds(UUID productId) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2);
        scheduledExecutorService.schedule( ()-> invokeConcurrentModification(productId), 0, MILLISECONDS);
        return scheduledExecutorService.schedule(() -> getPositionResponseEntity(productId), 5, SECONDS);
    }

    private void invokeConcurrentModification(final UUID productId) {
        IntStream.range(0, 100).parallel().forEach(
                i->modifyPosition(concurrentModifications(productId))
        );
    }

    private PositionModificationDto concurrentModifications(UUID priductId) {
        long random = ThreadLocalRandom.current().nextLong(3, 20);
        return PositionModificationDto.builder()
                .modification(-random)
                .productId(priductId)
                .build();
    }

    private void modifyPosition(PositionModificationDto positionModificationDto) {
        restTemplate
                .withBasicAuth(username, password)
                .postForEntity("http://localhost:" + port + "/api/positions/modify", positionModificationDto, String.class);
    }

    private ResponseEntity<String> getCreateProductResponseEntity(ProductDto productDto) {
        return restTemplate
                .withBasicAuth(username, password)
                .postForEntity("http://localhost:" + port + "/api/products/create",
                        productDto,
                        String.class);
    }

    private ResponseEntity<String> getCreatePositionResponseEntity(PositionDto positionDto) {
        return restTemplate
                .withBasicAuth(username, password)
                .postForEntity("http://localhost:" + port + "/api/positions/create", positionDto, String.class);
    }

    private ResponseEntity<Product> getProductResponseEntity(UUID id) {
        return restTemplate
                .withBasicAuth(username, password)
                .getForEntity("http://localhost:" + port + "/api/products/product/" + id.toString(),
                        Product.class);
    }

    private ResponseEntity<Object> getProductsResponseEntity() {
        return restTemplate
                .withBasicAuth(username, password)
                .getForEntity("http://localhost:" + port + "/api/products/all",
                        Object.class);
    }

    private ResponseEntity<Object> getPositionsResponseEntity() {
        return restTemplate
                .withBasicAuth(username, password)
                .getForEntity("http://localhost:" + port + "/api/positions/all",
                        Object.class);
    }

    private ResponseEntity<Position> getPositionResponseEntity(UUID productId) {
        return restTemplate
                .withBasicAuth(username, password)
                .getForEntity("http://localhost:" + port +
                                "/api/positions/position/" +
                                productId.toString(),
                        Position.class);
    }

    private ResponseEntity<String> getResponseForMessagesEndpoint() {
        return restTemplate
                .withBasicAuth(username, password)
                .getForEntity("http://localhost:" + port + "/api/messages", String.class);
    }
}