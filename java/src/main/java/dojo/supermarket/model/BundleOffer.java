package dojo.supermarket.model;

import java.util.Map;
import java.util.stream.Stream;

public interface BundleOffer {

    Stream<Discount> getDiscounts(Map<Product, Double> productQuantities, SupermarketCatalog catalog);

    default BundleOffer togetherWith(BundleOffer other) {
        return (productQuantities, catalog) -> {
            if(this.getDiscounts(productQuantities, catalog).findAny().isPresent()) {
                return this.concat(other).getDiscounts(productQuantities, catalog);
            } else {
                return Stream.empty();
            }
        };
    }

    default BundleOffer concat(BundleOffer other) {
        return (productQuantities, catalog) -> Stream.concat(
            this.getDiscounts(productQuantities, catalog),
            other.getDiscounts(productQuantities, catalog)
        );
    }
}
