package dojo.supermarket.model;

import java.util.Objects;
import java.util.stream.Stream;

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

    Offer percentDiscount(double percentage) {
        return (Product product1, double quantity, double unitPrice) -> {
            if (equals(product1)) {
                return Stream.of(new Discount(
                    product1,
                    percentage + "% off",
                    quantity * unitPrice * percentage / 100.0
                ));
            }
            return Stream.empty();
        };
    }

    Offer twoForAmount(double amount) {
        return (Product product1, double quantity, double unitPrice) -> {
            if (equals(product1)) {
                if ((int) quantity >= 2) {
                    double total = amount * (int) quantity / 2 + (int) quantity % 2 * unitPrice;
                    double discountN = unitPrice * quantity - total;
                    return Stream.of(new Discount(product1, "2 for " + amount, discountN));
                }
            }
            return Stream.empty();
        };
    }

    Offer threeForTwo() {
        return (Product product1, double quantity, double unitPrice) -> {
            if (equals(product1)) {
                if ((int) quantity > 2) {
                    int numberOfXs = (int) quantity / 3;
                    double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + (int) quantity % 3 * unitPrice);
                    return Stream.of(new Discount(product1, "3 for 2", discountAmount));
                }
            }
            return Stream.empty();
        };
    }

    Offer fiveForAmount(double amount) {
        return (Product product1, double quantity, double unitPrice) -> {
            if (equals(product1)) {
                int numberOfXs = (int) quantity / 5;
                if ((int) quantity >= 5) {
                    double discountTotal = unitPrice * quantity - (amount * numberOfXs + (int) quantity % 5 * unitPrice);
                    return Stream.of(new Discount(product1, 5 + " for " + amount, discountTotal));
                }
            }
            return Stream.empty();
        };
    }
}
