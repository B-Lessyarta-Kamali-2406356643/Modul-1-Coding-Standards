package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepository {

    private final List<Order> data = new ArrayList<>();

    public Order save(Order order) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId().equals(order.getId())) {
                data.set(i, order);
                return order;
            }
        }
        data.add(order);
        return order;
    }

    public Order findById(String id) {
        for (Order order : data) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public List<Order> findAllByAuthor(String author) {
        List<Order> result = new ArrayList<>();
        for (Order order : data) {
            if (order.getAuthor().equals(author)) {
                result.add(order);
            }
        }
        return result;
    }
}