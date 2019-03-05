package dojo.supermarket.model;

import dojo.supermarket.ReceiptPrinter;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SupermarketTest {
    private SupermarketCatalog catalog;
    private Teller teller;
    private ShoppingCart theCart;
    private Product toothbrush;
    private Product rice;
    private Product apples;
    private Product cherryTomatoes;
    private Product toothpaste;

    @BeforeEach
    public void setUp() {
        catalog = new FakeCatalog();
        teller = new Teller(catalog);
        theCart = new ShoppingCart();

        toothbrush = new Product("toothbrush", ProductUnit.Each);
        catalog.addProduct(toothbrush, 0.99);
        toothpaste = new Product("toothpaste", ProductUnit.Each);
        catalog.addProduct(toothpaste, 1.79);
        rice = new Product("rice", ProductUnit.Each);
        catalog.addProduct(rice, 2.99);
        apples = new Product("apples", ProductUnit.Kilo);
        catalog.addProduct(apples, 1.99);
        cherryTomatoes = new Product("cherry tomato box", ProductUnit.Each);
        catalog.addProduct(cherryTomatoes, 0.69);

    }

    @Test
    public void an_empty_shopping_cart_should_cost_nothing() {
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void one_normal_item() {
        theCart.addItem(toothbrush);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void two_normal_items() {
        theCart.addItem(toothbrush);
        theCart.addItem(rice);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void buy_two_get_one_free() {
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        teller.addSpecialOffer(toothbrush.threeForTwo());
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void buy_five_get_one_free() {
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        theCart.addItem(toothbrush);
        teller.addSpecialOffer(toothbrush.threeForTwo());
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void loose_weight_product() {
        theCart.addItemQuantity(apples, .5);
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void percent_discount() {
        theCart.addItem(rice);
        teller.addSpecialOffer(rice.percentDiscount(10.0));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void xForY_discount() {
        theCart.addItem(cherryTomatoes);
        theCart.addItem(cherryTomatoes);
        teller.addSpecialOffer(cherryTomatoes.twoForAmount(.99));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount() {
        theCart.addItemQuantity(apples, 5);
        teller.addSpecialOffer(apples.fiveForAmount(6.99));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withSix() {
        theCart.addItemQuantity(apples, 6);
        teller.addSpecialOffer(apples.fiveForAmount(6.99));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withSixteen() {
        theCart.addItemQuantity(apples, 16);
        teller.addSpecialOffer(apples.fiveForAmount(6.99));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    public void FiveForY_discount_withFour() {
        theCart.addItemQuantity(apples, 4);
        teller.addSpecialOffer(apples.fiveForAmount(6.99));
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }

    @Test
    void two_products_with_ten_percent_of_in_bundle() {
        theCart.addItem(toothbrush);
        theCart.addItem(toothpaste);
        teller.addSpecialOffer(
            toothbrush
                .percentDiscount(10)
                .togetherWith(
                    toothpaste.percentDiscount(10).toBundleOffer()
                )
        );
        Receipt receipt = teller.checksOutArticlesFrom(theCart);
        Approvals.verify(new ReceiptPrinter(40).printReceipt(receipt));
    }
}
