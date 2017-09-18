package ee.ojiambout.ivan.homework_2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import static android.R.attr.name;

public class GreetingActivity extends AppCompatActivity {
    public static final int EMAIL_SENT = 2;
    private  HashMap user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

        TextView username       = (TextView) findViewById(R.id.user_name);
        final EditText title    = (EditText) findViewById(R.id.title);
        final EditText email    = (EditText) findViewById(R.id.email);
        final EditText body     = (EditText)(findViewById(R.id.body));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            user = (HashMap) bundle.get("user");
            String hello = "You are welcome, you can go ahead and send an email";
            username.setText("Hello "+ user.get("firstname") +" " +user.get("surname")+" "+hello);
        }

        //sends the email when clicked
        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject       = title.getText().toString();
                String msg         = body.getText().toString();
                String email_address = email.getText().toString();

                //attaches the username at the bottom of the message body
                String footer_msg    =  "Sent by " + user.get("firstname")+" "+user.get("surname");
                String msg_body      = msg+"\n"+"\n"+"\t"+ footer_msg;

                composeEmail(email_address, subject, msg_body);
            }
        });

    }

    private void composeEmail(String addresses, String subject, String msg) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", addresses, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
        setResult(Activity.RESULT_OK);
        startActivityForResult(emailIntent, EMAIL_SENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EMAIL_SENT){
            if (resultCode == Activity.RESULT_CANCELED){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("MSG","Your email has been sent sucessfully");
                startActivity(intent);
            }
        }
    }
}
