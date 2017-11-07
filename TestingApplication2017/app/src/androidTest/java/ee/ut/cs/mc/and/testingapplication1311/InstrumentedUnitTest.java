package ee.ut.cs.mc.and.testingapplication1311;

import android.app.Application;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class InstrumentedUnitTest {

    private Application app;
    private SharedPreferences prefs;
    private ShoppingBasket basket;

    @Before
    public void setUp(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        app = (Application) instrumentation.getTargetContext().getApplicationContext();
        prefs = app.getSharedPreferences(UserStateHelper.KEY_JSON_BASKET, 0);

        // Put something in the SharedPreferences
        basket = new ShoppingBasket();
        basket.addItem(new ShoppingItem("Milk", 3));
    }

    @Test
    public void problematicUnitTest() {
        ShoppingItem item3 = new ShoppingItem("12345", 1);
        String formattedName = item3.getReformattedName();
        Assert.assertEquals("Product 12345", formattedName);
    }

    @Test
    public void loadingUserState(){
        // Put some items in the basket and save it
        basket.addItem(new ShoppingItem("Beer", 1));
        UserStateHelper.saveBasket(basket, prefs);

        //actual testing
        ShoppingBasket loadedBasket = UserStateHelper.loadBasket(prefs);

        //verify the item we put in the saved basket is the same we have in the loaded basket
        assertEquals(basket.size(), loadedBasket.size());
        assertEquals(basket.get(0).name, loadedBasket.get(0).name);
        assertEquals(basket.get(0).amount, loadedBasket.get(0).amount);

    }


    @Test
    public void savingUserState(){
//        ShoppingBasket basket = new ShoppingBasket();
//        basket.addItem(new ShoppingItem("Milk", 3));

        UserStateHelper.saveBasket(basket, prefs);

        //We expect the saveBasket method to store the json representation of our basket
        String expectedResult = new Gson().toJson(basket);

        String actualResult = prefs.getString(UserStateHelper.KEY_JSON_BASKET, null);

        assertEquals(expectedResult, actualResult);
    }
}