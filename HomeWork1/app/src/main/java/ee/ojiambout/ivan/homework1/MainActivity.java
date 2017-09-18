package ee.ojiambout.ivan.homework1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Oncreate(), onstart() and onResume() are called
        //when the  app is launched
        Log.v("HOMEWORK1","oncreate method is caled");

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity2.class));
                //onPause is called when we navigate to Activity
                //then onStop method is also called
            }
        });
    }
    //onStart method is called immediately after oncreate method
    @Override
    protected void onStart() {
        super.onStart();
        Log.v("HOMEWORK1","onstart method is called");
    }

    //onResume method is called immediately after onStart method
    @Override
    protected void onResume() {
        super.onResume();
        Log.v("HOMEWORK1", "OnResume is called");
    }

    //when you change the orientation of the app
    //onPause, onStop, onDestroy, onCreate, onStart and onResume respectively

    //when you click the back button
    //onPause, onStop and onDestroy is called

    //when you press the home button or overview button
    //the onPause and onStop method are created respectively

    //when you preview the app again,
    //onStart and onResume method are called

    //onPause is called when we navigate to another activity
    @Override
    protected void onPause() {
        super.onPause();
        Log.v("HOMEWORK1","OnPause was called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("HOMEWORK1","onStop was called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("HOMEWORK1","OnDestroy was called");
    }


}
