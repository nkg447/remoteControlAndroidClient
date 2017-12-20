package com.example.nikunj.remotecontrol;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    String parentPath = "/home";

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

            //out.println("/home/nikunj");
            makeActivity();
        } catch (Exception e) {
            errorHandler(e);
        }
        Button drives=(Button)findViewById(R.id.drives);
        drives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentPath="/media";
                makeActivity();
            }
        });
    }

    public void makeActivity() {
        linear.removeAllViews();
        try {
            out.println(parentPath);
            out.flush();
            List<String> file = new LinkedList<>();
            List<String> dir = new LinkedList<>();
            String str2 = in.readLine();
            if (str2.equals("START_MSG")) {
                boolean reading = true;
                while (reading) {
                    str2 = in.readLine();
                    if (str2.equals("END_MSG")) {
                        reading = false;
                    } else {
                        if (str2.charAt(0) == 16)
                            dir.add(str2.substring(1));
                        else
                            file.add(str2.substring(1));

                    }
                }
            }
            int id = 0;
            Button btn;
            for (String s : dir) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.folder, null);
                // Add the new row before the add field button.
                linear.addView(rowView, linear.getChildCount() - 1);
                btn = (Button) (linear.findViewById(R.id.dir));
                btn.setId(id++);
                btn.setText(s);
                btn.setOnClickListener(this);
            }

            for (String s : file) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.file, null);
                // Add the new row before the add field button.
                linear.addView(rowView, linear.getChildCount());
                btn = (Button) (linear.findViewById(R.id.dir));
                btn.setId(id++);
                btn.setText(s);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        out.println(parentPath + "/" + ((Button) view).getText());
                        out.flush();
                    }
                });
            }

        } catch (Exception e) {
            Intent er = new Intent(this, ErrorLabel.class);
            er.putExtra("error", e.toString());
            startActivity(er);
        }
    }


    @Override
    public void onClick(View view) {
        parentPath = parentPath + "/" + ((Button) view).getText();
        makeActivity();
    }

    @Override
    public void onBackPressed() {
        parentPath = parentPath.substring(0, parentPath.lastIndexOf('/'));
        //errorHandler(new Exception(parentPath));
        makeActivity();
    }

    public void errorHandler(Exception e) {
        Intent er = new Intent(this, ErrorLabel.class);
        er.putExtra("error", e.toString() + " \nMessage - " + e.getMessage());
        startActivity(er);
    }

}
