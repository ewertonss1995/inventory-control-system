package br.com.training.inventory_control_system.port.in;

import br.com.training.inventory_control_system.adapter.in.requests.ProductRequest;
import br.com.training.inventory_control_system.adapter.out.responses.GetProductResponse;

import java.util.List;

public interface ProductUsecase {
    void saveProduct(ProductRequest request);
    GetProductResponse getProduct(Integer productId);
    List<GetProductResponse> getProducts();
    void updateProduct(Integer productId, ProductRequest request);
}
