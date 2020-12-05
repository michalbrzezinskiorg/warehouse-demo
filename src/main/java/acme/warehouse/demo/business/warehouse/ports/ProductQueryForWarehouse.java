package acme.warehouse.demo.business.warehouse.ports;

import acme.warehouse.demo.business.products.domain.Product;

import java.util.UUID;

public interface ProductQueryForWarehouse {
    Product getProduct(UUID uuid);
}
