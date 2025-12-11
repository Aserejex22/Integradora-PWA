package mx.edu.utez.back.controller;

import mx.edu.utez.back.model.Product;
import mx.edu.utez.back.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(productService.create(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product productData) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            Product existing = product.get();
            if (productData.getName() != null)
                existing.setName(productData.getName());
            if (productData.getSku() != null)
                existing.setSku(productData.getSku());
            if (productData.getPrice() != null)
                existing.setPrice(productData.getPrice());
            if (productData.getStock() != null)
                existing.setStock(productData.getStock());
            if (productData.getDescription() != null)
                existing.setDescription(productData.getDescription());
            if (productData.getPhotoBase64() != null)
                existing.setPhotoBase64(productData.getPhotoBase64());
            if (productData.getQrCode() != null)
                existing.setQrCode(productData.getQrCode());
            if (productData.getAvailable() != null)
                existing.setAvailable(productData.getAvailable());
            return ResponseEntity.ok(productService.create(existing));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
