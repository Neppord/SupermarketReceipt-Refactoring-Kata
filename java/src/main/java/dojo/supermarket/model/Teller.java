package dojo.supermarket.model;

import java.util.List;
import java.util.stream.Stream;

class Teller {

    private final SupermarketCatalog catalog;
    private Offer offers = (product, quantity, unitPrice) -> Stream.empty();

    Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        switch (offerType) {
            case ThreeForTwo: {
                Offer offer = (Product product1, double quantity, double unitPrice) -> {
                    if (product.equals(product1)) {
                        if ((int) quantity > 2) {
                            int numberOfXs = (int) quantity / 3;
                            double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + (int) quantity % 3 * unitPrice);
                            return Stream.of(new Discount(product1, "3 for 2", discountAmount));
                        }
                    }
                    return Stream.empty();
                };
                addSpecialOffer(offer);
            } break;
            case TwoForAmount: {
                Offer offer = (Product product1, double quantity, double unitPrice) -> {
                    if (product.equals(product1)) {
                        if ((int) quantity >= 2) {
                            double total = argument * (int) quantity / 2 + (int) quantity % 2 * unitPrice;
                            double discountN = unitPrice * quantity - total;
                            return Stream.of(new Discount(product1, "2 for " + argument, discountN));
                        }
                    }
                    return Stream.empty();
                };
                addSpecialOffer(offer);
            } break;
            case FiveForAmount: {
                Offer offer = (Product product1, double quantity, double unitPrice) -> {
                    if (product.equals(product1)) {
                        int numberOfXs = (int) quantity / 5;
                        if ((int) quantity >= 5) {
                            double discountTotal = unitPrice * quantity - (argument * numberOfXs + (int) quantity % 5 * unitPrice);
                            return Stream.of(new Discount(product1, 5 + " for " + argument, discountTotal));
                        }
                    }
                    return Stream.empty();
                };
                addSpecialOffer(offer);
            } break;
            case TenPercentDiscount: {
                Offer offer = (Product product1, double quantity, double unitPrice) -> {
                    if (product.equals(product1)) {
                        return Stream.of(new Discount(
                            product1,
                            argument + "% off",
                            quantity * unitPrice * argument / 100.0
                        ));
                    }
                    return Stream.empty();
                };
                addSpecialOffer(offer);
                break;
            }
        }
    }

    private void addSpecialOffer(Offer offer) {
        offers = offers.and(offer);
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
        theCart.handleOffers(receipt, this.catalog, offers);

        return receipt;
    }

}
