package com.example.nikunj.remotecontrol;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class IPSelector extends AppCompatActivity {
    EditText ip,port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_select);
        ip = (EditText) findViewById(R.id.ip);
        port = (EditText) findViewById(R.id.port);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);



    }

    //code to get your own pc ip
    public void getPCIP() {

    }


    public void onClick(View view) {

        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("ip", ip.getText().toString());
        i.putExtra("port",port.getText().toString());
        startActivity(i);
    }
}
