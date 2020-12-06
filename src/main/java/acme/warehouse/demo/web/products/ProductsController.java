package acme.warehouse.demo.web.products;

import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.config.beans.CurrentUser;
import acme.warehouse.demo.web.products.dto.ProductDto;
import acme.warehouse.demo.web.products.ports.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
class ProductsController {

    private final ProductService service;
    private final CurrentUser user;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    void addNewProduct(@RequestBody ProductDto product) {
        user.getCurrentUser().ifPresent(u->service.saveNewProduct(product, u));
    }

    @GetMapping("all")
    Page<Product> getProducts(Pageable pageable) {
        return service.getProducts(pageable);
    }

    @GetMapping("product/{uuid}")
    Product getProduct(@PathVariable UUID uuid) {
        return service.getProductBy(uuid);
    }
}
