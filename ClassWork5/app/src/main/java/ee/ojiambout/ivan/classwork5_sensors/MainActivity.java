package ee.ojiambout.ivan.classwork5_sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager sensorManager;
    TextView textView;
    private  Sensor mAcc;
    View lview;
    View rview;
    View cview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textview);
        lview    = findViewById(R.id.left1);
        cview    = findViewById(R.id.center1);
        rview    = findViewById(R.id.right1);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAcc    = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        textView.setText(mAcc.getName());
        List<Sensor> msensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(int i=0; i<msensor.size(); i++){
            textView.append("\n"+i +msensor.get(i).getName());
        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y= sensorEvent.values[1];
        float z = sensorEvent.values[2];
        textView.setText( "X : " +x+" Y : "+y+" Z : "+z);
        if (x>3){
            lview.setVisibility(View.VISIBLE);
            rview.setVisibility(View.INVISIBLE);
            cview.setVisibility(View.INVISIBLE);
        }
        else if (x<-3){
            rview.setVisibility(View.VISIBLE);
            lview.setVisibility(View.INVISIBLE);
            cview.setVisibility(View.INVISIBLE);
        }
        else
            rview.setVisibility(View.INVISIBLE);
            lview.setVisibility(View.INVISIBLE);
            cview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mAcc,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
