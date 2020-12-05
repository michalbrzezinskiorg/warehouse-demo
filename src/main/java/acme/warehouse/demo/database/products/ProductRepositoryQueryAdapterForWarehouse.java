package acme.warehouse.demo.database.products;

import acme.warehouse.demo.business.products.domain.Product;
import acme.warehouse.demo.config.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import acme.warehouse.demo.business.warehouse.ports.ProductQueryForWarehouse;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
class ProductRepositoryQueryAdapterForWarehouse implements ProductQueryForWarehouse {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product getProduct(UUID uuid) {
        return productRepository.findById(uuid)
                .map(e -> modelMapper.map(e, Product.class))
                .orElseThrow(()->new ResourceNotFoundException("could not found PRODUCT by id: "+uuid));
    }
}
