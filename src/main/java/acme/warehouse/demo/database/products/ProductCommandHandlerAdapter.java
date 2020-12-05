package acme.warehouse.demo.database.products;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import acme.warehouse.demo.eventstream.products.ProductCommandHandler;
import acme.warehouse.demo.business.products.domain.Product;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
class ProductCommandHandlerAdapter implements ProductCommandHandler {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public void save(Product product) {
        ProductEntity save = productRepository.save(modelMapper.map(product, ProductEntity.class));
        log.info("saved [{}]", save);
    }
}

