package ee.ut.cs.mc.and.testingapplication1311;


import java.util.ArrayList;

public class ShoppingBasket extends ArrayList<ShoppingItem> {

    public int getTotal(){
        int sum = 0;

        for (ShoppingItem item : this ) {
            sum += item.price * item.amount;
        }
        return sum;
    }


    /** Add item or update item count if item already exists */
    public void addItem(ShoppingItem item){
        ShoppingItem existingItem = tryToFindExistingItem(item);

        if (existingItem != null){
            existingItem.amount++;
        } else {
            add(item);
        }
    }

    /** Return existing instance of item if it already exists, otherwise return null */
    private ShoppingItem tryToFindExistingItem(ShoppingItem item) {
        for (ShoppingItem existingItem : this ) {
            if ( existingItem.name.equals(item.name) ) {
                return existingItem;
            }
        }
        return null;
    }
}
