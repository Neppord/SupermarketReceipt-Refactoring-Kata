package dojo.supermarket.model;

import java.util.*;

class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    private Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }


    void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    public Map<Product, Double> getProductQuantities() {
        return Collections.unmodifiableMap(productQuantities);
    }
}
