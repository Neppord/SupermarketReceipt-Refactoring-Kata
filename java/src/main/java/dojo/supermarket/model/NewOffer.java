package dojo.supermarket.model;

import java.util.stream.Stream;

public interface NewOffer {

    Stream<Discount> getDiscounts(Product product, double quantity, double unitPrice);
}
