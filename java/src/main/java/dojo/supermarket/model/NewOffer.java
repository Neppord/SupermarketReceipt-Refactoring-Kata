package dojo.supermarket.model;

import java.util.stream.Stream;

public interface NewOffer {

    Stream<Discount> getDiscounts(Product product, double quantity, double unitPrice);

    default NewOffer and(NewOffer other) {
        return (product, quantity, unitPrice) -> Stream.concat(
            this.getDiscounts(product, quantity, unitPrice),
            other.getDiscounts(product, quantity, unitPrice)
        );
    }
}
