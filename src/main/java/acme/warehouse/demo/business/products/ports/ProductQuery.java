package acme.warehouse.demo.business.products.ports;

import acme.warehouse.demo.business.products.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductQuery {

    Page<Product> getProducts(Pageable pageable);

    Product getProductBy(UUID uuid);
}
