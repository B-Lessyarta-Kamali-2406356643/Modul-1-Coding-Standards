package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Repository
public class ProductRepository {
    public List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        if (product.getProductId() == null || product.getProductId().isBlank()) {
            product.setProductId(generateId());
        }

        if (product.getProductQuantity() != null && product.getProductQuantity() < 0) {
            product.setProductQuantity(0);
        }

        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product findById(String productId) {
        for (Product prod : productData) {
            if (prod.getProductId() != null && prod.getProductId().equals(productId)) {
                return prod;
            }
        }
        return null;
    }

    public Product edit(String productId, Product edited) {
        Product exists = findById(productId);
        if (exists == null) {
            return null;
        }

        exists.setProductName(edited.getProductName());

        Integer quantity = edited.getProductQuantity();
        if (quantity == null || quantity < 0) {
            quantity = 0;
        }
        exists.setProductQuantity(edited.getProductQuantity());
        return exists;
    }

    public String generateId() {
        int max=0;
        for (Product prod : productData) {
            if (prod.getProductId() == null) {
                continue;
            }
            try {
                int val = Integer.parseInt(prod.getProductId());
                if (val > max) {
                    max = val;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        return String.valueOf(max+1);
    }

    public boolean deleteProduct(String productId) {
        Iterator<Product> it = productData.iterator();
        while (it.hasNext()) {
            Product p = it.next();
            if (p.getProductId() != null && p.getProductId().equals(productId)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
}
