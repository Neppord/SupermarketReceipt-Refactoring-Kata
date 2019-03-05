package dojo.supermarket.model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class Teller {

    private final SupermarketCatalog catalog;
    private NewOffer newOffers = (product, quantity, unitPrice) -> Stream.empty();

    Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        Offer offer = new Offer(offerType, product, argument);
        addSpecialOffer(offer.createNewOffer());
    }

    private void addSpecialOffer(NewOffer offer) {
        newOffers = newOffers.and(offer);
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
        theCart.handleOffers(receipt, this.catalog, newOffers);

        return receipt;
    }

}
