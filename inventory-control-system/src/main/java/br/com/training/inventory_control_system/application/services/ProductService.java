package br.com.training.inventory_control_system.application.services;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.mappers.ProductMapper;
import br.com.training.inventory_control_system.domain.repositories.ProductRepository;
import br.com.training.inventory_control_system.port.in.ProductUsecase;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements ProductUsecase {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void saveProduct(ProductRequest request) {
        repository.save(mapper.toEntity(request));
    }
}
