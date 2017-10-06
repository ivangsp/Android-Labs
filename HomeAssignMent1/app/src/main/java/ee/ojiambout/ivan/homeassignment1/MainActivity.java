package ee.ojiambout.ivan.homeassignment1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS =1;
    CustomAdaptor adaptor;
    String success_message ="Nothing selected yet!";;
    ArrayList<Contact> contacts;
    ListView listView;
    TextView msgTextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4B8F3B")));

        msgTextview = (TextView) findViewById(R.id.status);
        listView = (ListView) findViewById(R.id.list_contacts);

        if(savedInstanceState != null && savedInstanceState.getParcelableArrayList("contacts") != null){
            contacts = savedInstanceState.getParcelableArrayList("contacts");
            success_message =savedInstanceState.getString("msg");
            msgTextview.setText(success_message);
            adaptor = new CustomAdaptor(this, contacts);
            listView.setAdapter(adaptor);

        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }else{
                contacts = getContacts();
                adaptor = new CustomAdaptor(this, contacts);
                listView.setAdapter(adaptor);
                msgTextview.setText(success_message);
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Contact contactDetails = (Contact) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, ContactDetails.class);

                intent.putExtra("email", contactDetails.getEmail());
                intent.putExtra("name", contactDetails.getName());
                intent.putExtra("number", contactDetails.getNumber());
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            String name = data.getStringExtra("name");
            success_message = "sent email to "+name;
            msgTextview.setText(success_message);
        }
        if(data == null){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                contacts = getContacts();
                adaptor = new CustomAdaptor(this, contacts);
                listView.setAdapter(adaptor);
            } else {
                Toast.makeText(this, "Accept the permission to continue", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //method to Reading contact names and phone numbers
    private ArrayList<Contact> getContacts(){

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection  = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID

        };
        Cursor people = getContentResolver().query(uri,projection, null, null, null);
        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        int contact_id = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);

        people.moveToFirst();

        try {
            do {
                String name = people.getString(indexName);
                String number = people.getString(indexNumber);
                String id = people.getString(contact_id);
                String email = getEmail(id);
                //Log.v("NUmber", number);
                contacts.add( new Contact(id, name, email, number));

            } while (people.moveToNext());


        } catch (CursorIndexOutOfBoundsException e) {

        }
        return  contacts;
    }
//getting email address by specified contact_id
    private String getEmail(String id){
        Cursor cursor = null;
        String email = "";
        try{
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=?", new String[] { id },
                    null);



            // let's just get the first email
            if (cursor.moveToFirst()) {
                int nameId = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);

                int emailIdx = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                email = cursor.getString(emailIdx);
                return email;
            }


        } catch (CursorIndexOutOfBoundsException e) {

        }
        return  email;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adaptor.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        //Store the instance of the state
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("contacts", contacts);
        outState.putString("msg", success_message);

    }

}

