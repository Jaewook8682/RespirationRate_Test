package com.example.wearable1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MainActivity extends AppCompatActivity {
    Float[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();
        PyObject module = py.getModule("test");
        //get the predictions from the ml module
        PyObject Prediction = module.callAttr("evaluate", data);
        System.out.println(Prediction);
        DateTimeFormatter date_f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter time_f = DateTimeFormatter.ofPattern("HH:mm:ss");
        String Date = LocalDateTime.now().format(date_f).toString();
        String Time = LocalDateTime.now().format(time_f).toString();
        db.insertData(Date, "coughing", Time, 1);
    }
}