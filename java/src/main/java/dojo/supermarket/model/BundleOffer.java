package dojo.supermarket.model;

import java.util.Map;
import java.util.stream.Stream;

public interface BundleOffer {

    Stream<Discount> getDiscounts(Map<Product, Double> productQuantities, SupermarketCatalog catalog);
}
