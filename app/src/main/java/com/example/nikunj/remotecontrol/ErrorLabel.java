package com.example.nikunj.remotecontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ErrorLabel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_label);
        Intent i=getIntent();
        TextView tv=(TextView)findViewById(R.id.error);
        tv.setText(i.getStringExtra("error"));
    }
}
