package acme.warehouse.demo.web.products.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductDto {
    String name;
    String description;
}
