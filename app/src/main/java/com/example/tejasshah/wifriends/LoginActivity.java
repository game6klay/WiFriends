package com.example.tejasshah.wifriends;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    android.support.v7.app.ActionBar actionBar;
    Button bLogin;
    TextView registerLink;
    EditText etPassword;
    EditText etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPassword = ( EditText) findViewById(R.id.etPassword);
        etUsername = ( EditText) findViewById(R.id.etUsername);

        bLogin = (Button) findViewById(R.id.bLogin);

        registerLink = (TextView) findViewById( R.id.tvRegisterHere);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);

            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                String p = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
                ErrorDialogue ed = new ErrorDialogue();
                if (username.isEmpty()){
                    ed.showErrorText("Please Enter a UserName",LoginActivity.this);
                }
                else if(password.isEmpty()){
                    ed.showErrorText("Please Enter a Password", LoginActivity.this);
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean( "success");
                                if(success){
                                    Toast.makeText(getBaseContext(),"Logged In Successfully !!!",Toast.LENGTH_SHORT).show();
                                    String name = jsonResponse.getString("name");
                                    String email = jsonResponse.getString("email");
                                    System.out.println(email);
                                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                    intent.putExtra("name",name);
                                    intent.putExtra("username",username);
                                    intent.putExtra("email",email);
                                    startActivity(intent);
                                    finish();


                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Login Failed")
                                            .setNegativeButton("Retry",null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(username, p, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }


            }
        });
    }

}