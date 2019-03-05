package dojo.supermarket.model;

import java.util.List;

class Teller {

    private final SupermarketCatalog catalog;
    private BundleOffer offers = Offer.convert(Offer::emptyOffer).toBundleOffer();

    Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    void addSpecialOffer(Offer offer) {
        addSpecialOffer(offer.toBundleOffer());
    }

    void addSpecialOffer(BundleOffer offer) {
        offers = offers.concat(offer);
    }

    Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        Receipt receipt = new Receipt();
        handleProducts(theCart, receipt);
        handleOffers(theCart, receipt);
        return receipt;
    }

    private void handleOffers(ShoppingCart theCart, Receipt receipt) {
        offers
            .getDiscounts(theCart.getProductQuantities(), this.catalog)
            .forEach(receipt::addDiscount);
    }

    private void handleProducts(ShoppingCart theCart, Receipt receipt) {
        List<ProductQuantity> productQuantities = theCart.getItems();
        for (ProductQuantity pq: productQuantities) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = this.catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            receipt.addProduct(p, quantity, unitPrice, price);
        }
    }

}
