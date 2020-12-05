package acme.warehouse.demo.database.products;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
class ProductEntity {
    @Id
    private UUID id;
    private String name;
    private String description;

}
