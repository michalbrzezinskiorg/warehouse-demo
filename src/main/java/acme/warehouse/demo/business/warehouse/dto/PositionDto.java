package acme.warehouse.demo.business.warehouse.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PositionDto {
    UUID productId;
    Long quantity;
}
