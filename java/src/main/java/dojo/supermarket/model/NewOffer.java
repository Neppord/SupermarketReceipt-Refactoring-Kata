package dojo.supermarket.model;

import java.util.Optional;

@FunctionalInterface
public interface NewOffer {
    Optional<Discount> calculateDiscount(Product product, double quantity, double unitPrice);
}
