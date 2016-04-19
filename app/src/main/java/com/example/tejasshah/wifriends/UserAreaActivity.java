package com.example.tejasshah.wifriends;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserAreaActivity extends AppCompatActivity {
    Button bAddNetwork;
    Button bModify,bWiFi;
    android.support.v7.app.ActionBar actionBar;
    String name,username,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final EditText etUsername = ( EditText) findViewById(R.id.etUsername);
        final EditText etEmail = ( EditText) findViewById(R.id.etEmail);
        final TextView welcomeMessage = (TextView) findViewById(R.id.tvWelcomeMsg);
        etUsername.setEnabled(false);
        etEmail.setEnabled(false);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        username = intent.getStringExtra("username");
        email = intent.getStringExtra("email");

        actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.user_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        String message = "Welcome " + name  ;
        welcomeMessage.setText(message);
        etUsername.setText(username);
        etEmail.setText(email);
        bWiFi = (Button)findViewById(R.id.bWiFi);
        bAddNetwork = (Button)findViewById(R.id.bAddNetwork);
        bAddNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNetworkIntent = new Intent(UserAreaActivity.this, AddNetworkActivity.class);
                addNetworkIntent.putExtra("username", username);
                addNetworkIntent.putExtra("name", name);
                addNetworkIntent.putExtra("email", email);
                UserAreaActivity.this.startActivity(addNetworkIntent);

            }
        });
       bModify = (Button)findViewById(R.id.bModify);
        bModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("On Click inside");
                // final String wname = etNetworkName.getText().toString();
                // final String wpass = etNetworkPassword.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            System.out.println(success);
                            if(success){
                                String name = jsonResponse.getString("name");
                                String email = jsonResponse.getString("email");
                                //String wid = jsonResponse.getString("wid");
                                String wname = jsonResponse.getString("wname");
                                String wpass = jsonResponse.getString("wpass");
                                String username = jsonResponse.getString("username");
                                //System.out.println(name + email + wname + wpass + username);
                                Intent intent = new Intent(UserAreaActivity.this, ModifyNetworkActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("username",username);
                                intent.putExtra("email",email);
                                //intent.putExtra("wid",wid);
                                intent.putExtra("wname",wname);
                                intent.putExtra("wpass",wpass);
                                UserAreaActivity.this.startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                builder.setMessage("No Network Registered")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ModifyNetwork modNetwork = new ModifyNetwork(username,email, name, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
                queue.add(modNetwork);

            }
        });

        final Button bViewFriend = (Button)findViewById(R.id.bViewFriend);
        bViewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserAreaActivity.this, AddFriends.class);
                i.putExtra("name", name);
                i.putExtra("username", username);
                i.putExtra("email", email);
                startActivity(i);

            }
        });



        bWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            System.out.println(success);
                            if(success){
                                String name = jsonResponse.getString("name");
                                String email = jsonResponse.getString("email");
                                //String wid = jsonResponse.getString("wid");
                                String wname = jsonResponse.getString("wname");
                                String wpass = jsonResponse.getString("wpass");
                                String name1 = jsonResponse.getString("name1");
                                String username = jsonResponse.getString("username");
                               // System.out.println(name + email + wname + wpass + username);
                                Intent intent = new Intent(UserAreaActivity.this, ViewWiFiActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("username",username);
                                intent.putExtra("email",email);
                                intent.putExtra("name1",name1);
                                intent.putExtra("wname",wname);
                                intent.putExtra("wpass",wpass);
                                startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                builder.setMessage("No WIFI Found for you !!")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ViewWiFi vw = new ViewWiFi(username,email, name, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
                queue.add(vw);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.connectWifi:
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            System.out.println(success);
                            if(success){
                                String name = jsonResponse.getString("name");
                                String email = jsonResponse.getString("email");
                                //String wid = jsonResponse.getString("wid");
                                String wname = jsonResponse.getString("wname");
                                String wpass = jsonResponse.getString("wpass");
                                String name1 = jsonResponse.getString("name1");
                                String username = jsonResponse.getString("username");
                                // System.out.println(name + email + wname + wpass + username);
                                Intent intent = new Intent(UserAreaActivity.this, ViewWiFiActivity.class);
                                intent.putExtra("name",name);
                                intent.putExtra("username",username);
                                intent.putExtra("email",email);
                                intent.putExtra("name1",name1);
                                intent.putExtra("wname",wname);
                                intent.putExtra("wpass",wpass);
                                startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(UserAreaActivity.this);
                                builder.setMessage("No WIFI Found for you !!")
                                        .setNegativeButton("Retry",null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ViewWiFi vw = new ViewWiFi(username,email, name, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserAreaActivity.this);
                queue.add(vw);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
