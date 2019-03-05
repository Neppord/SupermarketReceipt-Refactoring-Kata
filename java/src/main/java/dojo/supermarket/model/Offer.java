package dojo.supermarket.model;

import java.util.stream.Stream;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    double argument;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
    }

    Product getProduct() {
        return this.product;
    }

    NewOffer createNewOffer() {
        return (Product product, double quantity, double unitPrice) -> {
            if (getProduct().equals(product)) {
                if (offerType == SpecialOfferType.ThreeForTwo) {
                    if ((int) quantity > 2) {
                        int numberOfXs = (int) quantity / 3;
                        double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + (int) quantity % 3 * unitPrice);
                        return Stream.of(new Discount(product, "3 for 2", discountAmount));
                    }

                } else if (offerType == SpecialOfferType.TwoForAmount) {
                    if ((int) quantity >= 2) {
                        double total = argument * (int) quantity / 2 + (int) quantity % 2 * unitPrice;
                        double discountN = unitPrice * quantity - total;
                        return Stream.of(new Discount(product, "2 for " + argument, discountN));
                    }

                }
                if (offerType == SpecialOfferType.FiveForAmount) {
                    int numberOfXs = (int) quantity / 5;
                    if ((int) quantity >= 5) {
                        double discountTotal = unitPrice * quantity - (argument * numberOfXs + (int) quantity % 5 * unitPrice);
                        return Stream.of(new Discount(product, 5 + " for " + argument, discountTotal));
                    }
                }
                if (offerType == SpecialOfferType.TenPercentDiscount) {
                    return Stream.of(new Discount(
                        product,
                        argument + "% off",
                        quantity * unitPrice * argument / 100.0
                    ));
                }
            }
            return Stream.empty();
        };
    }
}
