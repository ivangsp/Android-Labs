package ee.ut.cs.mc.and.testingapplication1311;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFS = "SHOPPING_LIST";

    ListView shoppingListView;
    EditText newItemName;
    TextView totalCostTextView;

    ShoppingListViewAdapter mAdapter;
    SharedPreferences sharedPrefs;

    ShoppingBasket mShoppingBasket;

    @Override
    protected void onResume() {
        //Try to load previously stored basket..
        mShoppingBasket = UserStateHelper.loadBasket(sharedPrefs);
        if (mShoppingBasket==null){
            mShoppingBasket = new ShoppingBasket();
        }
        mAdapter = new ShoppingListViewAdapter(this, mShoppingBasket);
        shoppingListView.setAdapter(mAdapter);

        updateTotalCostView();
        super.onResume();
    }
    
    @Override
    protected void onPause() {
        UserStateHelper.saveBasket(mShoppingBasket, sharedPrefs);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPrefs = getSharedPreferences(APP_PREFS , 0);

        //Get UI element references
        shoppingListView = (ListView) findViewById(R.id.shoppingListView);
        newItemName = (EditText) findViewById(R.id.editText_name);
        totalCostTextView = (TextView) findViewById(R.id.tv_sum);
    }

    public void onNewItemButtonClicked(View v) {
        //Get values
        String name = newItemName.getText().toString();
        newItemName.setText("");

        //Add new item to the basket, update UI
        ShoppingItem newItem = new ShoppingItem(name, 1);
        mShoppingBasket.addItem(newItem);
        mAdapter.notifyDataSetChanged();

        updateTotalCostView();
    }

    private void updateTotalCostView() {
        int totalCost = mShoppingBasket.getTotal();
        totalCostTextView.setText("Total Cost: $ " + totalCost);
    }

    public void onClearButtonClicked(View v){
        mShoppingBasket.clear();
        mAdapter.notifyDataSetChanged();
        updateTotalCostView();
    }
}
