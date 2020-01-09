package com.lding.pad.myseial.library.loadinglibrary.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.lding.pad.myseial.R;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_USEFUL_STRING = "extra_useful_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
