package com.example.nikunj.remotecontrol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


    BufferedReader in;
    PrintWriter out;
    Socket s = null;
    EditText cmd;
    private LinearLayout linear;
    String parentPath = "/home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        linear = (LinearLayout) findViewById(R.id.linearLayout2);


        Intent i = getIntent();
        try {
            s = new Socket(i.getStringExtra("ip"), Integer.parseInt(i.getStringExtra("port")));
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), false);

            //out.println("/home/nikunj");
            makeActivity();
        } catch (Exception e) {
            errorHandler(e);
        }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            parentPath = parentPath.substring(0, parentPath.lastIndexOf('/'));
            if(parentPath.equals("")){
                parentPath="/home";
            }
            makeActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_drive) {
            parentPath = "/media";
            makeActivity();

        } else if (id == R.id.nav_cmd) {
            linear.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.command_exec, null);
            // Add the new row before the add field button.
            linear.addView(rowView, linear.getChildCount());
            cmd=(EditText)findViewById(R.id.command);
            Button exec=(Button)findViewById(R.id.execute);

            cmd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cmd.setText("");
                }
            });

            exec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    out.println("cmd://"+cmd.getText());
                    out.flush();

                }
            });

        } else if (id == R.id.nav_path) {
            linear.removeAllViews();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.open_path, null);
            // Add the new row before the add field button.
            linear.addView(rowView, linear.getChildCount());
            cmd=(EditText)findViewById(R.id.command);
            Button exec=(Button)findViewById(R.id.execute);

            cmd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cmd.setText("");
                }
            });

            exec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parentPath=cmd.getText().toString();
                    makeActivity();
                }
            });

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            shareIt();

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareIt() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://sourceforge.net/projects/remote-control-android/files/remoteControl.apk/download";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download link");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    public void errorHandler(Exception e) {
        Intent er = new Intent(this, ErrorLabel.class);
        er.putExtra("error", e.toString() + " \nMessage - " + e.getMessage());
        startActivity(er);
    }
}
