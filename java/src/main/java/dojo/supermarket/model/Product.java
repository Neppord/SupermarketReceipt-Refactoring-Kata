package dojo.supermarket.model;

import java.util.Objects;
import java.util.Optional;

public class Product {
    private final String name;
    private final ProductUnit unit;

    public Product(String name, ProductUnit unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }


    public ProductUnit getUnit() {
        return unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) &&
                unit == product.unit;
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, unit);
    }

    NewOffer createPercentageOffer(double percentage) {
        return (product, q, u) -> {
                if (this.equals(product)) {
                    return Optional.of(new Discount(
                        this,
                        percentage + "% off",
                        q * u * percentage / 100.0
                    ));
                } else {
                    return Optional.empty();
                }
            };
    }
}
