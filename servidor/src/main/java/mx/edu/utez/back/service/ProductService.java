package mx.edu.utez.back.service;

import mx.edu.utez.back.model.Product;
import mx.edu.utez.back.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Error creating product", e);
        }
    }

    public List<Product> findAll() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error finding products", e);
        }
    }

    public Optional<Product> findById(Long id) {
        try {
            return productRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error finding product", e);
        }
    }

    public Optional<Product> findBySku(String sku) {
        try {
            return productRepository.findBySku(sku);
        } catch (Exception e) {
            throw new RuntimeException("Error finding product by SKU", e);
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }
}
