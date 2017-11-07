package ee.ut.cs.mc.and.testingapplication1311;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    private ShoppingBasket basket;
    private ShoppingItem item1;
    private ShoppingItem item2;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void initEmptyBasket() {
        basket = new ShoppingBasket();
        item1 = new ShoppingItem("milk", 2);
        item2 = new ShoppingItem("bread", 1);
    }

    @Test
    public void addingItemsIncreasesSize(){

        int originalsize    = basket.size();
        basket.addItem(item1);
        assertEquals(basket.size(), originalsize+1);
        assertEquals(basket.get(0), item1);
    }

    @Test
    public void basketTotalWithDifferentItems(){
        basket.addItem(item1);
        basket.addItem(item1);
        assertEquals(basket.getTotal(), item1.price+item2.price);
    }

 //Try uncommenting the following test, observe the error it produces!
    //  @Test
//    public void problematicUnitTest() {
//        ShoppingItem item3 = new ShoppingItem("12345", 1);
//        item3.getReformattedName();
//        assertEquals("Product 12345", item3.name);
//    }


}