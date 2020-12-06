package acme.warehouse.demo.business.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public class PositionModificationDto {
    UUID productId;
    Long modification;
}
