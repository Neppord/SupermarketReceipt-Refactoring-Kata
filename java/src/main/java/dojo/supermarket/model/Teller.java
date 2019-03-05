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
        switch (offerType) {
            case ThreeForTwo: {
                NewOffer newOffer = new Offer(SpecialOfferType.ThreeForTwo, product, argument).createNewOffer();
                addSpecialOffer(newOffer);
            } break;
            case TwoForAmount: {
                NewOffer newOffer = new Offer(SpecialOfferType.TwoForAmount, product, argument).createNewOffer();
                addSpecialOffer(newOffer);
            } break;
            case FiveForAmount: {
                NewOffer newOffer = new Offer(SpecialOfferType.FiveForAmount, product, argument).createNewOffer();
                addSpecialOffer(newOffer);
            } break;
            case TenPercentDiscount: {
                NewOffer newOffer = new Offer(SpecialOfferType.TenPercentDiscount, product, argument).createNewOffer();
                addSpecialOffer(newOffer);
                break;
            }
        }
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
