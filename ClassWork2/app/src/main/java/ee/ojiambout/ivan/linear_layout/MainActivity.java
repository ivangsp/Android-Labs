package ee.ojiambout.ivan.linear_layout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import static ee.ojiambout.ivan.linear_layout.R.id.submitBtn;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting_activity);

        Button LinearLayoutBtn   = (Button) findViewById(R.id.button1);
        Button RelativeLayoutBtn = (Button) findViewById(R.id.button3);

        final EditText fname = (EditText) findViewById(R.id.firstname);
        final EditText sname = (EditText) findViewById(R.id.surname);
        final EditText email = (EditText) findViewById(R.id.email);

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap user = new HashMap();
                user.put("fname", fname.getText().toString());
                user.put("sname", sname.getText().toString());
                user.put("email", email.getText().toString());

                Intent intent = new Intent(MainActivity.this, Welcome_page.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        LinearLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LinearLayoutActivity.class));
            }
        });
        RelativeLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RelativeLayoutActivity.class));
            }
        });


    }

}
