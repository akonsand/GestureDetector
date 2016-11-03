package com.example.naved.gesturedetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor wave;
    Sensor shake;
    Sensor acc;
    TextView screen;
    Boolean doubleWave = false;
    Boolean waiting = false;

    long proxyPrevActionTime;

    final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        wave = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        shake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, wave, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, shake, SensorManager.SENSOR_DELAY_NORMAL);

        screen = (TextView) findViewById(R.id.yolo);
        proxyPrevActionTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, wave);
        sensorManager.unregisterListener(this, shake);
        sensorManager.unregisterListener(this, acc);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if(event.values[0] < 10) {
                long currentTime = System.currentTimeMillis();
                //Log.d(TAG, event.values[0] + "");
                if(!waiting) {
                    waiting = true;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!doubleWave) {
                                screen.setText("Single wave");
                            }
                            doubleWave = false;
                            waiting = false;
                        }
                    }, 1000);
                } else {
                    long timeSinceLastWave =  currentTime - proxyPrevActionTime;
                    Log.d(TAG, timeSinceLastWave + "");
                    if(timeSinceLastWave < 2000) {
                        doubleWave = true;
                        screen.setText("Double wave");
                    }
                }
                proxyPrevActionTime = currentTime;
            }
        }
        else if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
            if(event.values[0] > 25) {
                if(event.values[1] > 10) {
                    screen.setText("Double twist");
                } else {
                    screen.setText("Chop chop");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}