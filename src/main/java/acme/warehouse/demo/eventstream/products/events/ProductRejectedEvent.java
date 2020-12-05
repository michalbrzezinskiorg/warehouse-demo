package acme.warehouse.demo.eventstream.products.events;

import lombok.Builder;
import lombok.Value;
import acme.warehouse.demo.business.products.domain.Product;

@Value
@Builder
public class ProductRejectedEvent {
    Product position;
    String message;
    String messageTo;
}
