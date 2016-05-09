package com.example.tejasshah.wifriends;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tejasshah.wifriends.models.Friends;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFriendsActivity extends AppCompatActivity {
    EditText etSearchFriend;
    ListView lvMyFriends;
    static String username,email,name;
    ArrayList<Friends> friendsList;
    FloatingActionButton fab;
    private static MyFriendsAdapter adapt_myFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        Intent i = getIntent();
        username = i.getStringExtra("username");
        email = i.getStringExtra("email");
        name = i.getStringExtra("name");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new LoadFriendsData().execute("http://selvinphp.netau.net/ViewFriends.php");

            }
        });





        etSearchFriend = (EditText)findViewById(R.id.etSearchFriend);
        lvMyFriends = (ListView)findViewById(R.id.lvMyFriends);

      /*  etSearchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapt_myFriends.getFilter().filter(s);
                Snackbar.make(getCurrentFocus(),"Code for Remove Friends Needed",Snackbar.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


    }

    private class FetchMyFriends extends StringRequest {
        private static final String FETCH_FRIEND_URL = "http://selvinphp.netau.net/MyFriends.php";
        private Map<String, String> params;

        public FetchMyFriends( String username, Response.Listener<String> listener){
            super (Request.Method.POST, FETCH_FRIEND_URL, listener, null);
            params = new HashMap<>();
            params.put("username",username);

        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        View v = findViewById(R.id.etSearchFriend);
        if(!isInternetPresent){
            Snackbar.make(v,"Unable to Load Friends \nPlease Connect to the Internet",Snackbar.LENGTH_LONG)
                    .setAction("Settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(i);
                        }
                    }).show();
        }else{
           // new LoadFriends().execute("http://selvinphp.netau.net/MyFriends.php");

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            //Toast.makeText(c, "Logged In Successfully !!!", Toast.LENGTH_SHORT).show();
                            //String name = jsonResponse.getString("name");
                            //String email = jsonResponse.getString("email");
                            //System.out.println(email);
                            //Intent intent = new Intent(LoginActivity.this, Home.class);
                            //intent.putExtra("name", name);
                            //intent.putExtra("username", username);
                            //intent.putExtra("email", email);
                            //startActivity(intent);
                            //finish();
                            //MyFriendsActivity myFriendsActivity = new MyFriendsActivity();
                            //myFriendsActivity.getAdapt_myFriends().notifyDataSetChanged();

                        } else {

                            Snackbar.make(getCurrentFocus(),"Failed to Add Friend",Snackbar.LENGTH_LONG).show();
                                    /*AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Login Failed")
                                            .setNegativeButton("Retry",null)
                                            .create()
                                            .show();*/
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            };
            FetchMyFriends fetchMyFriends = new FetchMyFriends(username, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(fetchMyFriends);
        }
        super.onPostCreate(savedInstanceState);
    }

    class LoadFriends extends AsyncTask<String,Void,ArrayList<Friends>>{
        @Override
        protected ArrayList<Friends> doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);


                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", username);
                String query = builder.build().getEncodedQuery();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                //readStream(in);

                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                JSONArray jArr = new JSONArray(buffer.toString());
                friendsList = new ArrayList<Friends>();
                for(int i=0;i< jArr.length();i++){
                    JSONObject jObj = jArr.getJSONObject(i);
                    String name = jObj.get("name").toString();
                    String username = jObj.get("username").toString();
                    int imgResource = R.drawable.user_icon;
                    Friends friends = new Friends(name,imgResource,username);
                    friendsList.add(friends);
                }
                return friendsList;



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!= null){
                    connection.disconnect();
                }
            }try{
                reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<Friends> friendses) {
            super.onPostExecute(friendses);
            if(friendses.size()>0){
                adapt_myFriends= new MyFriendsAdapter(MyFriendsActivity.this,friendses);
                lvMyFriends.setAdapter(adapt_myFriends);
            }
            lvMyFriends.setTextFilterEnabled(true);
            lvMyFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getBaseContext(),"Test",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class LoadFriendsData extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);


                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoInput(true);
                connection.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", username);
                String query = builder.build().getEncodedQuery();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                //readStream(in);

                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                //JSONArray jArr = new JSONArray(buffer.toString());
                return buffer.toString();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }finally {
                if(connection!= null){
                    connection.disconnect();
                }
            }try{
                reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent registerIntent = new Intent(MyFriendsActivity.this, SearchFriendsActivity.class);
            registerIntent.putExtra("JSONdata",s);
            registerIntent.putExtra("name", name);
            registerIntent.putExtra("username", username);
            registerIntent.putExtra("email", email);
            MyFriendsActivity.this.startActivity(registerIntent);

        }
    }

    public static String getUsername() {
        return username;
    }

    public static MyFriendsAdapter getAdapt_myFriends() {
        return adapt_myFriends;
    }
}
