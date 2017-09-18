package ee.ojiambout.ivan.homework_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    public  static  final int USER_REGISTERED = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText first_name     = (EditText) findViewById(R.id.firstname);
        final EditText sur_name             = (EditText) findViewById(R.id.surname);
        Button submitBtn              = (Button) findViewById(R.id.submit_btn);
        TextView success_msg          = (TextView) findViewById(R.id.sucess_msg);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap user    = new HashMap();
                user.put("firstname", first_name.getText().toString());
                user.put("surname",  sur_name.getText().toString());

                Intent intent       = new Intent(MainActivity.this, GreetingActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, USER_REGISTERED);

            }
        });

        String success_message;
        success_message = getIntent().getStringExtra("MSG");
        if(success_message != null){
            success_msg.setText(success_message);

        }



    }

}
