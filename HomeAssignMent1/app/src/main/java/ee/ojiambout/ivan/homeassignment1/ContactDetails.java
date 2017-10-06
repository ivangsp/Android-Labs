package ee.ojiambout.ivan.homeassignment1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ivan on 10/3/2017.
 */
;
public class ContactDetails  extends  AppCompatActivity{
    public static final int EMAIL_SENT = 1;
    private  String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);



        TextView  nameTextview = (TextView) findViewById(R.id.name);
        TextView emailTextview = (TextView) findViewById(R.id.email);
        TextView phoneTextview  = (TextView) findViewById(R.id.phone);

        Button submtBtn = (Button) findViewById(R.id.btnSendEmail);
        final Intent intent = getIntent();
        name = intent.getStringExtra("name");
        nameTextview.setText(name);
        emailTextview.setText(intent.getStringExtra("email"));
        phoneTextview.setText(intent.getStringExtra("number"));

        submtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = intent.getStringExtra("email");
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", email, null));
                setResult(Activity.RESULT_OK);
                startActivityForResult(emailIntent, EMAIL_SENT);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EMAIL_SENT){
            if (resultCode == Activity.RESULT_CANCELED){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("name", name);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}
