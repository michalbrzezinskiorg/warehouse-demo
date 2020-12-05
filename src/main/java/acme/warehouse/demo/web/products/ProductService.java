package acme.warehouse.demo.web.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.web.products.dto.ProductDto;

import java.util.UUID;

public interface ProductService {
    void saveNewProduct(ProductDto productDto, String authentication);

    Page<Product> getProducts(Pageable pageable);

    Product getProductBy(UUID uuid);
}
