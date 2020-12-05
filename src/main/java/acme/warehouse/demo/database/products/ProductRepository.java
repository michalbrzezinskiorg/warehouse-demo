package acme.warehouse.demo.database.products;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

}
