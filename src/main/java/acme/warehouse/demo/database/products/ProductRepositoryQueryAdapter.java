package acme.warehouse.demo.database.products;

import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.business.products.ports.ProductQuery;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import acme.warehouse.demo.config.exceptions.ResourceNotFoundException;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
class ProductRepositoryQueryAdapter implements ProductQuery {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(e -> modelMapper.map(e, Product.class));
    }

    @Override
    public Product getProductBy(UUID uuid) {
        return productRepository.findById(uuid)
                .map(e -> modelMapper.map(e, Product.class))
                .orElseThrow(()->new ResourceNotFoundException("could not found PRODUCT by id: "+uuid));
    }
}
