package ee.ojiambout.ivan.classwork3;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);


        //storing file on local storage
        String FILENAME  = "hello_file";
        String string    = "Hello World";

        try {
            FileOutputStream fileOutputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fileOutputStream.write(string.getBytes());
            fileOutputStream.close();


            FileInputStream file = openFileInput(FILENAME);
            InputStreamReader inputStream     = new InputStreamReader(file);
            BufferedReader bufferedReader = new BufferedReader(inputStream);
            String readString              = bufferedReader.readLine( );

            if(readString.equalsIgnoreCase(string)){
                Log.v("MY_DEBUG", string);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Reading contact names and phone numbers
        StringBuilder contacts = getContacts();
        Log.v("CONTACTS", contacts.toString());
    }

    //Reading contact names and phone numbers
    private Cursor  getContactsCurso(){
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection  = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        Cursor people = getContentResolver().query(uri,projection, null, null, null);
        return people;
    }
    private  StringBuilder getContacts(){
        Cursor people = getContactsCurso();
        StringBuilder contact = new StringBuilder();
        contact.append("Saved Contacts");

        int indexName = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int indexNumber = people.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        people.moveToFirst();

        try {
            do {
                String name = people.getString(indexName);
                String number = people.getString(indexNumber);

                contact.append("\n");
                contact.append(name);
                contact.append(number);;


            } while (people.moveToNext());


        } catch (CursorIndexOutOfBoundsException e) {

        }
        return  contact;

    }
}
