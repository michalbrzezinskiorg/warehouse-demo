package acme.warehouse.demo.eventstream.products;

import acme.warehouse.demo.business.products.domain.Product;

public interface ProductCommandHandler {
    void save(Product product);
}
