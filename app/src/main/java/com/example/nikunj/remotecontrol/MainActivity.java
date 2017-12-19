package com.example.nikunj.remotecontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    BufferedReader in;
    PrintWriter out;
    Socket s = null;
    private LinearLayout linear;
    String parentPath="/home/nikunj";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        linear = (LinearLayout) findViewById(R.id.linearLayout2);

        Intent i = getIntent();
        try {
            s = new Socket(i.getStringExtra("ip"), 3333);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), false);

            out.println("/home/nikunj");
            out.flush();
            List<String> list = new LinkedList<>();
            String str2 = in.readLine();
            if (str2.equals("START_MSG")) {
                boolean reading = true;
                while (reading) {
                    str2 = in.readLine();
                    if (str2.equals("END_MSG")) {
                        reading = false;
                    } else {
                        list.add(str2);
                    }
                }
            }
            int id = 0;
            for (String s : list) {

                //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button btn = new Button(this);
                btn.setId(id++);
                btn.setText(s);
                btn.setOnClickListener(this);
                linear.addView(btn);
            }
        } catch (Exception e) {
            errorHandler(e);
        }
    }

    @Override
    public void onClick(View view) {
        parentPath=parentPath+"/"+((Button)view).getText();
        linear.removeAllViews();
        try{
            out.println(parentPath);
            out.flush();

            List<String> list=new LinkedList<>();
            String str2 = in.readLine();
            if (str2.equals("START_MSG")) {
                boolean reading = true;
                while (reading) {
                    str2 = in.readLine();
                    if (str2.equals("END_MSG")) {
                        reading = false;
                    } else {
                        list.add(str2);
                    }
                }
            }
            int id=0;
            for (String s : list) {

                //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                Button btn = new Button(this);
                btn.setId(id++);
                btn.setText(s);
                btn.setOnClickListener(this);
                linear.addView(btn);
            }

        }
        catch (Exception e){
            Intent er=new Intent(this,ErrorLabel.class);
            er.putExtra("error",e.toString());
            startActivity(er);
        }
    }

    public void errorHandler(Exception e){
        Intent er = new Intent(this, ErrorLabel.class);
        er.putExtra("error", e.toString());
        startActivity(er);
    }

}
