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

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product product: productQuantities().keySet()) {
            if (offers.containsKey(product)) {
                Stream<Discount> discounts = getDiscounts(
                    product,
                    productQuantities.get(product),
                    offers.get(product),
                    catalog.getUnitPrice(product)
                );
                discounts.forEach(receipt::addDiscount);
            }
        }
    }

    private Stream<Discount> getDiscounts(Product product, double quantity, Offer offer, double unitPrice) {
        if (offer.offerType == SpecialOfferType.ThreeForTwo) {
            if ((int) quantity > 2) {
                int numberOfXs = (int) quantity / 3;
                double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + (int) quantity % 3 * unitPrice);
                return Stream.of(new Discount(product, "3 for 2", discountAmount));
            }

        } else if (offer.offerType == SpecialOfferType.TwoForAmount) {
            if ((int) quantity >= 2) {
                double total = offer.argument * (int) quantity / 2 + (int) quantity % 2 * unitPrice;
                double discountN = unitPrice * quantity - total;
                return Stream.of(new Discount(product, "2 for " + offer.argument, discountN));
            }

        }
        if (offer.offerType == SpecialOfferType.FiveForAmount) {
            int numberOfXs = (int) quantity / 5;
            if ((int) quantity >= 5) {
                double discountTotal = unitPrice * quantity - (offer.argument * numberOfXs + (int) quantity % 5 * unitPrice);
                return Stream.of(new Discount(product, 5 + " for " + offer.argument, discountTotal));
            }
        }
        if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
            return Stream.of(new Discount(
                product,
                offer.argument + "% off",
                quantity * unitPrice * offer.argument / 100.0
            ));
        }
        return Stream.empty();
    }
}
