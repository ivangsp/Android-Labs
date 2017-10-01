package ee.ojiambout.ivan.homeassignment1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ivan on 10/1/2017.
 */


public class ContactsDetailsActivity extends AppCompatActivity {
    private String name,email,phoneNumber;
    static final int SEND_EMAIL_REQUEST = 1;  // The request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        phoneNumber=getIntent().getStringExtra("phoneNumber");

        TextView tvName=(TextView)findViewById(R.id.lblStatus);
        TextView tvPhone=(TextView)findViewById(R.id.lblPhoneNo);
        TextView tvEmail=(TextView)findViewById(R.id.lblEmail);
        Button btn = (Button)findViewById(R.id.btnSendEmail);

        tvName.setText(name);
        tvPhone.setText(phoneNumber);
        tvEmail.setText(email);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    // send an Email
    public void sendEmail() {
        TextView rec_email = (TextView)findViewById(R.id.lblEmail);
        String recipient_email = rec_email.getText().toString();

        Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO);
        sendEmailIntent.setData(Uri.parse("mailto:"));
        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient_email});

        if (sendEmailIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(sendEmailIntent, SEND_EMAIL_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SEND_EMAIL_REQUEST && resultCode == RESULT_CANCELED) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("Result", "Sent email to "+name);
            startActivity(intent);
        }
    }
}
