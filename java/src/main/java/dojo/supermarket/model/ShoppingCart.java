package dojo.supermarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {

    private final List<ProductQuantity> items = new ArrayList<>();
    Map<Product, Double> productQuantities = new HashMap<>();


    List<ProductQuantity> getItems() {
        return new ArrayList<>(items);
    }

    void addItem(Product product) {
        this.addItemQuantity(product, 1.0);
    }

    Map<Product, Double> productQuantities() {
        return productQuantities;
    }


    public void addItemQuantity(Product product, double quantity) {
        items.add(new ProductQuantity(product, quantity));
        if (productQuantities.containsKey(product)) {
            productQuantities.put(product, productQuantities.get(product) + quantity);
        } else {
            productQuantities.put(product, quantity);
        }
    }

    void handleOffers(Receipt receipt, Map<Product, Offer> offers, SupermarketCatalog catalog) {
        for (Product product: productQuantities().keySet()) {
            double quantity = productQuantities.get(product);
            if (offers.containsKey(product)) {
                Offer offer = offers.get(product);
                double unitPrice = catalog.getUnitPrice(product);
                int quantityAsInt = (int) quantity;
                Discount discount = getDiscount(product, quantity, offer, unitPrice, quantityAsInt);
                if (discount != null)
                    receipt.addDiscount(discount);
            }

        }
    }

    private Discount getDiscount(Product product, double quantity, Offer offer, double unitPrice, int quantityAsInt) {
        if (offer.offerType == SpecialOfferType.ThreeForTwo) {
            if (quantityAsInt > 2) {
                int numberOfXs = quantityAsInt / 3;
                double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
                return new Discount(product, "3 for 2", discountAmount);
            }

        } else if (offer.offerType == SpecialOfferType.TwoForAmount) {
            if (quantityAsInt >= 2) {
                double total = offer.argument * quantityAsInt / 2 + quantityAsInt % 2 * unitPrice;
                double discountN = unitPrice * quantity - total;
                return new Discount(product, "2 for " + offer.argument, discountN);
            }

        }
        if (offer.offerType == SpecialOfferType.FiveForAmount) {
            int numberOfXs = quantityAsInt / 5;
            if (quantityAsInt >= 5) {
                double discountTotal = unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice);
                return new Discount(product, 5 + " for " + offer.argument, discountTotal);
            }
        }
        if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
            return new Discount(
                product,
                offer.argument + "% off",
                quantity * unitPrice * offer.argument / 100.0
            );
        }
        return null;
    }
}
