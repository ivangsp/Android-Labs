package ee.ojiambout.ivan.homework3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public  static  boolean IS_PLAYING = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button  playBtn = (Button) findViewById(R.id.playbtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  mServiceIntent = new Intent(MainActivity.this, MusicService.class);
                if (IS_PLAYING){
                    stopService(mServiceIntent);
                    IS_PLAYING = false;
                    playBtn.setText("PLAY");

                }
                else{
                    startService(mServiceIntent);
                    IS_PLAYING = true;
                    playBtn.setText("PAUSE");
                }

            }
        });
    }
}
