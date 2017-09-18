package ee.ojiambout.ivan.homework1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        //the button goes back to the main activity when clicked
        Button buckBtn = (Button) findViewById(R.id.buck_btn);
        buckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity2.this, MainActivity.class));

                //oncreate is called
                //onStop is created
                //onResume is called
            }
        });
    }
}
