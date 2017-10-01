package ee.ojiambout.ivan.homeassignment1;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String SELECTED_CONTACT_ID 	= "contact_id";
    public static final String KEY_CONTACT_NAME 	= "contact_name";
    public static final String KEY_PHONE_NUMBER 	= "phone_number";
    public static final String KEY_EMAIL 	        = "email";

    int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

        FragmentManager fragmentManager 	        = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction     = fragmentManager.beginTransaction();
        ContactsListFragment 	fragment 			= new ContactsListFragment();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();


        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("Result")) {
                String result = intent.getStringExtra("Result");
                TextView status = (TextView)findViewById(R.id.lblStatus);
                status.setText(result);
            }
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null ) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setTitle("Contacts");
        }
    }

}
