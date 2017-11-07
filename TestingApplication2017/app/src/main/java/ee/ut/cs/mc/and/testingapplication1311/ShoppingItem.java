package ee.ut.cs.mc.and.testingapplication1311;

import android.text.TextUtils;

import java.util.Random;

public class ShoppingItem {

    String name;
    int price;
    int amount;

    public ShoppingItem(String name, int amount) {

        this.name = name;
        this.price = 1;
        this.amount = amount;
    }

    public String getReformattedName(){
        if (TextUtils.isDigitsOnly(this.name))
            return "Product " + this.name;
        else
            return this.name;
    }

    @Override
    public String toString() {
        return String.format("%s price:%s amount%s", name, price, amount);
    }
}
