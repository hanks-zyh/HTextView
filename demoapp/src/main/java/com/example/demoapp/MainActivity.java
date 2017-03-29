package com.example.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * MainActivity
 * Created by hanks on 2017/3/21.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launch(View view) {
        if (view instanceof TextView) {
            String className = "com.example.demoapp." + ((TextView) view).getText().toString();
            try {
                startActivity(new Intent(this, Class.forName(className)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
