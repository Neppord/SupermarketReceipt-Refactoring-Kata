package dojo.supermarket.model;

import java.util.stream.Stream;

public interface Offer {

    Stream<Discount> getDiscounts(Product product, double quantity, double unitPrice);

    default Offer and(Offer other) {
        return (product, quantity, unitPrice) -> Stream.concat(
            this.getDiscounts(product, quantity, unitPrice),
            other.getDiscounts(product, quantity, unitPrice)
        );
    }
}
