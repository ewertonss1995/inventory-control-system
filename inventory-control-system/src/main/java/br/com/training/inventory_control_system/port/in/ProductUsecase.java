package br.com.training.inventory_control_system.port.in;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;

public interface ProductUsecase {
    void saveProduct(ProductRequest request);
}
