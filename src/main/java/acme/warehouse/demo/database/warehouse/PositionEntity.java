package acme.warehouse.demo.database.warehouse;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
class PositionEntity {
    @Id
    private UUID productId;
    private Long quantity;
    private String productName;
}
