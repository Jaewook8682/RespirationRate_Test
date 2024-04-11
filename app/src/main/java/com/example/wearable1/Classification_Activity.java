package com.example.wearable1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class Classification_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject module = py.getModule("classification");
        //get the predictions from the ml module
        PyObject Prediction = module.callAttr("evaluate");
        System.out.println(Prediction);
    }
}