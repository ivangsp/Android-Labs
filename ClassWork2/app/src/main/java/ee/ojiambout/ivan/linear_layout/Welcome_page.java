package ee.ojiambout.ivan.linear_layout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by ivan on 9/16/2017.
 */

public class Welcome_page extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        TextView name = (TextView) findViewById(R.id.name);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            HashMap user = (HashMap) bundle.get("user");
            name.setText("Hello "+ user.get("fname") +" " +user.get("sname"));
        }

        //Back button
        Button buckBtn = (Button) findViewById(R.id.buck_btn);
        buckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Welcome_page.this, MainActivity.class));
            }
        });



    }
}
