package ee.ojiambout.ivan.homeassignment2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity  implements SensorEventListener{
    AnimatorSet animate;
    private SensorManager mSensorManager;
    private Sensor sensor;
    private int movement = 70;
    private ImageView ball;
    private ImageView blck;
    private RelativeLayout mlayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setContentView(R.layout.activity_main);

        mlayout = (RelativeLayout) findViewById(R.id.mlayout);
        ball = (ImageView) findViewById(R.id.ball);
        blck = (ImageView) findViewById(R.id.blck);

    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            animate = new AnimatorSet();
            ObjectAnimator xAnim;

            if (ball.getX() + ball.getWidth() < Math.round(mlayout.getWidth() / 2)) {
                xAnim = ObjectAnimator.ofFloat(ball, "translationX", blck.getX() + blck.getWidth() + 300.0f);
            } else {
                xAnim = ObjectAnimator.ofFloat(ball, "translationX", blck.getX() - 300.0f);
            }

            ObjectAnimator moveup = ObjectAnimator.ofFloat(ball, "translationY", -180.0f);
            moveup.setDuration(1000);
            ObjectAnimator moveupdown = ObjectAnimator.ofFloat(ball, "translationY", 0.0f);
            moveup.setDuration(1100);

            xAnim.setDuration(2500);
            animate.play(moveup).with(xAnim);
            animate.play(moveupdown).after(moveup);
            animate.start();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        //Log.v("DEBUG_MODE", Float.toString(event.values[1]));
        if (event.values[1] > 2) {
            if (ball.getX() + ball.getWidth() + movement <= mlayout.getWidth()) {
                if (!((ball.getX() + ball.getWidth()) <= blck.getX() &&
                        ball.getX() + ball.getWidth() + movement > blck.getX())) {
                    ball.setX(ball.getX() + movement);
                }
            }
        } else if (event.values[1] < -2) {
            if (ball.getX() - movement >= 0) {
                if (!(ball.getX() >= blck.getX() + blck.getWidth() &&
                        ball.getX() - movement < blck.getX() + blck.getWidth())) {
                    ball.setX(ball.getX() - movement);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


}