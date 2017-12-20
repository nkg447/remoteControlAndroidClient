package com.example.nikunj.remotecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class IPSelector extends AppCompatActivity {
    EditText ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.execute);
        ip = (EditText) findViewById(R.id.ip);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }

    //code to get your own pc ip
    public void getPCIP(){

    }


    public void onClick(View view) {
        Intent i=new Intent(this,MainActivity.class);
        i.putExtra("ip",ip.getText().toString());
        startActivity(i);
    }
}
