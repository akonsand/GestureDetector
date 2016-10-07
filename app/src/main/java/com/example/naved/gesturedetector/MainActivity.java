package com.example.naved.gesturedetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    static final int SHAKE_INTERVAL = 3000;
    static final int NUMBER_OF_ACTIONS = 2;
    static final int VOLUME_UP_THRESHOLD = 4;
    static final int VOLUME_DOWN_THRESHOLD = -4;
    static final int RECENT_APPS_THRESHOLD = 4;
    static final int HOME_THRESHOLD = 4;

    SensorManager sensorManager;
    Sensor shake;
    TextView screen;

    long prevActionTime;
    int volumeUpActionCount, volumeDownActionCount, recentAppsActionCount, homeActionCount = 0;
    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        shake = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);

        sensorManager.registerListener(this, shake, SensorManager.SENSOR_DELAY_NORMAL);

        screen = (TextView) findViewById(R.id.yolo);
        prevActionTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, shake);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float currentx = (int) Math.round(event.values[0]);
        float currenty = (int) Math.round(event.values[1]);
        float currentz = (int) Math.round(event.values[2]);

        long curTime = System.currentTimeMillis();

        long timeElapsed = curTime - this.prevActionTime;

        if(currentx >= VOLUME_UP_THRESHOLD) {
            Log.d(TAG,"UP");
            volumeUpActionCount++;
            this.prevActionTime = curTime;
            if(timeElapsed < SHAKE_INTERVAL) {
               if(volumeUpActionCount >= NUMBER_OF_ACTIONS) {
                    Log.d(TAG, "Volume up");
                    screen.setText("Volume up");
                    volumeUpActionCount = 0;
                }
            } else {
                volumeUpActionCount = 0;
            }
        } else if(currentx <= VOLUME_DOWN_THRESHOLD) {
            Log.d(TAG,"DOWN");
            volumeDownActionCount++;
            this.prevActionTime = curTime;
            if(timeElapsed < SHAKE_INTERVAL) {
                if(volumeDownActionCount >= NUMBER_OF_ACTIONS) {
                    Log.d(TAG, "Volume down");
                    screen.setText("Volume down");
                    volumeDownActionCount = 0;
                }
            } else  {
                volumeDownActionCount =0;
            }
        } else if(currentz > RECENT_APPS_THRESHOLD) {
            recentAppsActionCount++;
            this.prevActionTime = curTime;
            if(timeElapsed < SHAKE_INTERVAL) {
                if(recentAppsActionCount >= NUMBER_OF_ACTIONS) {
                    Log.d(TAG, "Recent apps");
                    screen.setText("Recent apps");
                    recentAppsActionCount = 0;
                }
            } else {
                recentAppsActionCount = 0;
            }
        } else if(currenty > HOME_THRESHOLD) {
            homeActionCount++;
            this.prevActionTime = curTime;
            if(timeElapsed < SHAKE_INTERVAL) {
                if(homeActionCount >= NUMBER_OF_ACTIONS) {
                    Log.d(TAG, "Home");
                    screen.setText("Home");
                    homeActionCount = 0;
                }
            } else {
                homeActionCount = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
