package dojo.supermarket.model;

import java.util.stream.Stream;

public interface Offer {

    Stream<Discount> getDiscounts(Product product, double quantity, double unitPrice);

    default Offer concat(Offer other) {
        return (product, quantity, unitPrice) -> Stream.concat(
            this.getDiscounts(product, quantity, unitPrice),
            other.getDiscounts(product, quantity, unitPrice)
        );
    }

    default BundleOffer toBundleOffer() {
        return (productQuantities, catalog) ->
            productQuantities
            .entrySet()
            .stream()
            .flatMap(e -> {
                Product product = e.getKey();
                double quantity = e.getValue();
                return getDiscounts(
                    product,
                    quantity,
                    catalog.getUnitPrice(product)
                );
            });
    }

    static Offer convert(Offer offer) {
        return offer;
    }

    static Stream<Discount> emptyOffer(Product product, double quantity, double unitPrice) {
        return Stream.empty();
    }
}
