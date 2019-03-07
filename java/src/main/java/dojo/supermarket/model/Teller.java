package dojo.supermarket.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class Teller {

    private final SupermarketCatalog catalog;
    private Map<Product, Offer> offers = new HashMap<>();

    Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        this.offers.put(product, new Offer(offerType, product, argument));
    }

    Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        Receipt receipt = new Receipt();
        List<ProductQuantity> productQuantities = theCart.getItems();
        for (ProductQuantity pq: productQuantities) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = this.catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            receipt.addProduct(p, quantity, unitPrice, price);
        }
        List<Discount> discounts = handleOffers(theCart);
        if (discounts.isEmpty()) {
            theCart.handleOffers(receipt, this.offers, this.catalog);
        } else {
            discounts.forEach(receipt::addDiscount);
        }
        return receipt;
    }

    private List<Discount> handleOffers(ShoppingCart theCart) {
        Map<Product, Double> productQuantities = theCart.productQuantities();
        for (Product product : productQuantities.keySet()){
            if (offers.containsKey(product)) {
                Offer offer = this.offers.get(product);
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    double percentage = offer.argument;
                    double quantity = productQuantities.get(product);
                    double unitPrice = catalog.getUnitPrice(product);
                    Optional<Discount> discount = product
                        .calculateOptionalPercentageDiscount(percentage, quantity, unitPrice);
                    return discount.stream().collect(Collectors.toList());
                }
            }
        }

        return List.of();
    }

}
