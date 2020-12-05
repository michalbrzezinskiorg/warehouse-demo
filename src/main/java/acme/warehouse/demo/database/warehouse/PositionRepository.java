package acme.warehouse.demo.database.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface PositionRepository extends JpaRepository<PositionEntity, UUID> {

}
