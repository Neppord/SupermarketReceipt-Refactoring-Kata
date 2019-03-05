package dojo.supermarket.model;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

class Teller {

    private final SupermarketCatalog catalog;
    private List<Offer> offers = new LinkedList<>();

    Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        this.offers.add(new Offer(offerType, product, argument));
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
        NewOffer newOffer = (product, quantity, unitPrice) -> Stream.empty();
        for (Offer offer: offers) {
            newOffer = newOffer.and(offer.createNewOffer());
        }
        theCart.handleOffers(receipt, this.catalog, newOffer);

        return receipt;
    }

}
