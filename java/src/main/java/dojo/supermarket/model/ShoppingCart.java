package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    private Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    private Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, SupermarketCatalog catalog, NewOffer newOffer) {
        for (Product product: productQuantities().keySet()) {
            Stream<Discount> discounts = newOffer.getDiscounts(
                    product,
                    productQuantities.get(product),
                    catalog.getUnitPrice(product)
                );
            discounts.forEach(receipt::addDiscount);
        }
    }

}
