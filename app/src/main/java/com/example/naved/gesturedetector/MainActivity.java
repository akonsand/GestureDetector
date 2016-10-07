package com.example.naved.gesturedetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor acc;
    Sensor rotation;
    TextView main;
    float rotationx;
    float rotationy;
    float rotationz;
    long currentTime;
    int count = 0;
    final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_NORMAL);

        main = (TextView) findViewById(R.id.yolo);
        currentTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, acc);
        sensorManager.unregisterListener(this, rotation);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Log.d("ACCELERATION", "event x " + event.values[0] + "event y" + event.values[1] + "event z" + event.values[2]);
        } else
        {
            //Log.d("ROTATION", "event x " + event.values[0] + " event y" + event.values[1] + " event z" + event.values[2] + " scalar " + event.values[3]);
            float currentx = event.values[0];
            float currenty = event.values[1];
            float currentz = event.values[2];

            Log.d(TAG, "currentx" + currentx);
            Log.d(TAG, "rotationx" + rotationx);

            if(Math.abs(currentx - rotationx) >= 0.5)
            {
                long currentTimeLocal = System.currentTimeMillis();
                Log.d(TAG, "currentTimeLocal" + currentTimeLocal);
                Log.d(TAG, "currentTime" + this.currentTime);

                long diff = currentTimeLocal - this.currentTime;
                Log.d(TAG, "diff" + diff);
                if(diff < 10000)
                {
                    count++;
                    Log.d(TAG, "Count " +  count);
                    if(count >= 3)
                    {
                        Log.d("Flip", "detected");
                        main.setText(main.getText() + "\t" + "Flip Detected");
                        count = 0;
                    }
                }
                else
                {
                    count = 0;
                }
                this.currentTime = currentTimeLocal;
            }

            rotationx = currentx;
            rotationy = currenty;
            rotationz = currentz;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
