package ee.ojiambout.ivan.linear_layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by ivan on 9/17/2017.
 */

public class  RelativeLayoutActivity  extends AppCompatActivity{
    private TextView textView;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_layout);

        textView  = (TextView) findViewById(R.id.values);
        editText  = (EditText) findViewById(R.id.tmp);
        Button submitBtn = (Button) findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertTemperature();
            }
        });
    }


    private void convertTemperature(){

        float temp = Float.valueOf(editText.getText().toString());
        textView.setText(String.valueOf(temp)+"F");
        textView.setVisibility(View.VISIBLE);
        editText.clearFocus();
    }
}
