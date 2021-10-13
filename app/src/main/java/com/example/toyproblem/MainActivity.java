package com.example.toyproblem;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.sf.javaml.classification.KNearestNeighbors;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor s;

    int counter = 0;

    ArrayList<float[]> acc = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Linear acceleration is a software sensor that subtracts gravity from accelerometer values
        // Gets acceleration (minus the gravity)
        s = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void onClick(View v) {
        sensorManager.registerListener(this, s, 100000);

        /*// Imagine index = 0 is the height in cm and index = 1 is the weight in lbs
        Instance e1 = new DenseInstance(new double[]{170, 137}, "Bear");
        Instance e2 = new DenseInstance(new double[]{165, 145}, "Bear");
        Instance e3 = new DenseInstance(new double[]{120, 2000}, "Buffalo");
        Instance e4 = new DenseInstance(new double[]{127, 2200}, "Buffalo");

        Instance e5 = new DenseInstance(new double[]{135, 2500});

        Dataset dset = new DefaultDataset(); // Creates dataset
        dset.add(e1);
        dset.add(e2);
        dset.add(e3);
        dset.add(e4);

        // Train model (using k-nearest neighbor)
        KNearestNeighbors knn = new KNearestNeighbors(3); // Because we have 4 examples you should normally pick 2 but want an odd number for tie breakers when voting
        knn.buildClassifier(dset);

        Log.v("BearBuffaloClassifier", "Classifier created");

        Object result = knn.classify(e5); // classify method just returns an Object
        Log.v("BearBuffaloClassifier", "Class: " + result.toString());*/

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        acc.add(sensorEvent.values.clone());

        counter++;
        if (counter == 100) {
            sensorManager.unregisterListener(this);
            try {
                PrintWriter pr = new PrintWriter(
                        new FileOutputStream(
                                    new File(getExternalFilesDir("MyData"), "stand.txt")));
                for(float[] arr : acc) {
                    String str = arr[0] + "\t" + arr[1] + "\t" + arr[2];
                    pr.println(str);
                    Log.v("MYTAG", str);
                }
                pr.close();
                Log.v("MYTAG", "File written to " + getExternalFilesDir("MyData").toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            counter = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}